package clipstudio.oauth2.User.userinfo;
import clipstudio.entity.User;
import clipstudio.oauth2.User.Role;
import clipstudio.oauth2.User.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
//    구글 유저 정보 Response JSON
//    {
//        "sub": "식별값",
//            "name": "name",
//            "given_name": "given_name",
//            "picture": "https//lh3.googleusercontent.com/~~",
//            "email": "email",
//            "email_verified": true,
//            "locale": "ko"
//    }

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }


    @Getter
    public static class CustomOAuth2User extends DefaultOAuth2User {
        // OAuth2UserService에서 사용할 OAuth2User 객체를 커스텀한 클래스(추가할 필드가 있어서 사용) OAuth2UserService에서 사용하도록 할 예정
        private Role role;
        private String email;
        private SocialType socialType;
        private String socialId;
        /**
         * Constructs a {@code DefaultOAuth2User} using the provided parameters.
         *
         * @param authorities      the authorities granted to the user
         * @param attributes       the attributes about the user
         * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
         *                         {@link #getAttributes()}
         */
        public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                                Map<String, Object> attributes, String nameAttributeKey,
                                String email, Role role, SocialType socialType, String socialId) {
            super(authorities, attributes, nameAttributeKey);
            this.role = role;
            this.email = email;
            this.socialType = socialType;
            this.socialId = socialId;
            // super()로 부모 객체인 DefaultOAuth2User를 생성
            // role 파라미터를 추가로 받아서, 주입하여 CustomOAuth2User를 생성
        }
    }

    /**
     * 각 소셜에서 받아오는 데이터가 다르므로
     * 소셜별로 데이터를 받는 데이터를 분기 처리하는 DTO 클래스 => 지금은 google 밖에 없으므로, 받아온 데이터를
     */
    @Getter
    @Slf4j
    public static class OAuth2Attributes {

        private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
        private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)


        @Builder
        private OAuth2Attributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
            this.nameAttributeKey = nameAttributeKey;
            this.oauth2UserInfo = oauth2UserInfo;
        }

        /**
         * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
         * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
         * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
         * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
         */

        public static OAuth2Attributes ofGoogle(SocialType socialType, String userNameAttributeName, Map<String, Object> attributes) {
            // 인스트럭션의 of 메소드에서 소셜 별로 OAuthAttributes를 다르게 생성하는 분기 기능 필요 없기 때문에(google 로그인 하나만 진행) of => ofGoogle 로 압축
            log.info(socialType + " 로그인 시작");
            return OAuth2Attributes.builder()
                    .nameAttributeKey(userNameAttributeName)
                    .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                    .build();
        }

        /**
         * of(ofGoogle) 메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
         * OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
         * role은 user로 설정
         */
        public User toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
            return User.builder()
                    .socialType(socialType)
                    .socialId(oauth2UserInfo.getId())
                    .username(oauth2UserInfo.getUsername())
                    .email(oauth2UserInfo.getEmail())
                    .role(Role.user)
                    .build();
        }
    }
}
