package org.bekoocinema.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bekoocinema.entity.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {

    @Schema(example = "null")
    String firstName;

    @Schema(example = "null")
    String lastName;

    @Schema(example = "null")
    @Email(message = "Sai định dạng email", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    String email;

    @Schema(example = "null")
    @Size(min = 8, message = "Mật khẩu tối thiểu 8 kí tự")
    String password;

    @Schema(example = "null")
    @Pattern(regexp = "^(0|\\+84)(3[2-9]|5[2689]|7[0-9]|8[1-9]|9[0-9])[0-9]{7}$", message = "Sai định dạng số điện thoại")
    String phone;

    @Schema(example = "null")
    String role;

    @Schema(example = "null")
    Status status;
}
