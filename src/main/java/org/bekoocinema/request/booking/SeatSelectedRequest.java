package org.bekoocinema.request.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatSelectedRequest {
    String showtimeId;
    String seatId;
    boolean isSelected;
}
