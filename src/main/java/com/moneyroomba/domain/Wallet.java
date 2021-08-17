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
 * A Wallet.
 */
@Entity
@Table(name = "t_wallet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Wallet implements Serializable {

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
    @Column(name = "in_reports", nullable = false)
    private Boolean inReports;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "balance", nullable = false)
    private Double balance;

    @Min(value = 0)
    @Column(name = "icon")
    private Integer icon;

    @OneToMany(mappedBy = "wallet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attachment", "wallet", "currency", "category", "sourceUser", "recievingUser" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "wallet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "currency", "sourceUser", "category", "wallet" }, allowSetters = true)
    private Set<ScheduledTransaction> scheduledTransactions = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
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
    private UserDetails user;

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

    public Wallet id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Wallet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Wallet description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getInReports() {
        return this.inReports;
    }

    public Wallet inReports(Boolean inReports) {
        this.inReports = inReports;
        return this;
    }

    public void setInReports(Boolean inReports) {
        this.inReports = inReports;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Wallet isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Double getBalance() {
        return this.balance;
    }

    public Wallet balance(Double balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getIcon() {
        return this.icon;
    }

    public Wallet icon(Integer icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public Wallet transactions(Set<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public Wallet addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setWallet(this);
        return this;
    }

    public Wallet removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setWallet(null);
        return this;
    }

    public void setTransactions(Set<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setWallet(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setWallet(this));
        }
        this.transactions = transactions;
    }

    public Set<ScheduledTransaction> getScheduledTransactions() {
        return this.scheduledTransactions;
    }

    public Wallet scheduledTransactions(Set<ScheduledTransaction> scheduledTransactions) {
        this.setScheduledTransactions(scheduledTransactions);
        return this;
    }

    public Wallet addScheduledTransaction(ScheduledTransaction scheduledTransaction) {
        this.scheduledTransactions.add(scheduledTransaction);
        scheduledTransaction.setWallet(this);
        return this;
    }

    public Wallet removeScheduledTransaction(ScheduledTransaction scheduledTransaction) {
        this.scheduledTransactions.remove(scheduledTransaction);
        scheduledTransaction.setWallet(null);
        return this;
    }

    public void setScheduledTransactions(Set<ScheduledTransaction> scheduledTransactions) {
        if (this.scheduledTransactions != null) {
            this.scheduledTransactions.forEach(i -> i.setWallet(null));
        }
        if (scheduledTransactions != null) {
            scheduledTransactions.forEach(i -> i.setWallet(this));
        }
        this.scheduledTransactions = scheduledTransactions;
    }

    public UserDetails getUser() {
        return this.user;
    }

    public Wallet user(UserDetails userDetails) {
        this.setUser(userDetails);
        return this;
    }

    public void setUser(UserDetails userDetails) {
        this.user = userDetails;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public Wallet currency(Currency currency) {
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
        if (!(o instanceof Wallet)) {
            return false;
        }
        return id != null && id.equals(((Wallet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Wallet{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", inReports='" + getInReports() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", balance=" + getBalance() +
            ", icon=" + getIcon() +
            "}";
    }
}
