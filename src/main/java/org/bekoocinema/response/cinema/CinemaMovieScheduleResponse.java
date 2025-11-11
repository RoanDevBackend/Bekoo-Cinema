package org.bekoocinema.response.cinema;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CinemaMovieScheduleResponse {
    String cinemaId;
    String cinemaName;
    String address;
    List<CinemaDateScheduleResponse> dates;
}
