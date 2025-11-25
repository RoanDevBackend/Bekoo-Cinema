package org.bekoocinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.constant.EndPointConstant;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.request.room.CreateShowtimeRequest;
import org.bekoocinema.request.room.UpdateShowtimeRequest;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.service.ShowtimeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShowtimeController {
    final ShowtimeService showtimeService;

    @Operation(summary = "Api dùng để tạo xuất chiếu", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @PostMapping("/showtime")
    public ApiResponse newShowtime(@RequestBody @Valid CreateShowtimeRequest createShowtimeRequest) throws AppException {
        showtimeService.newShowTime(createShowtimeRequest);
        return ApiResponse.success(201, "Thêm thành công");
    }

    @GetMapping("/public-api/showtime/{movieId}")
    public ApiResponse getShowtime(@PathVariable String movieId) {
        return ApiResponse.success(200, "Danh sách chiếu phim", showtimeService.getShowtime(movieId));
    }

    @Operation(
        summary = "Lấy lịch chiếu phim theo ngày và rạp",
        parameters = {
            @Parameter(name = "cinemaId", description = "ID của rạp chiếu"),
            @Parameter(name = "date", description = "Ngày chiếu (format: yyyy-MM-dd), mặc định là hôm nay"),
            @Parameter(name = "days", description = "Số ngày muốn lấy (1-7 ngày), mặc định là 7 ngày"),
        }
    )
    @GetMapping(EndPointConstant.PUBLIC + "/showtime/schedule")
    public ApiResponse getShowtimeSchedule(
        @RequestParam(required = false) String cinemaId,
        @RequestParam(required = false) String date,
        @RequestParam(required = false, defaultValue = "7") int days
    ){
        return ApiResponse.success(200, "Lấy lịch chiếu thành công", showtimeService.getShowtimeSchedule(cinemaId, date, days));
    }

    @Operation(
        summary = "Cập nhật xuất chiếu",
        security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @PutMapping("/showtime/{showtimeId}")
    public ApiResponse updateShowtime(
            @PathVariable String showtimeId, 
            @RequestBody @Valid UpdateShowtimeRequest updateShowtimeRequest) throws AppException {
        showtimeService.updateShowTime(showtimeId, updateShowtimeRequest);
        return ApiResponse.success(200, "Cập nhật xuất chiếu thành công");
    }

    @Operation(
        summary = "Xóa xuất chiếu",
        security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @DeleteMapping("/showtime/{showtimeId}")
    public ApiResponse deleteShowtime(@PathVariable String showtimeId) throws AppException {
        showtimeService.deleteShowTime(showtimeId);
        return ApiResponse.success(200, "Xóa xuất chiếu thành công");
    }

}
