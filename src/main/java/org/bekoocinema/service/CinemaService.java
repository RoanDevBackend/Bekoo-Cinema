package org.bekoocinema.service;

import org.bekoocinema.request.cinema.CreateCinemaRequest;
import org.bekoocinema.request.cinema.UpdateCinemaRequest;
import org.bekoocinema.response.PageResponse;
import org.bekoocinema.response.cinema.CinemaMovieScheduleResponse;
import org.bekoocinema.response.cinema.CinemaResponse;

import java.util.List;

public interface CinemaService {
    void addCinema(CreateCinemaRequest createCinemaRequest);
    List<CinemaResponse> getCinema(String keyWord, String province);
    CinemaResponse getCinemaById(String id);
    PageResponse<?> getAllCinemas(int pageIndex, int pageSize);
    void updateCinema(String id, UpdateCinemaRequest updateCinemaRequest);
    void deleteCinema(String id);
    CinemaMovieScheduleResponse getMovieSchedule(String cinemaId, String startDate);
}
