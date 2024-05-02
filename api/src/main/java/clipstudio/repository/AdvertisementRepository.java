package projects.seller.ClipStudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.seller.ClipStudio.Entity.Advertisement;

import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    Optional<Advertisement> findByVideoNumberAndOrderInVideo(Long videoNumber, Integer orderInVideo);
}
