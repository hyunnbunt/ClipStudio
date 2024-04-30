package projects.seller.ClipStudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.seller.ClipStudio.Entity.Video;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByNumber(long number);
}
