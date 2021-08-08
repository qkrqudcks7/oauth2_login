package com.example.oauth2_login.security.oauth2;

import com.example.oauth2_login.config.AppProperties;
import com.example.oauth2_login.exception.BadRequestException;
import com.example.oauth2_login.security.TokenProvider;
import com.example.oauth2_login.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.example.oauth2_login.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
@Slf4j
// 인증이 성공하면 스프링 시큐리티는 이 메소드를 호출합니다.
//이 메소드에서는 몇 가지 유효성 검사를 수행하고, JWT 인증 토큰을 만들고, 쿼리 문자열에 추가 된 JWT 토큰을 사용하여 클라이언트가 지정한 redirect_uri로 사용자를 리디렉션합니다.
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AppProperties appProperties;

    private final TokenProvider tokenProvider;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        // 응답이 commit 됐으면
        if (response.isCommitted()) {
            log.debug("해당 응답은 이미 처리되었습니다." + targetUrl + "이 주소에 접속할 수 없습니다.");
            return;
        }
        // 인증 과정에서 세션에 저장되었을 수 있는 임시 인증 관련 데이터를 제거합니다.
        clearAuthenticationAttributes(request,response);
        getRedirectStrategy().sendRedirect(request,response,targetUrl);

    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("죄송합니다! 승인되지 않은 리디렉션 URI가 있어 인증을 진행할 수 없습니다.");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        String token = tokenProvider.createToken(authentication);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token",token)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // 호스트와 포트만 확인. 만약 클라이언트가 원하는 경우, 다른 경로를 사용하도록 허용한다.
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if (authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost()) && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
