package projects.seller.ClipStudio.oauth2.User.userinfo;

import java.util.Map;

public abstract class OAuth2UserInfo { // 소셜 타입별로 유저 정보를 가지는 OAuth2UserInfo 추상 클래스 (구글 뿐 아니라 다른 소셜 로그인도 이 클래스 상속받아 해당 유저 정보 클래스를 구현할 수 있다)
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId(); //소셜 식별 값 : 구글 - "sub", 카카오 - "id", 네이버 - "id"

    public abstract String getUsername();

    public abstract String getEmail();
}
