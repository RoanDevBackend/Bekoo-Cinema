package org.bekoocinema.request.cinema;

import jakarta.validation.constraints.NotBlank;
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
public class CreateCinemaRequest {
    @NotBlank(message = "Tên không được bỏ trống")
    String name;
    @NotBlank(message = "Địa chỉ tỉnh không được bỏ trống")
    String province;
    String district;
    @NotBlank(message = "Địa chỉ xã không được bỏ trống")
    String commune;
    String detailAddress;
    @NotBlank(message = "Số điện thoại không được bỏ trống")
    @Pattern(regexp = "^(0|\\+84)(3[2-9]|5[2689]|7[0-9]|8[1-9]|9[0-9])[0-9]{7}$", message = "Sai định dạng số điện thoại")
    String phone;
    String description;
    List<MultipartFile> files;
}
