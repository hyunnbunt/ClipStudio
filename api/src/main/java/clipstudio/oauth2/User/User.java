package clipstudio.oauth2.User;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
@Table(name="users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long number;
    @Column
    private String username;
    @Column(nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role; // USER, SELLER
    @Column // social login 수정하고 나서 => nullable = true 로 수정
    private LocalDate createdDate;
    @Enumerated(EnumType.STRING)
    private SocialType socialType; // GOOGLE, ...
    private String socialId;

    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.role = Role.user; // 일단 모든 사용자는 user로 권한 설정됨
    }
}
