package projects.seller.ClipStudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.seller.ClipStudio.Entity.AdIn;

import java.util.Optional;

public interface AdInRepository extends JpaRepository<AdIn, Long> {
    Optional<AdIn> findByVideoNumberAndOrderInVideo(Long videoNumber, Integer orderInVideo);
}
