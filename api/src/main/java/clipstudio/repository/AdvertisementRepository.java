package clipstudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import clipstudio.Entity.Advertisement;

import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    Optional<Advertisement> findByVideoNumberAndOrderInVideo(Long videoNumber, Integer orderInVideo);
}
