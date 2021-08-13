package com.moneyroomba.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moneyroomba.domain.Attachment;
import com.moneyroomba.domain.Category;
import com.moneyroomba.domain.Currency;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.Wallet;
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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TransactionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private LocalDate dateAdded;

    @DecimalMin(value = "0")
    private Double amount;

    @NotNull
    @DecimalMin(value = "0")
    private Double originalAmount;

    @NotNull
    private MovementType movementType;

    @NotNull
    private Boolean scheduled;

    @NotNull
    private Boolean addToReports;

    @NotNull
    private Boolean incomingTransaction;

    @NotNull
    private TransactionType transactionType;

    private TransactionState state;

    private Attachment attachment;

    @NotNull
    @JsonIgnoreProperties(value = { "transactions", "user", "currency" }, allowSetters = true)
    private Wallet wallet;

    @JsonIgnoreProperties(value = { "transactions", "scheduledTransactions", "wallets", "invoices" }, allowSetters = true)
    private Currency currency;

    @JsonIgnoreProperties(value = { "categories", "transactions", "parent", "user" }, allowSetters = true)
    private Category category;

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

    private ContactDTO recievingUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionDTO id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public TransactionDTO name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public TransactionDTO description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public TransactionDTO dateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Double getAmount() {
        return this.amount;
    }

    public TransactionDTO amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getOriginalAmount() {
        return this.originalAmount;
    }

    public TransactionDTO originalAmount(Double originalAmount) {
        this.originalAmount = originalAmount;
        return this;
    }

    public void setOriginalAmount(Double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public MovementType getMovementType() {
        return this.movementType;
    }

    public TransactionDTO movementType(MovementType movementType) {
        this.movementType = movementType;
        return this;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public Boolean getScheduled() {
        return this.scheduled;
    }

    public TransactionDTO scheduled(Boolean scheduled) {
        this.scheduled = scheduled;
        return this;
    }

    public void setScheduled(Boolean scheduled) {
        this.scheduled = scheduled;
    }

    public Boolean getAddToReports() {
        return this.addToReports;
    }

    public TransactionDTO addToReports(Boolean addToReports) {
        this.addToReports = addToReports;
        return this;
    }

    public void setAddToReports(Boolean addToReports) {
        this.addToReports = addToReports;
    }

    public Boolean getIncomingTransaction() {
        return this.incomingTransaction;
    }

    public TransactionDTO incomingTransaction(Boolean incomingTransaction) {
        this.incomingTransaction = incomingTransaction;
        return this;
    }

    public void setIncomingTransaction(Boolean incomingTransaction) {
        this.incomingTransaction = incomingTransaction;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public TransactionDTO transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionState getState() {
        return this.state;
    }

    public TransactionDTO state(TransactionState state) {
        this.state = state;
        return this;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }

    public Attachment getAttachment() {
        return this.attachment;
    }

    public TransactionDTO attachment(Attachment attachment) {
        this.setAttachment(attachment);
        return this;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    public TransactionDTO wallet(Wallet wallet) {
        this.setWallet(wallet);
        return this;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public TransactionDTO currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Category getCategory() {
        return this.category;
    }

    public TransactionDTO category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public UserDetails getSourceUser() {
        return this.sourceUser;
    }

    public TransactionDTO sourceUser(UserDetails userDetails) {
        this.setSourceUser(userDetails);
        return this;
    }

    public void setSourceUser(UserDetails userDetails) {
        this.sourceUser = userDetails;
    }

    public ContactDTO getRecievingUser() {
        return this.recievingUser;
    }

    public TransactionDTO recievingUser(ContactDTO userDetails) {
        this.setRecievingUser(userDetails);
        return this;
    }

    public void setRecievingUser(ContactDTO userDetails) {
        this.recievingUser = userDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionDTO)) {
            return false;
        }
        return id != null && id.equals(((TransactionDTO) o).id);
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
