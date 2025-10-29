package org.bekoocinema.service;

import org.bekoocinema.request.room.CreateShowtimeRequest;
import org.bekoocinema.response.showtime.ShowtimeResponse;

import java.util.List;
import java.util.Map;

public interface ShowtimeService {
    void newShowTime(CreateShowtimeRequest createRequest);
    List<ShowtimeResponse> getShowtime(String movieId);
}
