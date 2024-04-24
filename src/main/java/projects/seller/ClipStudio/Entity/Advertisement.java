package projects.seller.ClipStudio.Entity;

import jakarta.persistence.*;

import java.sql.Time;
import java.util.List;

@Entity
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToMany
    List<Video> videos;
    String title;
}
