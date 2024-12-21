package com.buihuuduy.book_rating.controller;

import com.buihuuduy.book_rating.DTO.ApiResponse;
import com.buihuuduy.book_rating.DTO.response.DailyAccessChart;
import com.buihuuduy.book_rating.service.LoginHistoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/login-history")
public class LoginHistoryController
{
    private final LoginHistoryService loginHistoryService;

    public LoginHistoryController(LoginHistoryService loginHistoryService) {
        this.loginHistoryService = loginHistoryService;
    }

    @GetMapping("/chart")
    public ApiResponse<List<DailyAccessChart>> getDailyAccessCountsInCurrentWeek() {
        List<DailyAccessChart> dailyAccessCounts = loginHistoryService.displayDailyAccessChartInCurrentWeek();
        ApiResponse<List<DailyAccessChart>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(dailyAccessCounts);
        apiResponse.setCode(200);
        return apiResponse;
    }
}
