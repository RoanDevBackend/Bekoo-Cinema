package org.bekoocinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

    final StatisticsService statisticsService;

    @Operation(
            summary = "Lấy thống kê tổng quan hệ thống",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @GetMapping("/statistics")
    public ApiResponse getStatistics() {
        return ApiResponse.success(200, "Lấy thống kê thành công", statisticsService.getStatistics());
    }
}
