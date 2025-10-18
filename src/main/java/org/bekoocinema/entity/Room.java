package org.bekoocinema.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    int totalRow;
    int totalCol;
    @ManyToOne
            @JoinColumn(name = "cinema_id", nullable = false)
    Cinema cinema;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Showtime> showtimes = new HashSet<>();
    @OneToMany(mappedBy = "room")
    Set<Seat> seats = new HashSet<>();
}
