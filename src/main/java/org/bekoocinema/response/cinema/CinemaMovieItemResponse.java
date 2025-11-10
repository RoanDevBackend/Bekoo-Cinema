package org.bekoocinema.response.cinema;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CinemaMovieItemResponse {
    String movieId;
    String movieName;
    String posterUrl;
    int duration;
    List<String> genres;
    List<CinemaShowtimeItemResponse> showtimes;
}
