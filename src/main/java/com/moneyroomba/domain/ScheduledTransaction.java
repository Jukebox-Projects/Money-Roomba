package com.moneyroomba.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moneyroomba.domain.enumeration.MovementType;
import com.moneyroomba.domain.enumeration.RecurringType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ScheduledTransaction.
 */
@Entity
@Table(name = "t_scheduled_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ScheduledTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "original_amount", nullable = false)
    private Double originalAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Column(name = "add_to_reports", nullable = false)
    private Boolean addToReports;

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
    @JsonIgnoreProperties(value = { "transactions", "scheduledTransactions", "wallets", "invoices" }, allowSetters = true)
    private Currency currency;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "internalUser",
            "license",
            "wallets",
            "categories",
            "events",
            "transactions",
            "scheduledTransactions",
            "userDetails",
            "recievedTransactions",
            "targetContacts",
            "contact",
            "sourceContacts",
        },
        allowSetters = true
    )
    private UserDetails sourceUser;

    @ManyToOne
    @JsonIgnoreProperties(value = { "categories", "transactions", "scheduledTransactions", "parent", "user" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ScheduledTransaction id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ScheduledTransaction name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ScheduledTransaction description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getOriginalAmount() {
        return this.originalAmount;
    }

    public ScheduledTransaction originalAmount(Double originalAmount) {
        this.originalAmount = originalAmount;
        return this;
    }

    public void setOriginalAmount(Double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public MovementType getMovementType() {
        return this.movementType;
    }

    public ScheduledTransaction movementType(MovementType movementType) {
        this.movementType = movementType;
        return this;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public ScheduledTransaction startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public ScheduledTransaction endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getAddToReports() {
        return this.addToReports;
    }

    public ScheduledTransaction addToReports(Boolean addToReports) {
        this.addToReports = addToReports;
        return this;
    }

    public void setAddToReports(Boolean addToReports) {
        this.addToReports = addToReports;
    }

    public RecurringType getRecurringType() {
        return this.recurringType;
    }

    public ScheduledTransaction recurringType(RecurringType recurringType) {
        this.recurringType = recurringType;
        return this;
    }

    public void setRecurringType(RecurringType recurringType) {
        this.recurringType = recurringType;
    }

    public Integer getSeparationCount() {
        return this.separationCount;
    }

    public ScheduledTransaction separationCount(Integer separationCount) {
        this.separationCount = separationCount;
        return this;
    }

    public void setSeparationCount(Integer separationCount) {
        this.separationCount = separationCount;
    }

    public Integer getMaxNumberOfOcurrences() {
        return this.maxNumberOfOcurrences;
    }

    public ScheduledTransaction maxNumberOfOcurrences(Integer maxNumberOfOcurrences) {
        this.maxNumberOfOcurrences = maxNumberOfOcurrences;
        return this;
    }

    public void setMaxNumberOfOcurrences(Integer maxNumberOfOcurrences) {
        this.maxNumberOfOcurrences = maxNumberOfOcurrences;
    }

    public Integer getDayOfWeek() {
        return this.dayOfWeek;
    }

    public ScheduledTransaction dayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getWeekOfMonth() {
        return this.weekOfMonth;
    }

    public ScheduledTransaction weekOfMonth(Integer weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
        return this;
    }

    public void setWeekOfMonth(Integer weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }

    public Integer getDayOfMonth() {
        return this.dayOfMonth;
    }

    public ScheduledTransaction dayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        return this;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Integer getMonthOfYear() {
        return this.monthOfYear;
    }

    public ScheduledTransaction monthOfYear(Integer monthOfYear) {
        this.monthOfYear = monthOfYear;
        return this;
    }

    public void setMonthOfYear(Integer monthOfYear) {
        this.monthOfYear = monthOfYear;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public ScheduledTransaction currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public UserDetails getSourceUser() {
        return this.sourceUser;
    }

    public ScheduledTransaction sourceUser(UserDetails userDetails) {
        this.setSourceUser(userDetails);
        return this;
    }

    public void setSourceUser(UserDetails userDetails) {
        this.sourceUser = userDetails;
    }

    public Category getCategory() {
        return this.category;
    }

    public ScheduledTransaction category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScheduledTransaction)) {
            return false;
        }
        return id != null && id.equals(((ScheduledTransaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduledTransaction{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", originalAmount=" + getOriginalAmount() +
            ", movementType='" + getMovementType() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", addToReports='" + getAddToReports() + "'" +
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
