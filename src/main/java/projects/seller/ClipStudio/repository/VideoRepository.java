package projects.seller.ClipStudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.seller.ClipStudio.Entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {
    // extends, implements ?
}
