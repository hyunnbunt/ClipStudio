package clipstudio.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import clipstudio.oauth2.User.userRepository.UserRepository;
import clipstudio.oauth2.User.oauth2.CustomOAuth2User;
//import clipstudio.oauth2.jwt.service.JwtService;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

//    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            log.info(authentication.getPrincipal().toString());
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            log.info("user email: " + oAuth2User.getEmail());
//            loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
//            response.sendRedirect("/hello"); // redirect uri 대신 다른 주소로 보냄
        } catch (Exception e) {
            throw e;
        }
    }

    // TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
//    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
//        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
//        String refreshToken = jwtService.createRefreshToken();
//        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
//        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);
//        log.info("accessToken: " + accessToken);
//        log.info("refreshToken: " + refreshToken);
//        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
//        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
//    }
}