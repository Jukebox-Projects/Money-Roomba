package com.moneyroomba.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moneyroomba.domain.enumeration.MovementType;
import com.moneyroomba.domain.enumeration.TransactionState;
import com.moneyroomba.domain.enumeration.TransactionType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transaction.
 */
@Entity
@Table(name = "t_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transaction implements Serializable {

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
    @Column(name = "date_added", nullable = false)
    private LocalDate dateAdded;

    @DecimalMin(value = "0")
    @Column(name = "amount")
    private Double amount;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "original_amount", nullable = false)
    private Double originalAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;

    @NotNull
    @Column(name = "scheduled", nullable = false)
    private Boolean scheduled;

    @NotNull
    @Column(name = "add_to_reports", nullable = false)
    private Boolean addToReports;

    @NotNull
    @Column(name = "incoming_transaction", nullable = false)
    private Boolean incomingTransaction;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private TransactionState state;

    @OneToOne
    @JoinColumn(unique = true)
    private Attachment attachment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "transactions", "user", "currency" }, allowSetters = true)
    private Wallet wallet;

    @ManyToOne
    @JsonIgnoreProperties(value = { "transactions", "scheduledTransactions", "wallets", "invoices" }, allowSetters = true)
    private Currency currency;

    @ManyToOne
    @JsonIgnoreProperties(value = { "categories", "transactions", "parent", "user" }, allowSetters = true)
    private Category category;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "internalUser",
            "license",
            "wallets",
            "categories",
            "events",
            "transactions",
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
    @JsonIgnoreProperties(
        value = {
            "internalUser",
            "license",
            "wallets",
            "categories",
            "events",
            "transactions",
            "userDetails",
            "recievedTransactions",
            "targetContacts",
            "contact",
            "sourceContacts",
        },
        allowSetters = true
    )
    private UserDetails recievingUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaction id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Transaction name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Transaction description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public Transaction dateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Transaction amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getOriginalAmount() {
        return this.originalAmount;
    }

    public Transaction originalAmount(Double originalAmount) {
        this.originalAmount = originalAmount;
        return this;
    }

    public void setOriginalAmount(Double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public MovementType getMovementType() {
        return this.movementType;
    }

    public Transaction movementType(MovementType movementType) {
        this.movementType = movementType;
        return this;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public Boolean getScheduled() {
        return this.scheduled;
    }

    public Transaction scheduled(Boolean scheduled) {
        this.scheduled = scheduled;
        return this;
    }

    public void setScheduled(Boolean scheduled) {
        this.scheduled = scheduled;
    }

    public Boolean getAddToReports() {
        return this.addToReports;
    }

    public Transaction addToReports(Boolean addToReports) {
        this.addToReports = addToReports;
        return this;
    }

    public void setAddToReports(Boolean addToReports) {
        this.addToReports = addToReports;
    }

    public Boolean getIncomingTransaction() {
        return this.incomingTransaction;
    }

    public Transaction incomingTransaction(Boolean incomingTransaction) {
        this.incomingTransaction = incomingTransaction;
        return this;
    }

    public void setIncomingTransaction(Boolean incomingTransaction) {
        this.incomingTransaction = incomingTransaction;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public Transaction transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionState getState() {
        return this.state;
    }

    public Transaction state(TransactionState state) {
        this.state = state;
        return this;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }

    public Attachment getAttachment() {
        return this.attachment;
    }

    public Transaction attachment(Attachment attachment) {
        this.setAttachment(attachment);
        return this;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    public Transaction wallet(Wallet wallet) {
        this.setWallet(wallet);
        return this;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public Transaction currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Category getCategory() {
        return this.category;
    }

    public Transaction category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public UserDetails getSourceUser() {
        return this.sourceUser;
    }

    public Transaction sourceUser(UserDetails userDetails) {
        this.setSourceUser(userDetails);
        return this;
    }

    public void setSourceUser(UserDetails userDetails) {
        this.sourceUser = userDetails;
    }

    public UserDetails getRecievingUser() {
        return this.recievingUser;
    }

    public Transaction recievingUser(UserDetails userDetails) {
        this.setRecievingUser(userDetails);
        return this;
    }

    public void setRecievingUser(UserDetails userDetails) {
        this.recievingUser = userDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateAdded='" + getDateAdded() + "'" +
            ", amount=" + getAmount() +
            ", originalAmount=" + getOriginalAmount() +
            ", movementType='" + getMovementType() + "'" +
            ", scheduled='" + getScheduled() + "'" +
            ", addToReports='" + getAddToReports() + "'" +
            ", incomingTransaction='" + getIncomingTransaction() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
