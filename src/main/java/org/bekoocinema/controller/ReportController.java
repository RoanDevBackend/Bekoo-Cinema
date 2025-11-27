package org.bekoocinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    final ReportService reportService;

    @GetMapping("/price")
    @Operation(summary = "Báo cáo thống kê doanh thu đặt vé xem phim", parameters = {
            @Parameter(name = "groupType", description = "1-Theo ngày, 2-Theo tuần, 3-Theo tháng, 4-Theo năm")
    })
    public ApiResponse totalPrice(@RequestParam String fromDate,
                                  @RequestParam String toDate,
                                  @RequestParam int groupType) {
        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);
        var response = reportService.getChartByPrice(from, to, groupType);
        return ApiResponse.success(200, "OK", response);
    }

    @GetMapping("/total")
    @Operation(summary = "Báo cáo thống kê doanh thu đặt vé xem phim", parameters = {
            @Parameter(name = "groupType", description = "1-Theo ngày, 2-Theo tuần, 3-Theo tháng, 4-Theo năm")
    })
    public ApiResponse totalAll() {
        var response = reportService.getTotalReportResponse();
        return ApiResponse.success(200, "OK", response);
    }

}
