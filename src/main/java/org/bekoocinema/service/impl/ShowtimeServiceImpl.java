package org.bekoocinema.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bekoocinema.entity.*;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.exception.ErrorDetail;
import org.bekoocinema.mapper.RoomMapper;
import org.bekoocinema.mapper.SeatMapper;
import org.bekoocinema.repository.*;
import org.bekoocinema.request.room.CreateShowtimeRequest;
import org.bekoocinema.request.room.UpdateShowtimeRequest;
import org.bekoocinema.response.room.RoomResponse;
import org.bekoocinema.response.room.SeatResponse;
import org.bekoocinema.response.showtime.*;
import org.bekoocinema.service.ShowtimeService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {
    final ShowtimeRepository showtimeRepository;
    final MovieRepository movieRepository;
    final RoomRepository roomRepository;
    final CinemaRepository cinemaRepository;
    final BookingRepository bookingRepository;
    final RoomMapper roomMapper;
    final SeatMapper seatMapper;
    private final SeatRepository seatRepository;

    @Override
    public void newShowTime(CreateShowtimeRequest createRequest) throws AppException {
        Room room = roomRepository.findById(createRequest.getRoomId()).orElseThrow(
                () -> new AppException(ErrorDetail.ERR_ROOM_NOT_EXISTED)
        );
        Movie movie = movieRepository.findById(createRequest.getMovieId()).orElseThrow(
                () -> new AppException(ErrorDetail.ERR_MOVIE_NOT_EXISTED)
        );
        
        LocalDateTime startTime = LocalDateTime.parse(createRequest.getStartTime());
        LocalDateTime endTime = LocalDateTime.parse(createRequest.getEndTime());
        
        boolean hasConflict = showtimeRepository.existsConflictingShowtime(
                createRequest.getRoomId(),
                startTime,
                endTime
        );
        
        if (hasConflict) {
            throw new AppException(ErrorDetail.ERR_SHOWTIME_CONFLICT);
        }
        Showtime showtime = new Showtime();
        showtime.setRoom(room);
        showtime.setMovie(movie);
        showtime.setStartTime(startTime);
        showtime.setEndTime(endTime);
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

                List<String> tempSeatIdBooked = bookingRepository.getSeatBooked(showtimeItem.getId());
                List<String> seatIdBooked = tempSeatIdBooked.stream()
                        .flatMap(s -> Arrays.stream(s.split(",")))
                        .map(String::trim)
                        .filter(id -> !id.isEmpty())
                        .toList();

                ShowtimeDetailResponse showtimeDetailResponse = new ShowtimeDetailResponse();
                showtimeDetailResponse.setId(showtimeItem.getId());
                String date = showtimeItem.getStartTime().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
                showtimeDetailResponse.setDate(date);
                String startTime = showtimeItem.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                String endTime = showtimeItem.getEndTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                showtimeDetailResponse.setStartTime(startTime);
                showtimeDetailResponse.setEndTime(endTime);
                RoomResponse roomResponse = roomMapper.toResponse(showtimeItem.getRoom());
                List<SeatResponse> seatResponses = new ArrayList<>();
                for(Seat seat : showtimeItem.getRoom().getSeats()) {
                    SeatResponse seatResponse = seatMapper.toResponse(seat);
                    seatResponse.setBooked(seatIdBooked.contains(seat.getId()));
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

    @Override
    @Transactional
    public Object getShowtimeSchedule(String cinemaId, String date, int days) {
        if(days < 1 || days > 7) {
            throw new IllegalArgumentException("Số ngày phải từ 1 đến 7");
        }

        LocalDate startDate = date != null && !date.isBlank() ? LocalDate.parse(date) : LocalDate.now();

        LocalDate endDate = startDate.plusDays(days);

        List<Cinema> cinemas;
        if(cinemaId != null && !cinemaId.isBlank()) {
            Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new RuntimeException("Rạp chiếu không tồn tại"));
            cinemas = Collections.singletonList(cinema);
        }else{
            cinemas = cinemaRepository.findAll();
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        Map<String, String> dayOfWeekMap = Map.of(
            "MONDAY",
            "Thứ 2",
            "TUESDAY",
            "Thứ 3",
            "WEDNESDAY",
            "Thứ 4",
            "THURSDAY",
            "Thứ 5",
            "FRIDAY",
            "Thứ 6",
            "SATURDAY",
            "Thứ 7",
            "SUNDAY",
            "Chủ nhật"
        );

        List<CinemaScheduleMainResponse> cinemaSchedules = new ArrayList<>();

        for(Cinema cinema : cinemas) {
            List<DateMovieScheduleResponse> dateSchedules = new ArrayList<>();

            for(LocalDate currentDate = startDate; currentDate.isBefore(endDate); currentDate = currentDate.plusDays(1)){
                LocalDateTime dayStart = currentDate.atStartOfDay();
                LocalDateTime dayEnd = currentDate.atTime(23, 59, 59);

                List<Showtime> showtimes = showtimeRepository.findByCinemaAndDateRange(cinema.getId(), dayStart, dayEnd);

                if(showtimes.isEmpty()) continue;


                Map<String, List<Showtime>> showtimesByMovie = showtimes.stream()
                    .collect(Collectors.groupingBy(st -> st.getMovie().getId()));

                List<MovieScheduleResponse> movieSchedules = new ArrayList<>();

                for(Map.Entry<String, List<Showtime>> entry : showtimesByMovie.entrySet()) {
                    List<Showtime> movieShowtimes = entry.getValue();
                    if(movieShowtimes.isEmpty()) continue;

                    Movie movie = movieShowtimes.get(0).getMovie();

                    List<ShowtimeItemResponse> showtimeItems = movieShowtimes
                        .stream()
                        .sorted(Comparator.comparing(Showtime::getStartTime))
                        .map(st -> {
                            List<String> tempSeatIdBooked = bookingRepository.getSeatBooked(st.getId());
                            List<String> seatIdBooked = tempSeatIdBooked.stream()
                                    .flatMap(s -> Arrays.stream(s.split(",")))
                                    .map(String::trim)
                                    .filter(id -> !id.isEmpty())
                                    .toList();
                            int totalSeats = st.getRoom().getSeats().size();
                            int availableSeats = (int) st
                                .getRoom()
                                .getSeats()
                                .stream()
                                .filter(seat -> !seatIdBooked.contains(seat.getId()))
                                .count();

                            return ShowtimeItemResponse.builder()
                                .showtimeId(st.getId())
                                .startTime(st.getStartTime()
                                        .toLocalTime()
                                        .format(timeFormatter)
                                )
                                .endTime(st.getEndTime()
                                        .toLocalTime()
                                        .format(timeFormatter)
                                )
                                .roomId(st.getRoom().getId())
                                .roomName(st.getRoom().getName())
                                .availableSeats(availableSeats)
                                .totalSeats(totalSeats)
                                .build();
                        })
                        .collect(Collectors.toList());

                    List<String> genres = movie
                            .getGenres()
                        .stream()
                        .map(Genre::getName)
                        .collect(Collectors.toList());

                    MovieScheduleResponse movieSchedule = MovieScheduleResponse.builder()
                            .movieId(movie.getId())
                            .movieName(movie.getName())
                            .posterUrl(movie.getPosterUrl())
                            .duration(movie.getDuration())
                            .ageRating(movie.getNote())
                            .genres(genres)
                            .showtimes(showtimeItems)
                            .build();

                    movieSchedules.add(movieSchedule);
                }

                if(!movieSchedules.isEmpty()) {
                    movieSchedules.sort(Comparator.comparing(MovieScheduleResponse::getMovieName)
                    );

                    DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
                    DateMovieScheduleResponse dateSchedule = DateMovieScheduleResponse.builder()
                            .date(currentDate.format(dateFormatter))
                            .dayOfWeek(dayOfWeekMap.get(dayOfWeek.toString()))
                            .movies(movieSchedules)
                            .build();

                    dateSchedules.add(dateSchedule);
                }
            }

            if(!dateSchedules.isEmpty()) {
                CinemaScheduleMainResponse cinemaSchedule = CinemaScheduleMainResponse.builder()
                        .cinemaId(cinema.getId())
                        .cinemaName(cinema.getName())
                        .address(cinema.getAddress())
                        .province(cinema.getProvince())
                        .district(cinema.getDistrict())
                        .dateSchedules(dateSchedules)
                        .build();

                cinemaSchedules.add(cinemaSchedule);
            }
        }

        return cinemaSchedules;
    }

    @Override
    @Transactional
    public void updateShowTime(String showtimeId, UpdateShowtimeRequest updateRequest) throws AppException {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new AppException(ErrorDetail.ERR_SHOWTIME_NOT_EXISTED));
        
        Room room = roomRepository.findById(updateRequest.getRoomId())
                .orElseThrow(() -> new AppException(ErrorDetail.ERR_ROOM_NOT_EXISTED));
        
        Movie movie = movieRepository.findById(updateRequest.getMovieId())
                .orElseThrow(() -> new AppException(ErrorDetail.ERR_MOVIE_NOT_EXISTED));
        
        LocalDateTime startTime = LocalDateTime.parse(updateRequest.getStartTime());
        LocalDateTime endTime = LocalDateTime.parse(updateRequest.getEndTime());
        
        boolean hasConflict = showtimeRepository.existsConflictingShowtimeExcludingId(
                updateRequest.getRoomId(),
                startTime,
                endTime,
                showtimeId
        );
        
        if(hasConflict) {
            throw new AppException(ErrorDetail.ERR_SHOWTIME_CONFLICT);
        }
        
        showtime.setRoom(room);
        showtime.setMovie(movie);
        showtime.setStartTime(startTime);
        showtime.setEndTime(endTime);
        showtimeRepository.save(showtime);
    }

    @Override
    @Transactional
    public void deleteShowTime(String showtimeId) throws AppException {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new AppException(ErrorDetail.ERR_SHOWTIME_NOT_EXISTED));
        
        boolean hasBookings = bookingRepository.existsByShowtimeId(showtimeId);
        if(hasBookings) {
            throw new AppException(ErrorDetail.ERR_SHOWTIME_HAS_BOOKINGS);
        }
        
        showtimeRepository.delete(showtime);
    }


}
