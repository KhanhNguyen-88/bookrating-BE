package com.khanhvan.book_rating.service.impl;

import com.khanhvan.book_rating.DTO.response.DailyAccessChart;
import com.khanhvan.book_rating.repository.LoginHistoryRepository;
import com.khanhvan.book_rating.service.LoginHistoryService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoginHistoryServiceImpl implements LoginHistoryService
{
    private final LoginHistoryRepository loginHistoryRepository;

    public LoginHistoryServiceImpl(LoginHistoryRepository loginHistoryRepository) {
        this.loginHistoryRepository = loginHistoryRepository;
    }

    @Override
    public List<DailyAccessChart> displayDailyAccessChartInCurrentWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(7);

        return loginHistoryRepository.countDailyAccessesInCurrentWeek(startOfWeek, endOfWeek);
    }
}
