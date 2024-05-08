package clipstudio.oauth2.User.oauth2;

import clipstudio.oauth2.User.SocialType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import clipstudio.oauth2.User.Role;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
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
