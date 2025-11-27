package org.bekoocinema.response.report;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TotalReportResponse {
    long totalGenre;
    long totalMovie;
    long totalCinema;
    long totalRoom;
    long totalSeat;
    long totalUser;
    long totalPriceThisMonth;
}
