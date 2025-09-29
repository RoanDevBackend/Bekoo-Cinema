package org.bekoocinema.response.movie;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieResponse {
    String id;
    String name;
    String description;
    String director;
    String performer;
    LocalDateTime releaseDate;
    LocalDateTime closeDate;
    String nation;
    int duration;
    String note;
    int price;
    String trailerUrl;
    String posterUrl;
    List<String> genres;
}
