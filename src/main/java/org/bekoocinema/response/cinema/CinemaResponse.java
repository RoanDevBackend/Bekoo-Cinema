package org.bekoocinema.response.cinema;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CinemaResponse {
    String id;
    String name;
    String province;
    String district;
    String commune;
    String detailAddress;
    String phone;
    String description;
    String status;
    List<String> urlImages;
}
