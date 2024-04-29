package projects.seller.ClipStudio.oauth2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import projects.seller.ClipStudio.oauth2.handler.OAuth2LoginFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import projects.seller.ClipStudio.oauth2.handler.OAuth2LoginSuccessHandler;
//import projects.seller.ClipStudio.oauth2.User.service.CustomOAuth2UserService;
//import projects.seller.ClipStudio.oauth2.jwt.service.JwtService;
import projects.seller.ClipStudio.oauth2.User.userRepository.UserRepository;

import java.util.Arrays;
//import projects.seller.ClipStudio.oauth2.filter.JwtAuthenticationProcessingFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
//    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
//    private final CorsConfig corsConfig;
//    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
//    private final CustomOAuth2UserService customOAuth2UserService;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated());
//                .requestMatchers(new AntPathRequestMatcher("/videos/**")).hasAnyAuthority("ROLE_USER", "ROLE_SELLER")
//                .requestMatchers(new AntPathRequestMatcher("/seller/**")).hasAuthority("ROLE_SELLER")
//                .requestMatchers(new AntPathRequestMatcher("/hello")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/videos/test/new")).permitAll()
//                .anyRequest().authenticated());

//        http.oauth2Login(Customizer.withDefaults()); // OAuth2 기본 설정 : "/login" 으로 접속하면 google 로그인으로 연결되고, 성공시 "/"으로 리다이렉트, "resources/static/index.html"가 있다면 보여준다. 나는 apicontroller로 응답해주기로 함.

            http.oauth2Login(oAuth2LoginConfigurer -> oAuth2LoginConfigurer
                    .defaultSuccessUrl("http://localhost:5173", true));
//                        .successHandler(new SimpleUrlAuthenticationSuccessHandler("/hello")));
//                        .failureHandler(oAuth2LoginFailureHandler)));

        return http.build();
    }
//    @Bean
//    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
//        return new JwtAuthenticationProcessingFilter(jwtService, userRepository);
//    }

}
//    @Autowired
//    private PrincipalOauthUserService principalOauthUserService;
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable);
//        http.cors(AbstractHttpConfigurer::disable);
//        http.oauth2Login(Customizer.withDefaults());
//        return http.build();
//    }
//    ClientRegistrationRepository clientRegistrationRepository;

//        http.oauth2Login((oauth2) -> oauth2
//        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(principalOauthUserService)));

//        http.formLogin(form -> form.loginPage("/loginForm") // GET, 렌더링할 로그인 페이지를 직접 지정.
//                .loginProcessingUrl("/login") // POST, 로그인 요청(POST) 시큐리티가 처리.
//                .defaultSuccessUrl("/login/oauth2/code/google") // 로그인 성공시 이동할 페이지. 인덱스 페이지 리턴해줄 주소.
//                .permitAll());

////        );


//            http.oauth2Login(oAuth2LoginConfigurer -> oAuth2LoginConfigurer
//                    .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig // 회원정보를 바로 줘. token 주지 마.
//                                .baseUri("/oauth2/authorize") // 이 uri로 접근시 oauth 로그인 요청, 프론트에서 버튼에 넣는 부분.
//                                .authorizationRequestRepository(requestAuthorizationRequestRepository))
//                        .redirectionEndpoint(redirectionEndpointConfig -> oAuth2LoginConfigurer
//                                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
//                                                .userService(oAuth2UserService))
//                        .successHandler(authenticationSuccessHandler)
//                        .failureHandler(authenticationFailureHandler)));


//        return http.build();
//    }};
//    }
//    @Bean
//    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> httpSessionOAuth2AuthorizationRequestRepository() {
//        return new HttpSessionOAuth2AuthorizationRequestRepository();
//    }
//
//    @Bean
//    public AuthenticationFailureHandler authenticationFailureHandler() {
//        return new SimpleUrlAuthenticationFailureHandler();
//    }
//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler() {
//        return new SimpleUrlAuthenticationSuccessHandler();
//    }
//
//    @Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
//        return new DefaultOAuth2UserService();
//    }
//
//}
