package org.bekoocinema.response.cinema;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CinemaDateScheduleResponse {
    String date;
    List<CinemaMovieItemResponse> movies;
}
