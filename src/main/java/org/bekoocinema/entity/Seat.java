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
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String seatName;
    String seatType;
    int price;
    int colIdx;
    int rowIdx;
    String status;
    boolean isBooked;
    @ManyToOne
    @JoinColumn(name = "room_id")
    Room room;
}
