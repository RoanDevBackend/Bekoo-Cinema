package org.bekoocinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
}
