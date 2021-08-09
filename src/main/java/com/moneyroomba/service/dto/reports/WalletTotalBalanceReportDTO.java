package com.moneyroomba.service.dto.reports;

import com.moneyroomba.domain.Currency;

public class WalletTotalBalanceReportDTO {

    private Double total;

    private Currency currency;

    public WalletTotalBalanceReportDTO(Double total, Currency currency) {
        this.total = total;
        this.currency = currency;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
