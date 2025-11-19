package org.bekoocinema.service.impl;

import lombok.RequiredArgsConstructor;
import org.bekoocinema.repository.*;
import org.bekoocinema.response.statistics.StatisticsResponse;
import org.bekoocinema.service.StatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    final BookingRepository bookingRepository;
    final CinemaRepository cinemaRepository;
    final RoomRepository roomRepository;
    final MovieRepository movieRepository;
    final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public StatisticsResponse getStatistics() {
        long totalBookings = bookingRepository.countByPaymentStatus("Đã thanh toán thành công");

        Long totalRevenue = bookingRepository.sumTotalPriceByPaymentStatus("Đã thanh toán thành công");
        if (totalRevenue == null) {
            totalRevenue = 0L;
        }

        long totalCinemas = cinemaRepository.count();

        long totalRooms = roomRepository.count();

        long totalMovies = movieRepository.count();

        long totalGenres = genreRepository.count();

        return StatisticsResponse.builder()
                .totalBookings(totalBookings)
                .totalRevenue(totalRevenue)
                .totalCinemas(totalCinemas)
                .totalRooms(totalRooms)
                .totalMovies(totalMovies)
                .totalGenres(totalGenres)
                .build();
    }
}
