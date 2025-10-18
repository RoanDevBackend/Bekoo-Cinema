package org.bekoocinema.request.room;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRoomRequest {
    String cinemaId;
    String name;
    int totalRow;
    int totalCol;
    List<CreateSeatRequest> seats;
}
