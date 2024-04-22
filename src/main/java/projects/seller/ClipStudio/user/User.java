package projects.seller.ClipStudio.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role; // USER, SELLER
    private String provider;
    private String provideId;
    private LocalDateTime createDate;
    private SocialType socialType; // GOOGLE, ...
    private String socialId;
    private String refreshToken;

    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.role = Role.user; // 일단 모든 사용자는 user로 권한 설정됨
    }
    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
    @Builder
    public User(String username, String password, String email, Role role, String provider, String provideId, LocalDateTime createDate, SocialType socialType, String socialId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.provideId = provideId;
        this.createDate = createDate;
        this.socialId = socialId;
        this.socialType = socialType;
    }
}
