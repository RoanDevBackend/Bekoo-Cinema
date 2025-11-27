package org.bekoocinema.service;

import org.bekoocinema.response.report.ReportChartTemplateResponse;
import org.bekoocinema.response.report.TotalReportResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ReportService {
    List<ReportChartTemplateResponse> getChartByPrice(LocalDate from, LocalDate to, int groupType);
    public List<ReportChartTemplateResponse> getChartByBooking(LocalDate from, LocalDate to, int groupType);
    TotalReportResponse getTotalReportResponse();
}
