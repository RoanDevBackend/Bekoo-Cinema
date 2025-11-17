package org.bekoocinema.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Indexed
public class Movie extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @FullTextField(analyzer = "vietnameseAnalyzer")
    @KeywordField(name = "name_sort", normalizer = "lowercase", sortable = Sortable.YES)
    String name;
    @Column(columnDefinition = "text")
    @FullTextField(analyzer = "vietnameseAnalyzer")
    String description;
    String director;
    //Diễn viên
    String performer;
    @GenericField(sortable = Sortable.YES)
    LocalDateTime releaseDate;
    LocalDateTime closeDate;
    String nation;
    int duration;
    String note;
    @GenericField(sortable = Sortable.YES)
    int price;
    String trailerUrl;
    String posterUrl;

    @ManyToMany
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @IndexedEmbedded(includePaths = {"id", "name"})
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Showtime> showtimes = new HashSet<>();
}
