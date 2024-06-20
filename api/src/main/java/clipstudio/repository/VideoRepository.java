package clipstudio.repository;

import clipstudio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import clipstudio.entity.Video;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByNumber(long number);
    Optional<List<Video>> findByUploader(User user);
}
