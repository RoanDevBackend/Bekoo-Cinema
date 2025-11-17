package org.bekoocinema.response.statistics;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticsResponse {
    long totalBookings;
    long totalRevenue;
    long totalCinemas;
    long totalRooms;
    long totalMovies;
    long totalGenres;
}
