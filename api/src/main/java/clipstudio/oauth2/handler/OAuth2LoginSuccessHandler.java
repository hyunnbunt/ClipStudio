package clipstudio.oauth2.handler;

import clipstudio.oauth2.User.userinfo.GoogleOAuth2UserInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import clipstudio.repository.UserRepository;
//import clipstudio.oauth2.jwt.service.JwtService;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공");
        log.info(authentication.getPrincipal().toString());
        GoogleOAuth2UserInfo.CustomOAuth2User oAuth2User = (GoogleOAuth2UserInfo.CustomOAuth2User) authentication.getPrincipal();
        // CustomOAuth2User로 바꾸고 role 추가할 것
    }
}