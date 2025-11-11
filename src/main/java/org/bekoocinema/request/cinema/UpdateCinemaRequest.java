package org.bekoocinema.request.cinema;

import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCinemaRequest {

    String name;
    String province;
    String district;
    String commune;
    String detailAddress;
    String phone;
    String description;
    String status;
    Boolean replaceImages; // true: xóa ảnh cũ và thay thế, false: giữ ảnh cũ và thêm ảnh mới
    List<MultipartFile> files;
}
