package org.bekoocinema.request.genre;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateGenreRequest {
    @NotBlank(message = "Tên thể loại không được để trống")
    String name;
}
