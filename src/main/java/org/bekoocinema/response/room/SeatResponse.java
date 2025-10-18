package org.bekoocinema.response.room;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatResponse {
    String seatId;
    String seatName ;
    int price;
    int rowIdx;
    int colIdx;
}
