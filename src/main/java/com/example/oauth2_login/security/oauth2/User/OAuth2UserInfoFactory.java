package com.example.oauth2_login.security.oauth2.User;

import com.example.oauth2_login.model.AuthProvider;
import com.example.oauth2_login.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String,Object> attributes) {
        // equalsIgnoreCase: 대소문자 구분없이 문자열 자체만으로 비교
        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.github.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException(registrationId + "로그인은 지원이 안됩니다.");
        }
    }
}
