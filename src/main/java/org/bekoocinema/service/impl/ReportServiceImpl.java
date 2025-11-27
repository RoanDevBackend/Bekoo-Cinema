package org.bekoocinema.service.impl;

import lombok.RequiredArgsConstructor;
import org.bekoocinema.constant.ApplicationConstant;
import org.bekoocinema.repository.*;
import org.bekoocinema.response.report.ReportChartTemplateResponse;
import org.bekoocinema.response.report.TotalReportResponse;
import org.bekoocinema.service.ReportService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    final GenreRepository genreRepository;
    final MovieRepository movieRepository;
    final CinemaRepository cinemaRepository;
    final RoomRepository roomRepository;
    final SeatRepository seatRepository;
    final UserRepository userRepository;
    final BookingRepository bookingRepository;

    @Override
    public List<ReportChartTemplateResponse> getChartByPrice(LocalDate from, LocalDate to, int groupType) {
        List<ReportChartTemplateResponse> responses = new ArrayList<>();
        from = this.nextTo(from, groupType);
        to = this.nextTo(to, groupType);
        while (from.isBefore(to)) {
            LocalDateTime start = from.atStartOfDay();
            LocalDateTime end = this.nextTo(from, groupType).atStartOfDay();
            Long wrValue = bookingRepository.getTotalPrice(start, end);
            long value = wrValue == null ? 0 : wrValue;
            responses.add(new ReportChartTemplateResponse(from, value));
            from = nextTo(from, groupType);
        }
        return responses;
    }

    @Override
    public List<ReportChartTemplateResponse> getChartByBooking(LocalDate from, LocalDate to, int groupType) {
        List<ReportChartTemplateResponse> responses = new ArrayList<>();
        from = this.nextTo(from, groupType);
        to = this.nextTo(to, groupType);
        while (from.isBefore(to)) {
            LocalDateTime start = from.atStartOfDay();
            LocalDateTime end = this.nextTo(from, groupType).atStartOfDay();
            long value = bookingRepository.countByBookingDateBetween(start, end);
            responses.add(new ReportChartTemplateResponse(from, value));
            from = nextTo(from, groupType);
        }
        return responses;
    }

    @Override
    public TotalReportResponse getTotalReportResponse() {
        long totalGenre = genreRepository.count();
        long totalMovie = movieRepository.count();
        long totalCinema = cinemaRepository.count();
        long totalRoom = roomRepository.count();
        long totalSeat = seatRepository.count();
        long totalUser = userRepository.count() - 1; // trừ tài khoản admin
        Long wrTotalPriceThisMonth = bookingRepository.getTotalPrice(LocalDateTime.now().minusMonths(1), LocalDateTime.now());
        long totalPriceThisMonth = wrTotalPriceThisMonth == null ? 0 : wrTotalPriceThisMonth;

        return TotalReportResponse.builder()
                .totalGenre(totalGenre)
                .totalMovie(totalMovie)
                .totalCinema(totalCinema)
                .totalRoom(totalRoom)
                .totalSeat(totalSeat)
                .totalUser(totalUser)
                .totalPriceThisMonth(totalPriceThisMonth)
                .build();
    }

    private LocalDate nextTo(LocalDate from, int groupType) {
        if (groupType == (ApplicationConstant.DateType.DAY))
            return from.plusDays(1);
        else if (groupType == (ApplicationConstant.DateType.WEEK)) {
            return from.plusWeeks(1);
        } else if (groupType == (ApplicationConstant.DateType.MONTH)) {
            return from.plusMonths(1);
        } else {
            return from.plusYears(1);
        }
    }
}
