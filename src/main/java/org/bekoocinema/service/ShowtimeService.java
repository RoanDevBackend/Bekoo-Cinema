package org.bekoocinema.service;

import org.bekoocinema.exception.AppException;
import org.bekoocinema.request.room.CreateShowtimeRequest;
import org.bekoocinema.request.room.UpdateShowtimeRequest;
import org.bekoocinema.response.showtime.ShowtimeResponse;

import java.util.List;

public interface ShowtimeService {
    void newShowTime(CreateShowtimeRequest createRequest) throws AppException;
    void updateShowTime(String showtimeId, UpdateShowtimeRequest updateRequest) throws AppException;
    void deleteShowTime(String showtimeId) throws AppException;
    List<ShowtimeResponse> getShowtime(String movieId);
    Object getShowtimeSchedule(String cinemaId, String date, int days);

}
