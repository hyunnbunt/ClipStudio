package clipstudio.repository;

import clipstudio.oauth2.User.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import clipstudio.Entity.Video;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByNumber(long number);
    Optional<List<Video>> findByUploader(User user);
}
