package com.example.oauth2_login.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
// 요청으로부터 전달된 JWT 토큰을 검증하는데 사용되는 클래스
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 전달된 request에서 jwt 토큰을 가져온다
            String jwt = getJwtFromRequest(request);

            // 가저온 토큰의 유효성 검사 후
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // 토큰에 있는 사용자 id를 가져온다
                Long userId = tokenProvider.getUserIdFromToken(jwt);

                // 가져온 사용자 id로 사용자 정보를 가져온다
                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                // 이 정보로 UsernamePasswordAuthenticationToken을 만들어 인증하는 과정을 거친다.
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception ex) {
            log.error("보안 컨텍스트에서 사용자 인증을 설정할 수 없습니다.",ex);
        }
        filterChain.doFilter(request,response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
