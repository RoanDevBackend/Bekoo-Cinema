package org.bekoocinema.request.room;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSeatRequest {
    String seatName ;
    String seatType ;
    int price;
    int rowIdx;
    int colIdx;
}
