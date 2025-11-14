package org.bekoocinema.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileUpdateRequest {

    @Schema(example = "null")
    String firstName;
    @Schema(example = "null")
    String lastName;

    @Pattern(
        regexp = "^(0|\\+84)(3[2-9]|5[2689]|7[0-9]|8[1-9]|9[0-9])[0-9]{7}$",
        message = "Sai định dạng số điện thoại"
    )
    @Schema(example = "null")
    String phone;
}
