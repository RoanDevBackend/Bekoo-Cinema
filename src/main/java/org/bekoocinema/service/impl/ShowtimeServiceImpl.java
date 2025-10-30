package org.bekoocinema.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bekoocinema.entity.*;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.exception.ErrorDetail;
import org.bekoocinema.mapper.RoomMapper;
import org.bekoocinema.mapper.SeatMapper;
import org.bekoocinema.repository.CinemaRepository;
import org.bekoocinema.repository.MovieRepository;
import org.bekoocinema.repository.RoomRepository;
import org.bekoocinema.repository.ShowtimeRepository;
import org.bekoocinema.request.room.CreateShowtimeRequest;
import org.bekoocinema.response.room.RoomResponse;
import org.bekoocinema.response.room.SeatResponse;
import org.bekoocinema.response.showtime.ShowtimeDetailResponse;
import org.bekoocinema.response.showtime.ShowtimeResponse;
import org.bekoocinema.service.ShowtimeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {
    final ShowtimeRepository showtimeRepository;
    final MovieRepository movieRepository;
    final RoomRepository roomRepository;
    final CinemaRepository cinemaRepository;
    final RoomMapper roomMapper;
    final SeatMapper seatMapper;

    @Override
    @SneakyThrows
    public void newShowTime(CreateShowtimeRequest createRequest) {
        Room room = roomRepository.findById(createRequest.getRoomId()).orElseThrow(
                () -> new AppException(ErrorDetail.ERR_ROOM_NOT_EXISTED)
        );
        Movie movie = movieRepository.findById(createRequest.getMovieId()).orElseThrow(
                () -> new AppException(ErrorDetail.ERR_MOVIE_NOT_EXISTED)
        );
        Showtime showtime = new Showtime();
        showtime.setRoom(room);
        showtime.setMovie(movie);
        showtime.setStartTime(LocalDateTime.parse(createRequest.getStartTime()));
        showtime.setEndTime(LocalDateTime.parse(createRequest.getEndTime()));
        showtimeRepository.save(showtime);
    }

    @Override
    @Transactional
    public List<ShowtimeResponse> getShowtime(String movieId) {
        List<Cinema> allCinema = cinemaRepository.findAll();
        List<ShowtimeResponse> showtimeResponses = new ArrayList<>();
        for(Cinema cinema : allCinema){
            List<Showtime> showtimes = showtimeRepository.getShowtime(movieId, cinema.getId());

            ShowtimeResponse showtimeResponse = new ShowtimeResponse();
            showtimeResponse.setCinemaId(cinema.getId());
            showtimeResponse.setCinemaName(cinema.getName());
            showtimeResponse.setProvince(cinema.getProvince());
            showtimeResponse.setDistrict(cinema.getDistrict());
            showtimeResponse.setCommune(cinema.getCommune());
            showtimeResponse.setDetailAddress(cinema.getDetailAddress());
            List<ShowtimeDetailResponse> showtimeDetailResponses = new ArrayList<>();
            for(Showtime showtimeItem : showtimes){
                ShowtimeDetailResponse showtimeDetailResponse = new ShowtimeDetailResponse();
                showtimeDetailResponse.setId(showtimeItem.getId());
                String date = showtimeItem.getStartTime().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
                showtimeDetailResponse.setDate(date);
                String startTime = showtimeItem.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                String endTime = showtimeItem.getEndTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                showtimeDetailResponse.setTimeline(startTime + "-" + endTime);
                RoomResponse roomResponse = roomMapper.toResponse(showtimeItem.getRoom());
                List<SeatResponse> seatResponses = new ArrayList<>();
                for(Seat seat : showtimeItem.getRoom().getSeats()) {
                    SeatResponse seatResponse = seatMapper.toResponse(seat);
                    seatResponses.add(seatResponse);
                }
                roomResponse.setSeats(seatResponses);
                showtimeDetailResponse.setRoomResponse(roomResponse);
                showtimeDetailResponses.add(showtimeDetailResponse);
            }
            showtimeResponse.setShowtimeDetails(showtimeDetailResponses);
            showtimeResponses.add(showtimeResponse);
        }
        return showtimeResponses;
    }
}
