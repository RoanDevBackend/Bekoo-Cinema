package org.bekoocinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.constant.EndPointConstant;
import org.bekoocinema.request.cinema.CreateCinemaRequest;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.service.CinemaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CinemaController {

    final CinemaService cinemaService;

    @Operation(summary = "Api dùng để tạo phim", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @PostMapping("/cinema")
    public ApiResponse createCinema(@ModelAttribute @Valid CreateCinemaRequest createCinemaRequest) {
        cinemaService.addCinema(createCinemaRequest);
        return ApiResponse.success(201, "Thêm thành công");
    }

    @Operation(summary = "Tìm kiếm rạp phim", parameters = {
            @Parameter(name = "keyWord", description = "Tên rạp hoặc địa chỉ rạp theo tỉnh")
    })
    @GetMapping(EndPointConstant.PUBLIC + "/cinema/key")
    public ApiResponse getCinemaByDistrict(@RequestParam(required = false, defaultValue = "") String keyWord) {
        return ApiResponse.success(200, "Tìm thành công", cinemaService.getCinema(keyWord));
    }

    @Operation(summary = "Lấy chi tiết rạp chiếu")
    @GetMapping(EndPointConstant.PUBLIC + "/cinema/{id}")
    public ApiResponse getCinemaById(@PathVariable String id){
        return ApiResponse.success(200, "Lấy rạp thành công", cinemaService.getCinemaById(id));
    }

    @Operation(summary = "Lấy danh sách tất cả rạp chiếu")
    @GetMapping(EndPointConstant.PUBLIC + "/cinema/all")
    public ApiResponse getAllCinemas(
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return ApiResponse.success(200, "Lấy danh sách rạp thành công", cinemaService.getAllCinemas(pageIndex, pageSize));
    }

    @Operation(
            summary = "Lấy danh sách phim chiếu theo rạp (phân theo ngày trong 1 tuần)",
            parameters = {
                    @Parameter(name = "cinemaId", description = "ID của rạp chiếu", required = true),
                    @Parameter(name = "startDate",
                            description = "Ngày bắt đầu (format: yyyy-MM-dd), mặc định là hôm nay"),
            }
    )
    @GetMapping(EndPointConstant.PUBLIC + "/cinema/{cinemaId}/movies")
    public ApiResponse getMovieSchedule(@PathVariable String cinemaId, @RequestParam(required = false) String startDate) {
        return ApiResponse.success(200, "Lấy lịch chiếu phim theo rạp thành công",
                cinemaService.getMovieSchedule(cinemaId, startDate)
        );
    }


}
