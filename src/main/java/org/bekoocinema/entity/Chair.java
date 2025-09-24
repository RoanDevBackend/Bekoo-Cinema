package org.bekoocinema.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Chair {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    int colIdx;
    int rowIdx;
    String status;
    @ManyToOne
    @JoinColumn(name = "chair_type_id")
    ChairType chairType;
    @ManyToOne
    @JoinColumn(name = "room_id")
    Room room;

}
