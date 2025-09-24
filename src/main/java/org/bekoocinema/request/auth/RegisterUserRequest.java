package org.bekoocinema.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterUserRequest {
    @NotBlank(message = "Họ không được bỏ trống")
    String firstName;
    @NotBlank(message = "Tên và tên đệm không được bỏ trống")
    String lastName;
    @NotBlank(message = "Email không được bỏ trống")
    @Email(message = "Sai định dạng email", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    String email;
    @NotBlank(message = "Email không được bỏ trống")
            @Size(min = 8, message = "Mật khẩu tối thiểu 8 kí tự")
    String password;
    @NotBlank(message = "Số điện thoại không được bỏ trống")
    @Pattern(regexp = "^(0|\\+84)(3[2-9]|5[2689]|7[0-9]|8[1-9]|9[0-9])[0-9]{7}$", message = "Sai định dạng số điện thoại")
    String phone;
}
