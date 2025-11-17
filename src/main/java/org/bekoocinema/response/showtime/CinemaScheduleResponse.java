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
public class CinemaScheduleResponse {
    String cinemaId;
    String cinemaName;
    String address;
    String province;
    String district;
    List<MovieScheduleResponse> movies;
}
