//package clipstudio.user.auth;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import clipstudio.user.User;
//import clipstudio.user.UserRepository;
//
//public class PrincipalOauthUserService extends DefaultOAuth2UserService {
//    @Autowired
//    private UserRepository userRepository;
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
//            log..
//        }
//    }
//}
