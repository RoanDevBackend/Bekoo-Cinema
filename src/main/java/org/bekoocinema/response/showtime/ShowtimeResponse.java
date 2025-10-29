package org.bekoocinema.response.showtime;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShowtimeResponse {
    String cinemaId;
    String cinemaName;
    String province;
    String district;
    String commune;
    String detailAddress;
    List<ShowtimeDetailResponse> showtimeDetails;
}
