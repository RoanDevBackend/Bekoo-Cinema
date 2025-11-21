package org.bekoocinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.constant.EndPointConstant;
import org.bekoocinema.request.cinema.CreateCinemaRequest;
import org.bekoocinema.request.cinema.UpdateCinemaRequest;
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
            @Parameter(name = "keyWord", description = "Tên rạp"),
            @Parameter(name = "province", description = "Lọc theo tỉnh/thành phố")
    })
    @GetMapping(EndPointConstant.PUBLIC + "/cinema/key")
    public ApiResponse getCinemaByDistrict(
            @RequestParam(required = false, defaultValue = "") String keyWord,
            @RequestParam(required = false, defaultValue = "") String province) {
        return ApiResponse.success(200, "Tìm thành công", cinemaService.getCinema(keyWord, province));
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
            summary = "Api dùng để cập nhật thông tin rạp",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @PutMapping("/cinema/{id}")
    public ApiResponse updateCinema(
            @PathVariable String id,
            @ModelAttribute @Valid UpdateCinemaRequest updateCinemaRequest) {
        cinemaService.updateCinema(id, updateCinemaRequest);
        return ApiResponse.success(200, "Cập nhật rạp thành công");
    }

    @Operation(
            summary = "Api dùng để xóa rạp",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @DeleteMapping("/cinema/{id}")
    public ApiResponse deleteCinema(@PathVariable String id) {
        cinemaService.deleteCinema(id);
        return ApiResponse.success(200, "Xóa rạp thành công");
    }

    @Operation(
            summary = "Lấy danh sách phim chiếu theo rạp trong ngày",
            parameters = {
                    @Parameter(name = "cinemaId", description = "ID của rạp chiếu", required = true),
                    @Parameter(name = "date", description = "Ngày cần lấy phim (format: yyyy-MM-dd)", required = true)
            }
    )
    @GetMapping(EndPointConstant.PUBLIC + "/cinema/{cinemaId}/movies/by-date/{date}")
    public ApiResponse getMoviesByCinemaAndDate(@PathVariable String cinemaId, @PathVariable String date) {
        return ApiResponse.success(200, "Lấy phim theo rạp và ngày thành công",
                cinemaService.getMoviesByCinemaAndDate(cinemaId, date)
        );
    }


}
