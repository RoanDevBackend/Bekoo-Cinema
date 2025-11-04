package org.bekoocinema.service;

import org.bekoocinema.request.cinema.CreateCinemaRequest;
import org.bekoocinema.response.cinema.CinemaResponse;

import java.util.List;

public interface CinemaService {
    void addCinema(CreateCinemaRequest createCinemaRequest);
    List<CinemaResponse> getCinema(String keyWord);
    CinemaResponse getCinemaById(String id);
    List<CinemaResponse> getAllCinemas();
}
