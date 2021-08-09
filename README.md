# :fire: OAuth2

## oauth2 로그인 동작 방식

### 1.사용자 측의 브라우저에서 엔드포인트로 접속


<code>
.baseUri("/oauth2/authorize")
</code>

baseUri인  http://localhost:8080/oauth2/authorize/{provider}?redirect_uri=http://localhost:8080/oauth2/redirect 로부터 시작한다

{provider}는 google, facebook, github를 설정해두었다.

인증 요청을 받으면 OAuth2 클라이언트는 사용자를 제공된 provider의 권한이 부여된 url로 넘겨줍니다.


### 2. 권한 정보 저장

<code>
.authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository())
</code>

권한 요청과 관련된 모든 상태는 여기에 cookieOAuth2AuthorizationRequestRepository에 저장

### 3. 권한 허용/거부

사용자가 provider 페이지에서 권한을 허용하거나 거부할 수 있다.

#### 3.1 거부

사용자가 권한을 거부하면 
<code>
.baseUri("/oauth2/callback/*")
</code>
똑같은 callbackUrl로 연결되지만 error가 발생한다.

error가 발생하면 다음을 호출한다.

<code>
.failureHandler(oAuth2AuthenticationFailureHandler);
</code>

### 4. 허용, 콜백 성공

사용자가 권한을 허용하면
<code>
.baseUri("/oauth2/callback/*")
</code>

사이트는 사용자를 인증 코드와 함께 콜백 url로 연결시킨다.

Spring Security는 access_token에 대한 인증 코드를 교환하고,

다음을 호출합니다.
<code>
.userService(customOAuth2UserService)
</code>


### 5. 인증된 사용자 검사

customOAuth2UserService는 인증된 사용자를 검색하여,

db에 없다면 추가하고, 있다면 업데이트합니다. 

### 6.




