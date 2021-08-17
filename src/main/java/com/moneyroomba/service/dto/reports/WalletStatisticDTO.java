package com.moneyroomba.service.dto.reports;

import com.moneyroomba.domain.Currency;
import com.moneyroomba.domain.Wallet;

public class WalletStatisticDTO {

    private Double balance;

    private String name;

    private Currency currency;

    private double percentage;

    public WalletStatisticDTO(Double balance, String name, Currency currency) {
        this.balance = balance;
        this.name = name;
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String wallet) {
        this.name = wallet;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
