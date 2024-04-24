package projects.seller.ClipStudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.seller.ClipStudio.Entity.Video;

public interface videoRepository extends JpaRepository<Video, Long> {
    // extends, implements ?
}
