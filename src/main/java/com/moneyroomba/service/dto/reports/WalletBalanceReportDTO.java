package com.moneyroomba.service.dto.reports;

import com.moneyroomba.domain.Currency;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.domain.enumeration.MovementType;
import java.time.LocalDate;

public class WalletBalanceReportDTO {

    private Double total;

    private MovementType movementType;

    private Wallet wallet;

    private Currency currency;

    public WalletBalanceReportDTO() {}

    public WalletBalanceReportDTO(Double total, MovementType movementType, Wallet wallet, Currency currency) {
        this.total = total;
        this.movementType = movementType;
        this.wallet = wallet;
        this.currency = currency;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
