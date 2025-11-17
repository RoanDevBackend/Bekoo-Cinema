package org.bekoocinema.response.showtime;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieScheduleResponse {
    String movieId;
    String movieName;
    String posterUrl;
    int duration;
    String ageRating;
    List<String> genres;
    List<ShowtimeItemResponse> showtimes;
}
