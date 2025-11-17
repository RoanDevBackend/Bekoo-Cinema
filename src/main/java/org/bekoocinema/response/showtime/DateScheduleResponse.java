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
public class DateScheduleResponse {
    String date;
    String dayOfWeek;
    List<CinemaScheduleResponse> cinemas;
}
