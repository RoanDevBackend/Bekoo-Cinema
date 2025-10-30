package org.bekoocinema.response.showtime;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bekoocinema.response.room.RoomResponse;
import org.bekoocinema.response.room.SeatResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShowtimeDetailResponse {
    String id;
    String date;
    String timeline;
    RoomResponse roomResponse;
}
