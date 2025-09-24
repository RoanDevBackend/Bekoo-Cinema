package org.bekoocinema.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String description;
    String director;
    //Diễn viên
    String performer;
    LocalDateTime releaseDate;
    LocalDateTime closeDate;
    String nation;
    int duration;
    String note;
    int price;
    String trailerUrl;
    String posterUrl;

    @ManyToMany
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Showtime> showtimes = new HashSet<>();
}
