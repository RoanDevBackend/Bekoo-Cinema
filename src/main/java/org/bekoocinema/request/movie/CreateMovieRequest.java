package org.bekoocinema.request.movie;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateMovieRequest {
    @NotBlank(message = "Tên tác giả không được bỏ trống")
    String director;
    //Diễn viên
    @NotBlank(message = "Tên diễn viên không được bỏ trống")
    String performer;
    String description;
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$",
    message = "Ngày khởi chiếu không đúng định dạng, yêu cầu minh hoạ 2004-24-12T00:00:00")
    String releaseDate;
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$",
            message = "Ngày dừng chiếu không đúng định dạng, yêu cầu minh hoạ 2004-24-12T00:00:00")
    String closeDate;
    @NotBlank
    String nation;
    @Min(value = 1, message = "Thời lượng phim không được nhỏ hơn 1")
    int duration;
    String note;
    @Min(value = 0, message = "Giá vé không được nhỏ hơn 0")
    int price;
    String trailerUrl;
    String posterUrl;
    @NotEmpty
    List<String> genreIds;
}
