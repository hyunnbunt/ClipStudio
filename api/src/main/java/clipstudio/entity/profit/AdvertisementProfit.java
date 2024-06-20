package clipstudio.entity.profit;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="advertisement_profit")
@IdClass(AdvertisementProfitKey.class)
public class AdvertisementProfit {
    @Id
    @Column(nullable = false)
    long advertisementNumber;
    @Id
    @Column(nullable = false)
    LocalDate date;
    @Column(nullable = false)
    double profit;
    @Column(nullable = false)
    long views;
}
