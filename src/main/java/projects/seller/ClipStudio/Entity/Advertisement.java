package projects.seller.ClipStudio.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name="advertisements")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long number;
    @ManyToOne
    @JoinColumn(nullable = false, name="video_number")
    public Video video;
    @Column(nullable = false)
    public Integer orderInVideo; // order in video.
    @Column(nullable = false)
    public Long views;
}
