package com.example.oauth2_login.util;

import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        // HttpServletRequest의 request의 정보를 읽는다
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    // 반드시 값이 있어야 하는 객체인 경우 .of 사용 null이면 exception발동
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name,value);
        cookie.setPath("/");
        // document.cookie와 같은 자바스크립트로 쿠키를 조회하는 것을 막는 옵션, XSS 공격 방지, 브라우저에서 조회 금지
        // 서버로 HTTP Request 요청을 보낼때만 쿠키를 전송한다.
        cookie.setHttpOnly(true);
        // 초 단위
        cookie.setMaxAge(maxAge);
        // HttpServletResponse객체에 cookie를 담아서 전송송
       response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("/");
                    cookie.setPath("/");
                    // 0: 즉시 지우기 , -1: 세션이 끝나면 지우기
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                // 자바 직렬화: JVM의 메모리에 상주되어 있는 데이터를 Byte형태로 변환
                // SerializationUtils.serialize(직렬화): 데이터 -> byte
                .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        // cls의 개체가 나타내는 클래스 또는 인터페이스에 개체를 캐스트한다.
        // SerializationUtils.deserialize(역직렬화): byte -> 데이터
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
