package com.moneyroomba.service.dto.reports;

import com.moneyroomba.domain.Category;
import com.moneyroomba.domain.Currency;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.domain.enumeration.MovementType;
import java.time.LocalDate;

public class TransactionsByCategoryDTO implements Comparable<TransactionsByCategoryDTO> {

    private Double total;

    private Long count;

    private Category category;

    private MovementType movementType;

    private Currency currency;

    private double percentage;

    public TransactionsByCategoryDTO(Double total, Long count, Category category, MovementType movementType, Currency currency) {
        this.total = total;
        this.count = count;
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

    public Long getCounter() {
        return count;
    }

    public void setCounter(Long count) {
        this.count = count;
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

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public int compareTo(TransactionsByCategoryDTO o) {
        if (getTotal() == null || o.getTotal() == null) {
            return 0;
        } else {
            return getTotal().compareTo(o.getTotal());
        }
    }
}
