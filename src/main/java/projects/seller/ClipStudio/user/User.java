package projects.seller.ClipStudio.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "USERS")
@AllArgsConstructor
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
    @Enumerated(EnumType.STRING)
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
}
