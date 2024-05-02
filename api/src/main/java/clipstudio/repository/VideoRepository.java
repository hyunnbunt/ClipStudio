package clipstudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import clipstudio.Entity.Video;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByNumber(long number);
}
