package com.moneyroomba.service.dto.reports;

import com.moneyroomba.domain.enumeration.MovementType;

public class TransactionCountReportDTO {

    Long count;

    MovementType movementType;

    public TransactionCountReportDTO(Long count, MovementType movementType) {
        this.count = count;
        this.movementType = movementType;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }
}
