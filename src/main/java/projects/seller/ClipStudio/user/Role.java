package projects.seller.ClipStudio.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    user("ROLE_USER"), seller("ROLE_SELLER"); // key, description : 코드에 항상 ROLE_ 붙어야 함
    private final String key;
}
