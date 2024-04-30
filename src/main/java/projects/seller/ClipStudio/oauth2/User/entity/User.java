package projects.seller.ClipStudio.oauth2.User.entity;

import jakarta.persistence.*;
import lombok.*;
import projects.seller.ClipStudio.oauth2.User.entity.enums.Role;
import projects.seller.ClipStudio.oauth2.User.entity.enums.SocialType;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
@Table(name="users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long number;
    private String username;
    @Column(nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role; // USER, SELLER
    private LocalDateTime createDate;
    @Enumerated(EnumType.STRING)
    private SocialType socialType; // GOOGLE, ...
    private String socialId;

    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.role = Role.user; // 일단 모든 사용자는 user로 권한 설정됨
    }
}
