package org.bekoocinema.request.room;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRoomRequest {
    String name;
    Integer totalRow;
    Integer totalCol;
    List<CreateSeatRequest> seats;
}
