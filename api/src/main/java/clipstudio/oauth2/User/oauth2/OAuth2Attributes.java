package clipstudio.oauth2.User.oauth2;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import clipstudio.oauth2.User.Role;
import clipstudio.oauth2.User.SocialType;
import clipstudio.oauth2.User.User;
import clipstudio.oauth2.User.userinfo.GoogleOAuth2UserInfo;
import clipstudio.oauth2.User.userinfo.OAuth2UserInfo;

import java.util.Map;

/**
 * 각 소셜에서 받아오는 데이터가 다르므로
 * 소셜별로 데이터를 받는 데이터를 분기 처리하는 DTO 클래스 => 지금은 google 밖에 없으므로, 받아온 데이터를
 */
@Getter
@Slf4j
public class OAuth2Attributes {

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