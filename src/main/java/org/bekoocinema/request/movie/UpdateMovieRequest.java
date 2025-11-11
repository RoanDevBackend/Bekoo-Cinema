package org.bekoocinema.request.movie;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateMovieRequest {
    String name;
    String director;
    String performer;
    String description;
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$",
            message = "Ngày khởi chiếu không đúng định dạng, yêu cầu minh hoạ 2024-24-12T00:00:00")
    String releaseDate;
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$",
            message = "Ngày dừng chiếu không đúng định dạng, yêu cầu minh hoạ 2024-24-12T00:00:00")
    String closeDate;
    String nation;
    Integer duration;
    String note;
    Integer price;
    String trailerUrl;
    MultipartFile posterFile;
    List<String> genreIds;
}
