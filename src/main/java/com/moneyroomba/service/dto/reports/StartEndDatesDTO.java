package com.moneyroomba.service.dto.reports;

import java.time.LocalDate;

public class StartEndDatesDTO {

    private LocalDate startDate;

    private LocalDate endDate;

    public StartEndDatesDTO(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
