package com.moneyroomba.service.criteria;

import com.moneyroomba.domain.enumeration.MovementType;
import com.moneyroomba.domain.enumeration.RecurringType;
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

    /**
     * Class for filtering RecurringType
     */
    public static class RecurringTypeFilter extends Filter<RecurringType> {

        public RecurringTypeFilter() {}

        public RecurringTypeFilter(RecurringTypeFilter filter) {
            super(filter);
        }

        @Override
        public RecurringTypeFilter copy() {
            return new RecurringTypeFilter(this);
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

    private RecurringTypeFilter recurringType;

    private IntegerFilter separationCount;

    private IntegerFilter maxNumberOfOcurrences;

    private IntegerFilter dayOfWeek;

    private IntegerFilter weekOfMonth;

    private IntegerFilter dayOfMonth;

    private IntegerFilter monthOfYear;

    private LongFilter currencyId;

    private LongFilter sourceUserId;

    private LongFilter categoryId;

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
        this.recurringType = other.recurringType == null ? null : other.recurringType.copy();
        this.separationCount = other.separationCount == null ? null : other.separationCount.copy();
        this.maxNumberOfOcurrences = other.maxNumberOfOcurrences == null ? null : other.maxNumberOfOcurrences.copy();
        this.dayOfWeek = other.dayOfWeek == null ? null : other.dayOfWeek.copy();
        this.weekOfMonth = other.weekOfMonth == null ? null : other.weekOfMonth.copy();
        this.dayOfMonth = other.dayOfMonth == null ? null : other.dayOfMonth.copy();
        this.monthOfYear = other.monthOfYear == null ? null : other.monthOfYear.copy();
        this.currencyId = other.currencyId == null ? null : other.currencyId.copy();
        this.sourceUserId = other.sourceUserId == null ? null : other.sourceUserId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
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

    public RecurringTypeFilter getRecurringType() {
        return recurringType;
    }

    public RecurringTypeFilter recurringType() {
        if (recurringType == null) {
            recurringType = new RecurringTypeFilter();
        }
        return recurringType;
    }

    public void setRecurringType(RecurringTypeFilter recurringType) {
        this.recurringType = recurringType;
    }

    public IntegerFilter getSeparationCount() {
        return separationCount;
    }

    public IntegerFilter separationCount() {
        if (separationCount == null) {
            separationCount = new IntegerFilter();
        }
        return separationCount;
    }

    public void setSeparationCount(IntegerFilter separationCount) {
        this.separationCount = separationCount;
    }

    public IntegerFilter getMaxNumberOfOcurrences() {
        return maxNumberOfOcurrences;
    }

    public IntegerFilter maxNumberOfOcurrences() {
        if (maxNumberOfOcurrences == null) {
            maxNumberOfOcurrences = new IntegerFilter();
        }
        return maxNumberOfOcurrences;
    }

    public void setMaxNumberOfOcurrences(IntegerFilter maxNumberOfOcurrences) {
        this.maxNumberOfOcurrences = maxNumberOfOcurrences;
    }

    public IntegerFilter getDayOfWeek() {
        return dayOfWeek;
    }

    public IntegerFilter dayOfWeek() {
        if (dayOfWeek == null) {
            dayOfWeek = new IntegerFilter();
        }
        return dayOfWeek;
    }

    public void setDayOfWeek(IntegerFilter dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public IntegerFilter getWeekOfMonth() {
        return weekOfMonth;
    }

    public IntegerFilter weekOfMonth() {
        if (weekOfMonth == null) {
            weekOfMonth = new IntegerFilter();
        }
        return weekOfMonth;
    }

    public void setWeekOfMonth(IntegerFilter weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }

    public IntegerFilter getDayOfMonth() {
        return dayOfMonth;
    }

    public IntegerFilter dayOfMonth() {
        if (dayOfMonth == null) {
            dayOfMonth = new IntegerFilter();
        }
        return dayOfMonth;
    }

    public void setDayOfMonth(IntegerFilter dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public IntegerFilter getMonthOfYear() {
        return monthOfYear;
    }

    public IntegerFilter monthOfYear() {
        if (monthOfYear == null) {
            monthOfYear = new IntegerFilter();
        }
        return monthOfYear;
    }

    public void setMonthOfYear(IntegerFilter monthOfYear) {
        this.monthOfYear = monthOfYear;
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

    public LongFilter getSourceUserId() {
        return sourceUserId;
    }

    public LongFilter sourceUserId() {
        if (sourceUserId == null) {
            sourceUserId = new LongFilter();
        }
        return sourceUserId;
    }

    public void setSourceUserId(LongFilter sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
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
            Objects.equals(recurringType, that.recurringType) &&
            Objects.equals(separationCount, that.separationCount) &&
            Objects.equals(maxNumberOfOcurrences, that.maxNumberOfOcurrences) &&
            Objects.equals(dayOfWeek, that.dayOfWeek) &&
            Objects.equals(weekOfMonth, that.weekOfMonth) &&
            Objects.equals(dayOfMonth, that.dayOfMonth) &&
            Objects.equals(monthOfYear, that.monthOfYear) &&
            Objects.equals(currencyId, that.currencyId) &&
            Objects.equals(sourceUserId, that.sourceUserId) &&
            Objects.equals(categoryId, that.categoryId)
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
            recurringType,
            separationCount,
            maxNumberOfOcurrences,
            dayOfWeek,
            weekOfMonth,
            dayOfMonth,
            monthOfYear,
            currencyId,
            sourceUserId,
            categoryId
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
            (recurringType != null ? "recurringType=" + recurringType + ", " : "") +
            (separationCount != null ? "separationCount=" + separationCount + ", " : "") +
            (maxNumberOfOcurrences != null ? "maxNumberOfOcurrences=" + maxNumberOfOcurrences + ", " : "") +
            (dayOfWeek != null ? "dayOfWeek=" + dayOfWeek + ", " : "") +
            (weekOfMonth != null ? "weekOfMonth=" + weekOfMonth + ", " : "") +
            (dayOfMonth != null ? "dayOfMonth=" + dayOfMonth + ", " : "") +
            (monthOfYear != null ? "monthOfYear=" + monthOfYear + ", " : "") +
            (currencyId != null ? "currencyId=" + currencyId + ", " : "") +
            (sourceUserId != null ? "sourceUserId=" + sourceUserId + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            "}";
    }
}
