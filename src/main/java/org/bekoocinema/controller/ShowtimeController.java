package org.bekoocinema.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.request.room.CreateShowtimeRequest;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.service.ShowtimeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShowtimeController {
    final ShowtimeService showtimeService;

    @PostMapping("/showtime")
    public ApiResponse newShowtime(@RequestBody @Valid CreateShowtimeRequest createShowtimeRequest) {
        showtimeService.newShowTime(createShowtimeRequest);
        return ApiResponse.success(201, "Thêm thành công");
    }

    @GetMapping("/public-api/showtime/{movieId}")
    public ApiResponse getShowtime(@PathVariable String movieId) {
        return ApiResponse.success(200, "Danh sách chiếu phim", showtimeService.getShowtime(movieId));
    }
}
