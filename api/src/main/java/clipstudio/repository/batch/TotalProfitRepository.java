package clipstudio.repository.batch;

import clipstudio.entity.profit.TotalProfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TotalProfitRepository extends JpaRepository<TotalProfit, Long> {

    @Query(value = "SELECT * FROM total_profit WHERE uploader_number = :uploaderNumber AND date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<TotalProfit> findAllByUploaderNumberAndDateBetween(@Param("uploaderNumber") Long uploaderNumber, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT * FROM total_profit WHERE date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<TotalProfit> findAllByDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
