package projects.seller.ClipStudio.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AdIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long number;
    @ManyToOne
    @JoinColumn(nullable = false)
    public Video video;
    @Column
    public int orderInVideo; // order in video.
    @Column
    public long views;
}
