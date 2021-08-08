package com.example.oauth2_login.security.oauth2;

import com.example.oauth2_login.model.AuthProvider;
import com.example.oauth2_login.model.User;
import com.example.oauth2_login.exception.OAuth2AuthenticationProcessingException;
import com.example.oauth2_login.repository.UserRepository;
import com.example.oauth2_login.security.UserPrincipal;
import com.example.oauth2_login.security.oauth2.User.OAuth2UserInfo;
import com.example.oauth2_login.security.oauth2.User.OAuth2UserInfoFactory;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
// User 정보 가져오는 역할을 하는 클래스
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // Oauth2 공급자로부터 AccessToken을 받은 후 호출 된다. OAuth2 공급자로부터 사용자 정보를 가져온다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest,oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (StringUtils.isBlank(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("없는 이메일 주소 입니다.");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if (userOptional.isPresent()) {
            user=userOptional.get();
            if (!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException(user.getProvider() + "에 로그인 하신게 맞습니까?");
            }
            user = updateExistingUser(user,oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user,oAuth2User.getAttributes());
    }
    // DB에 존재하지 않을 경우 새로 등록
    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        return userRepository.save(User.builder()
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .imageUrl(oAuth2UserInfo.getImageUrl())
                .provider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId())).providerId(oAuth2UserInfo.getId()).build());
    }
    // DB에 존재할 경우 정보 업데이트
    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        return userRepository.save(existingUser)
                .update(oAuth2UserInfo.getName(),oAuth2UserInfo.getImageUrl());
    }
}
