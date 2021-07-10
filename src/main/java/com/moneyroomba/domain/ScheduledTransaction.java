package com.moneyroomba.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moneyroomba.domain.enumeration.MovementType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
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
    @Column(name = "incoming_transaction", nullable = false)
    private Boolean incomingTransaction;

    @OneToMany(mappedBy = "scheduleTransaction")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "scheduleTransaction" }, allowSetters = true)
    private Set<SchedulePattern> schedulePatterns = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "transactions", "scheduledTransactions", "wallets", "invoices" }, allowSetters = true)
    private Currency currency;

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

    public Boolean getIncomingTransaction() {
        return this.incomingTransaction;
    }

    public ScheduledTransaction incomingTransaction(Boolean incomingTransaction) {
        this.incomingTransaction = incomingTransaction;
        return this;
    }

    public void setIncomingTransaction(Boolean incomingTransaction) {
        this.incomingTransaction = incomingTransaction;
    }

    public Set<SchedulePattern> getSchedulePatterns() {
        return this.schedulePatterns;
    }

    public ScheduledTransaction schedulePatterns(Set<SchedulePattern> schedulePatterns) {
        this.setSchedulePatterns(schedulePatterns);
        return this;
    }

    public ScheduledTransaction addSchedulePattern(SchedulePattern schedulePattern) {
        this.schedulePatterns.add(schedulePattern);
        schedulePattern.setScheduleTransaction(this);
        return this;
    }

    public ScheduledTransaction removeSchedulePattern(SchedulePattern schedulePattern) {
        this.schedulePatterns.remove(schedulePattern);
        schedulePattern.setScheduleTransaction(null);
        return this;
    }

    public void setSchedulePatterns(Set<SchedulePattern> schedulePatterns) {
        if (this.schedulePatterns != null) {
            this.schedulePatterns.forEach(i -> i.setScheduleTransaction(null));
        }
        if (schedulePatterns != null) {
            schedulePatterns.forEach(i -> i.setScheduleTransaction(this));
        }
        this.schedulePatterns = schedulePatterns;
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
            ", incomingTransaction='" + getIncomingTransaction() + "'" +
            "}";
    }
}
