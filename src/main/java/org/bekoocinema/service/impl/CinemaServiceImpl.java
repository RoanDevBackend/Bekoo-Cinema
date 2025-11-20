package org.bekoocinema.service.impl;

import com.cloudinary.Cloudinary;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bekoocinema.entity.Cinema;
import org.bekoocinema.entity.Genre;
import org.bekoocinema.entity.Image;
import org.bekoocinema.entity.Room;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.exception.ErrorDetail;
import org.bekoocinema.entity.Showtime;
import org.bekoocinema.mapper.CinemaMapper;
import org.bekoocinema.repository.CinemaRepository;
import org.bekoocinema.repository.ImageRepository;
import org.bekoocinema.repository.RoomRepository;
import org.bekoocinema.repository.ShowtimeRepository;
import org.bekoocinema.request.cinema.CreateCinemaRequest;
import org.bekoocinema.request.cinema.UpdateCinemaRequest;
import org.bekoocinema.response.PageResponse;
import org.bekoocinema.response.cinema.*;
import org.bekoocinema.service.CinemaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CinemaServiceImpl implements CinemaService {

    final Cloudinary cloudinary;
    final CinemaRepository cinemaRepository;
    final ImageRepository imageRepository;
    final CinemaMapper cinemaMapper;
    final RoomRepository roomRepository;
    final ShowtimeRepository showtimeRepository;

    @Override
    @SneakyThrows
    public void addCinema(CreateCinemaRequest createCinemaRequest) {
        Cinema cinema = cinemaMapper.toCinema(createCinemaRequest);
        cinemaRepository.save(cinema);
        List<Image> imageCinemas = new ArrayList<>();
        for(MultipartFile file: createCinemaRequest.getFiles()) {
            String urlCinemaImage = this.getFileUrl(file);
            Image image = new Image();
            image.setImageType("cinema");
            image.setUrl(urlCinemaImage);
            image.setTargetId(cinema.getId());
            imageCinemas.add(image);
        }
        imageRepository.saveAll(imageCinemas);
    }

    @Override
    public List<CinemaResponse> getCinema(String keyWord, String province) {
        List<Cinema> cinemas = cinemaRepository.getAllByKey(keyWord, province);
        List<CinemaResponse> cinemaResponses = new ArrayList<>();
        for (Cinema cinema : cinemas) {
            CinemaResponse cinemaResponse = cinemaMapper.toCinemaResponse(cinema);
            List<String> urlsCinema = imageRepository.getUrlByTargetId("cinema", cinema.getId());
            cinemaResponse.setUrlImages(urlsCinema);
            cinemaResponses.add(cinemaResponse);
        }
        return cinemaResponses;
    }

    @Override
    public CinemaResponse getCinemaById(String id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinema not found"));
        
        CinemaResponse cinemaResponse = cinemaMapper.toCinemaResponse(cinema);
        List<String> urlsCinema = imageRepository.getUrlByTargetId("cinema", cinema.getId());
        cinemaResponse.setUrlImages(urlsCinema);
        
        return cinemaResponse;
    }

    @Override
    public PageResponse<?> getAllCinemas(int pageIndex, int pageSize) {
        this.validPage(pageIndex, pageSize);
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Cinema> cinemaPage = cinemaRepository.findAllCinemas(pageable);
        
        List<CinemaResponse> responses = new ArrayList<>();
        for (Cinema cinema : cinemaPage.getContent()) {
            CinemaResponse cinemaResponse = cinemaMapper.toCinemaResponse(cinema);
            List<String> urlsCinema = imageRepository.getUrlByTargetId("cinema", cinema.getId());
            cinemaResponse.setUrlImages(urlsCinema);
            responses.add(cinemaResponse);
        }
        
        return PageResponse.<CinemaResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .sortBy(new PageResponse.SortBy("name", "asc"))
                .content(responses)
                .totalElements(cinemaPage.getTotalElements())
                .totalPages(cinemaPage.getTotalPages())
                .build();
    }

    private void validPage(int pageIndex, int pageSize){
        if(pageIndex < 1 || pageSize < 1){
            throw new IllegalArgumentException("Số trang và số phần tử trong một trang cần lớn hơn 1");
        }
    }

    @Override
    @SneakyThrows
    @Transactional
    public void updateCinema(String id, UpdateCinemaRequest updateCinemaRequest) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorDetail.ERR_CINEMA_NOT_EXISTED));

        if(updateCinemaRequest.getName() != null && !updateCinemaRequest.getName().isBlank()) {
            cinema.setName(updateCinemaRequest.getName());
        }
        if(updateCinemaRequest.getProvince() != null && !updateCinemaRequest.getProvince().isBlank()) {
            cinema.setProvince(updateCinemaRequest.getProvince());
        }
        if(updateCinemaRequest.getDistrict() != null) {
            cinema.setDistrict(updateCinemaRequest.getDistrict());
        }
        if(updateCinemaRequest.getCommune() != null && !updateCinemaRequest.getCommune().isBlank()) {
            cinema.setCommune(updateCinemaRequest.getCommune());
        }
        if(updateCinemaRequest.getDetailAddress() != null) {
            cinema.setDetailAddress(updateCinemaRequest.getDetailAddress());
        }
        if(updateCinemaRequest.getPhone() != null && !updateCinemaRequest.getPhone().isBlank()) {
            cinema.setPhone(updateCinemaRequest.getPhone());
        }
        if(updateCinemaRequest.getDescription() != null) {
            cinema.setDescription(updateCinemaRequest.getDescription());
        }
        if(updateCinemaRequest.getStatus() != null) {
            cinema.setStatus(updateCinemaRequest.getStatus());
        }

        cinemaRepository.save(cinema);

        if(updateCinemaRequest.getFiles() != null && !updateCinemaRequest.getFiles().isEmpty()) {
            Boolean replaceImages = updateCinemaRequest.getReplaceImages();

            if(replaceImages != null && replaceImages) {
                List<Image> oldImages = imageRepository.findAll()
                        .stream()
                        .filter(img -> "cinema".equals(img.getImageType()) && id.equals(img.getTargetId()))
                        .toList();
                if(!oldImages.isEmpty()) {
                    imageRepository.deleteAll(oldImages);
                }
            }

            List<Image> imageCinemas = new ArrayList<>();
            for(MultipartFile file : updateCinemaRequest.getFiles()) {
                String urlCinemaImage = this.getFileUrl(file);
                Image image = new Image();
                image.setImageType("cinema");
                image.setUrl(urlCinemaImage);
                image.setTargetId(cinema.getId());
                imageCinemas.add(image);
            }
            imageRepository.saveAll(imageCinemas);
        }
    }

    @Override
    @SneakyThrows
    @Transactional
    public void deleteCinema(String id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorDetail.ERR_CINEMA_NOT_EXISTED));

        List<Room> rooms = roomRepository.getRoomByCinema(id);
        if(!rooms.isEmpty()) {
            throw new RuntimeException("Không thể xóa rạp vì còn phòng chiếu " + rooms.size() + " đang hoạt động");
        }

        List<Image> images = imageRepository.findAll()
                .stream()
                .filter(img -> "cinema".equals(img.getImageType()) && id.equals(img.getTargetId()))
                .toList();
        if (!images.isEmpty()) {
            imageRepository.deleteAll(images);
        }

        cinemaRepository.delete(cinema);
    }


    @Override
    @Transactional(readOnly = true)
    public CinemaMovieScheduleResponse getMovieSchedule(String cinemaId, String startDate) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
            .orElseThrow(() -> new RuntimeException("Rạp chiếu không tồn tại"));

        LocalDate start = startDate != null && !startDate.isBlank()
            ? LocalDate.parse(startDate)
            : LocalDate.now();
        LocalDate end = start.plusDays(7);

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        List<Showtime> showtimes = showtimeRepository.findByCinemaAndDateRange(cinemaId, startDateTime, endDateTime);

        Map<LocalDate, List<Showtime>> showtimesByDate = showtimes
            .stream()
            .collect(Collectors.groupingBy(st -> st.getStartTime().toLocalDate()));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        List<CinemaDateScheduleResponse> dates = new ArrayList<>();

        for(LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            List<Showtime> dayShowtimes = showtimesByDate.getOrDefault(date, new ArrayList<>());

            if (dayShowtimes.isEmpty()) {
                continue;
            }

            Map<String, List<Showtime>> showtimesByMovie = dayShowtimes
                .stream()
                .collect(Collectors.groupingBy(st -> st.getMovie().getId()));

            List<CinemaMovieItemResponse> movies = new ArrayList<>();

            for(Map.Entry<String, List<Showtime>> entry : showtimesByMovie.entrySet()) {
                List<Showtime> movieShowtimes = entry.getValue();
                if (movieShowtimes.isEmpty()) continue;

                Showtime firstShowtime = movieShowtimes.get(0);

                CinemaMovieItemResponse movieResponse = new CinemaMovieItemResponse();
                movieResponse.setMovieId(firstShowtime.getMovie().getId());
                movieResponse.setMovieName(firstShowtime.getMovie().getName());
                movieResponse.setPosterUrl(firstShowtime.getMovie().getPosterUrl());
                movieResponse.setDuration(firstShowtime.getMovie().getDuration());

                List<String> genres = firstShowtime
                    .getMovie()
                    .getGenres()
                    .stream()
                    .map(Genre::getName)
                    .collect(Collectors.toList());
                movieResponse.setGenres(genres);

                List<CinemaShowtimeItemResponse> showtimeItems = movieShowtimes
                    .stream()
                    .sorted(Comparator.comparing(Showtime::getStartTime))
                    .map(st -> {
                        CinemaShowtimeItemResponse item = new CinemaShowtimeItemResponse();
                        item.setShowtimeId(st.getId());
                        item.setStartTime(st
                                .getStartTime()
                                .toLocalTime()
                                .format(timeFormatter));
                        item.setEndTime(st.getEndTime().toLocalTime().format(timeFormatter));
                        item.setRoomName(st.getRoom().getName());
                        return item;
                    })
                    .collect(Collectors.toList());

                movieResponse.setShowtimes(showtimeItems);
                movies.add(movieResponse);
            }

            movies.sort(Comparator.comparing(CinemaMovieItemResponse::getMovieName));

            CinemaDateScheduleResponse dateSchedule = new CinemaDateScheduleResponse();
            dateSchedule.setDate(date.format(dateFormatter));
            dateSchedule.setMovies(movies);
            dates.add(dateSchedule);
        }

        CinemaMovieScheduleResponse response = new CinemaMovieScheduleResponse();
        response.setCinemaId(cinema.getId());
        response.setCinemaName(cinema.getName());
        response.setAddress(cinema.getAddress());
        response.setDates(dates);

        return response;
    }

    private String getFileUrl(MultipartFile file) throws IOException {
        try {
            if(file == null || file.isEmpty()){
                return null;
            }
            var response = cloudinary.uploader().upload(file.getBytes(), Map.of());
            return response.get("url").toString();
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IOException("File không hợp lệ");
        }
    }
}
