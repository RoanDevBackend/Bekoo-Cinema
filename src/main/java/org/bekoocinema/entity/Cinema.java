package org.bekoocinema.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String province;
    String district;
    String commune;
    String detailAddress;
    String phone;
    @Column(columnDefinition = "text")
    String description;
    //Mất điện, đang nâng cấp, sửa,..
    String status;
    // Nhớ thêm vào phần ảnh
    public String getAddress() {
        return this.district + " ," + this.commune + " ," + this.province;
    }
}
