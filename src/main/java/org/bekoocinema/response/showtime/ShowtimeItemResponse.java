package org.bekoocinema.response.showtime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShowtimeItemResponse {
    String showtimeId;
    String startTime;
    String endTime;
    String roomId;
    String roomName;
    int availableSeats;
    int totalSeats;
}
