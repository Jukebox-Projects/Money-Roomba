package com.moneyroomba.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Currency.
 */
@Entity
@Table(name = "t_currency")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "conversion_rate", nullable = false)
    private Float conversionRate;

    @NotNull
    @Column(name = "admin_created", nullable = false)
    private Boolean adminCreated;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "currency")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attachment", "wallet", "currency", "category", "sourceUser" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "currency")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schedulePatterns", "currency" }, allowSetters = true)
    private Set<ScheduledTransaction> scheduledTransactions = new HashSet<>();

    @OneToMany(mappedBy = "currency")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transactions", "user", "currency" }, allowSetters = true)
    private Set<Wallet> wallets = new HashSet<>();

    @OneToMany(mappedBy = "currency")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "currency" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    public Currency(String code, String name, Float conversionRate, Boolean adminCreated, Boolean isActive) {
        this.code = code;
        this.name = name;
        this.conversionRate = conversionRate;
        this.adminCreated = adminCreated;
        this.isActive = isActive;
    }

    public Currency() {}

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Currency code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Currency name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getConversionRate() {
        return this.conversionRate;
    }

    public Currency conversionRate(Float conversionRate) {
        this.conversionRate = conversionRate;
        return this;
    }

    public void setConversionRate(Float conversionRate) {
        this.conversionRate = conversionRate;
    }

    public Boolean getAdminCreated() {
        return this.adminCreated;
    }

    public Currency adminCreated(Boolean adminCreated) {
        this.adminCreated = adminCreated;
        return this;
    }

    public void setAdminCreated(Boolean adminCreated) {
        this.adminCreated = adminCreated;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Currency isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public Currency transactions(Set<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public Currency addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setCurrency(this);
        return this;
    }

    public Currency removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setCurrency(null);
        return this;
    }

    public void setTransactions(Set<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setCurrency(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setCurrency(this));
        }
        this.transactions = transactions;
    }

    public Set<ScheduledTransaction> getScheduledTransactions() {
        return this.scheduledTransactions;
    }

    public Currency scheduledTransactions(Set<ScheduledTransaction> scheduledTransactions) {
        this.setScheduledTransactions(scheduledTransactions);
        return this;
    }

    public Currency addScheduledTransaction(ScheduledTransaction scheduledTransaction) {
        this.scheduledTransactions.add(scheduledTransaction);
        scheduledTransaction.setCurrency(this);
        return this;
    }

    public Currency removeScheduledTransaction(ScheduledTransaction scheduledTransaction) {
        this.scheduledTransactions.remove(scheduledTransaction);
        scheduledTransaction.setCurrency(null);
        return this;
    }

    public void setScheduledTransactions(Set<ScheduledTransaction> scheduledTransactions) {
        if (this.scheduledTransactions != null) {
            this.scheduledTransactions.forEach(i -> i.setCurrency(null));
        }
        if (scheduledTransactions != null) {
            scheduledTransactions.forEach(i -> i.setCurrency(this));
        }
        this.scheduledTransactions = scheduledTransactions;
    }

    public Set<Wallet> getWallets() {
        return this.wallets;
    }

    public Currency wallets(Set<Wallet> wallets) {
        this.setWallets(wallets);
        return this;
    }

    public Currency addWallet(Wallet wallet) {
        this.wallets.add(wallet);
        wallet.setCurrency(this);
        return this;
    }

    public Currency removeWallet(Wallet wallet) {
        this.wallets.remove(wallet);
        wallet.setCurrency(null);
        return this;
    }

    public void setWallets(Set<Wallet> wallets) {
        if (this.wallets != null) {
            this.wallets.forEach(i -> i.setCurrency(null));
        }
        if (wallets != null) {
            wallets.forEach(i -> i.setCurrency(this));
        }
        this.wallets = wallets;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public Currency invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Currency addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setCurrency(this);
        return this;
    }

    public Currency removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setCurrency(null);
        return this;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.setCurrency(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setCurrency(this));
        }
        this.invoices = invoices;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Currency)) {
            return false;
        }
        return id != null && id.equals(((Currency) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Currency{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", conversionRate=" + getConversionRate() +
            ", adminCreated='" + getAdminCreated() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
