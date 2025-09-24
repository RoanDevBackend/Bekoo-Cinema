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
public class ChairType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    int price;

    @OneToMany
    List<Chair> chairs;
}
