package org.bekoocinema.response.cinema;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CinemaShowtimeItemResponse {
    String showtimeId;
    String startTime;
    String endTime;
    String roomName;
}
