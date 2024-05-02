package projects.seller.ClipStudio.oauth2.User.userinfo;

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


}
