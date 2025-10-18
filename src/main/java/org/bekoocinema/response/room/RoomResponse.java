package org.bekoocinema.response.room;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomResponse {
    String roomId;
    String name;
    int totalRow;
    int totalCol;
    List<SeatResponse> seats;
}
