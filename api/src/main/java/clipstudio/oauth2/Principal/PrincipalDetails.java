//package clipstudio.user.auth;
//
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import clipstudio.user.User;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Map;
//
//@Data
//@Slf4j
//public class PrincipalDetails implements UserDetails {
//    private static final long serialVersionUID = 1L;
//    private User user;
//    private Map<String, Object> attributes;
//
//    public PrincipalDetails(User user, Map<String, Object> attributes) {
//        this.user = user;
//        this.attributes = attributes;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> collector = new ArrayList<>();
//        collector.add((GrantedAuthority) () -> String.valueOf(user.getRole()));
//        log.info(collector.toString());
//        return collector;
//    }
//
//    @Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUsername();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
