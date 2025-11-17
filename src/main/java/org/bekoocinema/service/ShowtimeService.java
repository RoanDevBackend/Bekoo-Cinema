package org.bekoocinema.service;

import org.bekoocinema.exception.AppException;
import org.bekoocinema.request.room.CreateShowtimeRequest;
import org.bekoocinema.response.showtime.ShowtimeDetailResponse;
import org.bekoocinema.response.showtime.ShowtimeResponse;

import java.util.List;
import java.util.Map;

public interface ShowtimeService {
    void newShowTime(CreateShowtimeRequest createRequest) throws AppException;
    void resetSeat(String showtimeId);
    List<ShowtimeResponse> getShowtime(String movieId);
    Object getShowtimeSchedule(String cinemaId, String date, int days);

}
