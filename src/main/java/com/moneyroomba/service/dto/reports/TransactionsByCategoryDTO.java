package com.moneyroomba.service.dto.reports;

import com.moneyroomba.domain.Category;
import com.moneyroomba.domain.Currency;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.domain.enumeration.MovementType;
import java.time.LocalDate;

public class TransactionsByCategoryDTO {

    private Double total;

    private Category category;

    private MovementType movementType;

    private Currency currency;

    public TransactionsByCategoryDTO(Double total, Category category, MovementType movementType, Currency currency) {
        this.total = total;
        this.category = category;
        this.movementType = movementType;
        this.currency = currency;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
