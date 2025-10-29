package org.bekoocinema.request.room;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateShowtimeRequest {
    String roomId;
    String movieId;
    String startTime;
    String endTime;
}
