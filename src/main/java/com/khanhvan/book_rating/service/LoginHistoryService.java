package com.khanhvan.book_rating.service;

import com.khanhvan.book_rating.DTO.response.DailyAccessChart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoginHistoryService
{
    List<DailyAccessChart> displayDailyAccessChartInCurrentWeek();
}
