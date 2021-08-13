package com.moneyroomba.service.dto.reports;

public class ImportedTransactionCountReportDTO {

    int count;

    public ImportedTransactionCountReportDTO(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
