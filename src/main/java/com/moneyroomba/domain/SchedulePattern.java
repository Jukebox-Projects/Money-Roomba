package com.moneyroomba.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moneyroomba.domain.enumeration.RecurringType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SchedulePattern.
 */
@Entity
@Table(name = "t_schedule_pattern")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SchedulePattern implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "recurring_type", nullable = false)
    private RecurringType recurringType;

    @Min(value = 0)
    @Column(name = "separation_count")
    private Integer separationCount;

    @Column(name = "max_number_of_ocurrences")
    private Integer maxNumberOfOcurrences;

    @Min(value = 0)
    @Max(value = 6)
    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "week_of_month")
    private Integer weekOfMonth;

    @Min(value = 0)
    @Max(value = 31)
    @Column(name = "day_of_month")
    private Integer dayOfMonth;

    @Min(value = 0)
    @Max(value = 11)
    @Column(name = "month_of_year")
    private Integer monthOfYear;

    @ManyToOne
    @JsonIgnoreProperties(value = { "currency", "sourceUser", "category" }, allowSetters = true)
    private ScheduledTransaction scheduleTransaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchedulePattern id(Long id) {
        this.id = id;
        return this;
    }

    public RecurringType getRecurringType() {
        return this.recurringType;
    }

    public SchedulePattern recurringType(RecurringType recurringType) {
        this.recurringType = recurringType;
        return this;
    }

    public void setRecurringType(RecurringType recurringType) {
        this.recurringType = recurringType;
    }

    public Integer getSeparationCount() {
        return this.separationCount;
    }

    public SchedulePattern separationCount(Integer separationCount) {
        this.separationCount = separationCount;
        return this;
    }

    public void setSeparationCount(Integer separationCount) {
        this.separationCount = separationCount;
    }

    public Integer getMaxNumberOfOcurrences() {
        return this.maxNumberOfOcurrences;
    }

    public SchedulePattern maxNumberOfOcurrences(Integer maxNumberOfOcurrences) {
        this.maxNumberOfOcurrences = maxNumberOfOcurrences;
        return this;
    }

    public void setMaxNumberOfOcurrences(Integer maxNumberOfOcurrences) {
        this.maxNumberOfOcurrences = maxNumberOfOcurrences;
    }

    public Integer getDayOfWeek() {
        return this.dayOfWeek;
    }

    public SchedulePattern dayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getWeekOfMonth() {
        return this.weekOfMonth;
    }

    public SchedulePattern weekOfMonth(Integer weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
        return this;
    }

    public void setWeekOfMonth(Integer weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }

    public Integer getDayOfMonth() {
        return this.dayOfMonth;
    }

    public SchedulePattern dayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        return this;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Integer getMonthOfYear() {
        return this.monthOfYear;
    }

    public SchedulePattern monthOfYear(Integer monthOfYear) {
        this.monthOfYear = monthOfYear;
        return this;
    }

    public void setMonthOfYear(Integer monthOfYear) {
        this.monthOfYear = monthOfYear;
    }

    public ScheduledTransaction getScheduleTransaction() {
        return this.scheduleTransaction;
    }

    public SchedulePattern scheduleTransaction(ScheduledTransaction scheduledTransaction) {
        this.setScheduleTransaction(scheduledTransaction);
        return this;
    }

    public void setScheduleTransaction(ScheduledTransaction scheduledTransaction) {
        this.scheduleTransaction = scheduledTransaction;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedulePattern)) {
            return false;
        }
        return id != null && id.equals(((SchedulePattern) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedulePattern{" +
            "id=" + getId() +
            ", recurringType='" + getRecurringType() + "'" +
            ", separationCount=" + getSeparationCount() +
            ", maxNumberOfOcurrences=" + getMaxNumberOfOcurrences() +
            ", dayOfWeek=" + getDayOfWeek() +
            ", weekOfMonth=" + getWeekOfMonth() +
            ", dayOfMonth=" + getDayOfMonth() +
            ", monthOfYear=" + getMonthOfYear() +
            "}";
    }
}
