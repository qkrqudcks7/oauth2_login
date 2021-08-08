package com.example.oauth2_login.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // 사용자가 인증없이 보안된 리소스에 액세스하려고 할 때 호출됩니다. 이 경우 401 Unauthorized 응답만 반환합니다.
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("승인되지 않은 접속입니다.",authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getLocalizedMessage());
    }
}
