package clipstudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import clipstudio.entity.Advertisement;

import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    Optional<List<Advertisement>> findByVideoNumber(Long videoNumber);
    Optional<Advertisement> findByVideoNumberAndOrderInVideo(Long videoNumber, int orderInVideo);
}
