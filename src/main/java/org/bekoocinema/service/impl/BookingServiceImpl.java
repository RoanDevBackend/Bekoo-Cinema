package org.bekoocinema.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.entity.*;
import org.bekoocinema.mapper.BookingMapper;
import org.bekoocinema.repository.*;
import org.bekoocinema.request.booking.BookingRequest;
import org.bekoocinema.response.booking.BookingResponse;
import org.bekoocinema.service.BookingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    final ShowtimeRepository showtimeRepository;
    final SeatRepository seatRepository;
    final BookingRepository bookingRepository;
    final BookingMapper bookingMapper;

    @Override
    @Transactional
    public void booking(BookingRequest bookingRequest, User user) {
        Showtime showtime = showtimeRepository.findById(bookingRequest.getShowtimeId())
                .orElseThrow(
                        () -> new RuntimeException("Xuất chiếu không tồn tại")
                );
        long totalPrice = 0;
        StringBuilder seatsName = new StringBuilder();
        List<Seat> seatsBooked = seatRepository.getSeatInId(bookingRequest.getSeatIds());
        for(Seat seat : seatsBooked) {
            totalPrice =  totalPrice + seat.getPrice();
            if(seatsName.toString().equals("")) {
                seatsName = new StringBuilder(seat.getSeatName());
            }else {
                seatsName.append(", ").append(seat.getSeatName());
            }
            seat.setBooked(true);
        }
        seatRepository.saveAll(seatsBooked);
        Booking booking = new Booking();
        booking.setBookingDate(LocalDateTime.now());
        booking.setMovieId(showtime.getMovie().getId());
        booking.setMovieName(showtime.getMovie().getName());
        Set<Genre> genres = showtime.getMovie().getGenres();
        StringBuilder genreName = new StringBuilder();
        for(Genre genre : genres){
            if(genreName.equals("")) {
                genreName.append(genre.getName());
            }else {
                genreName.append(genre.getName()).append(",");
            }
        }
        booking.setGenreName(genreName.toString());
        booking.setSeats(seatsName.toString());
        booking.setRoomName(showtime.getRoom().getName());

        booking.setCinemaName(showtime.getRoom().getCinema().getName());
        booking.setCinemaAddress(showtime.getRoom().getCinema().getAddress());

        booking.setPaymentMethod(bookingRequest.getPaymentMethod() == 1 ? "Thanh toán tại quầy" : "Chuyển khoản");
        booking.setPaymentStatus("Chưa thanh toán");

        booking.setUserId(user.getId());
        booking.setFullName(user.getFullName());
        booking.setEmail(user.getEmail());
        booking.setPhone(user.getPhone());

        booking.setTotalPrice(totalPrice);

        bookingRepository.save(booking);
    }

    @Override
    public List<BookingResponse> getBookings(User user) {
        List<Booking> bookings = bookingRepository.findByUserId(user.getId());
        return bookings.stream().map(bookingMapper::toResponse).collect(Collectors.toList());
    }
}
