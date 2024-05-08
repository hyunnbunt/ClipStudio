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

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공");
        log.info(authentication.getPrincipal().toString());
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        // CustomOAuth2User로 바꾸고 role 추가할 예정
    }
}