package com.moneyroomba.service.criteria;

import com.moneyroomba.domain.enumeration.MovementType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.moneyroomba.domain.ScheduledTransaction} entity. This class is used
 * in {@link com.moneyroomba.web.rest.ScheduledTransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /scheduled-transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ScheduledTransactionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MovementType
     */
    public static class MovementTypeFilter extends Filter<MovementType> {

        public MovementTypeFilter() {}

        public MovementTypeFilter(MovementTypeFilter filter) {
            super(filter);
        }

        @Override
        public MovementTypeFilter copy() {
            return new MovementTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private DoubleFilter originalAmount;

    private MovementTypeFilter movementType;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private BooleanFilter addToReports;

    private BooleanFilter incomingTransaction;

    private LongFilter schedulePatternId;

    private LongFilter currencyId;

    public ScheduledTransactionCriteria() {}

    public ScheduledTransactionCriteria(ScheduledTransactionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.originalAmount = other.originalAmount == null ? null : other.originalAmount.copy();
        this.movementType = other.movementType == null ? null : other.movementType.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.addToReports = other.addToReports == null ? null : other.addToReports.copy();
        this.incomingTransaction = other.incomingTransaction == null ? null : other.incomingTransaction.copy();
        this.schedulePatternId = other.schedulePatternId == null ? null : other.schedulePatternId.copy();
        this.currencyId = other.currencyId == null ? null : other.currencyId.copy();
    }

    @Override
    public ScheduledTransactionCriteria copy() {
        return new ScheduledTransactionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DoubleFilter getOriginalAmount() {
        return originalAmount;
    }

    public DoubleFilter originalAmount() {
        if (originalAmount == null) {
            originalAmount = new DoubleFilter();
        }
        return originalAmount;
    }

    public void setOriginalAmount(DoubleFilter originalAmount) {
        this.originalAmount = originalAmount;
    }

    public MovementTypeFilter getMovementType() {
        return movementType;
    }

    public MovementTypeFilter movementType() {
        if (movementType == null) {
            movementType = new MovementTypeFilter();
        }
        return movementType;
    }

    public void setMovementType(MovementTypeFilter movementType) {
        this.movementType = movementType;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            startDate = new LocalDateFilter();
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            endDate = new LocalDateFilter();
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public BooleanFilter getAddToReports() {
        return addToReports;
    }

    public BooleanFilter addToReports() {
        if (addToReports == null) {
            addToReports = new BooleanFilter();
        }
        return addToReports;
    }

    public void setAddToReports(BooleanFilter addToReports) {
        this.addToReports = addToReports;
    }

    public BooleanFilter getIncomingTransaction() {
        return incomingTransaction;
    }

    public BooleanFilter incomingTransaction() {
        if (incomingTransaction == null) {
            incomingTransaction = new BooleanFilter();
        }
        return incomingTransaction;
    }

    public void setIncomingTransaction(BooleanFilter incomingTransaction) {
        this.incomingTransaction = incomingTransaction;
    }

    public LongFilter getSchedulePatternId() {
        return schedulePatternId;
    }

    public LongFilter schedulePatternId() {
        if (schedulePatternId == null) {
            schedulePatternId = new LongFilter();
        }
        return schedulePatternId;
    }

    public void setSchedulePatternId(LongFilter schedulePatternId) {
        this.schedulePatternId = schedulePatternId;
    }

    public LongFilter getCurrencyId() {
        return currencyId;
    }

    public LongFilter currencyId() {
        if (currencyId == null) {
            currencyId = new LongFilter();
        }
        return currencyId;
    }

    public void setCurrencyId(LongFilter currencyId) {
        this.currencyId = currencyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ScheduledTransactionCriteria that = (ScheduledTransactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(originalAmount, that.originalAmount) &&
            Objects.equals(movementType, that.movementType) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(addToReports, that.addToReports) &&
            Objects.equals(incomingTransaction, that.incomingTransaction) &&
            Objects.equals(schedulePatternId, that.schedulePatternId) &&
            Objects.equals(currencyId, that.currencyId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            originalAmount,
            movementType,
            startDate,
            endDate,
            addToReports,
            incomingTransaction,
            schedulePatternId,
            currencyId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduledTransactionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (originalAmount != null ? "originalAmount=" + originalAmount + ", " : "") +
            (movementType != null ? "movementType=" + movementType + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (addToReports != null ? "addToReports=" + addToReports + ", " : "") +
            (incomingTransaction != null ? "incomingTransaction=" + incomingTransaction + ", " : "") +
            (schedulePatternId != null ? "schedulePatternId=" + schedulePatternId + ", " : "") +
            (currencyId != null ? "currencyId=" + currencyId + ", " : "") +
            "}";
    }
}
