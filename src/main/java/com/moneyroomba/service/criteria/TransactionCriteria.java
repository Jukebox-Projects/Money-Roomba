package com.moneyroomba.service.criteria;

import com.moneyroomba.domain.enumeration.MovementType;
import com.moneyroomba.domain.enumeration.TransactionType;
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
 * Criteria class for the {@link com.moneyroomba.domain.Transaction} entity. This class is used
 * in {@link com.moneyroomba.web.rest.TransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransactionCriteria implements Serializable, Criteria {

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
     * Class for filtering TransactionType
     */
    public static class TransactionTypeFilter extends Filter<TransactionType> {

        public TransactionTypeFilter() {}

        public TransactionTypeFilter(TransactionTypeFilter filter) {
            super(filter);
        }

        @Override
        public TransactionTypeFilter copy() {
            return new TransactionTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private LocalDateFilter dateAdded;

    private DoubleFilter amount;

    private DoubleFilter originalAmount;

    private MovementTypeFilter movementType;

    private BooleanFilter scheduled;

    private BooleanFilter addToReports;

    private BooleanFilter incomingTransaction;

    private TransactionTypeFilter transactionType;

    private LongFilter attachmentId;

    private LongFilter walletId;

    private LongFilter currencyId;

    private LongFilter categoryId;

    private LongFilter sourceUserId;

    public TransactionCriteria() {}

    public TransactionCriteria(TransactionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.dateAdded = other.dateAdded == null ? null : other.dateAdded.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.originalAmount = other.originalAmount == null ? null : other.originalAmount.copy();
        this.movementType = other.movementType == null ? null : other.movementType.copy();
        this.scheduled = other.scheduled == null ? null : other.scheduled.copy();
        this.addToReports = other.addToReports == null ? null : other.addToReports.copy();
        this.incomingTransaction = other.incomingTransaction == null ? null : other.incomingTransaction.copy();
        this.transactionType = other.transactionType == null ? null : other.transactionType.copy();
        this.attachmentId = other.attachmentId == null ? null : other.attachmentId.copy();
        this.walletId = other.walletId == null ? null : other.walletId.copy();
        this.currencyId = other.currencyId == null ? null : other.currencyId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.sourceUserId = other.sourceUserId == null ? null : other.sourceUserId.copy();
    }

    @Override
    public TransactionCriteria copy() {
        return new TransactionCriteria(this);
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

    public LocalDateFilter getDateAdded() {
        return dateAdded;
    }

    public LocalDateFilter dateAdded() {
        if (dateAdded == null) {
            dateAdded = new LocalDateFilter();
        }
        return dateAdded;
    }

    public void setDateAdded(LocalDateFilter dateAdded) {
        this.dateAdded = dateAdded;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public DoubleFilter amount() {
        if (amount == null) {
            amount = new DoubleFilter();
        }
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
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

    public BooleanFilter getScheduled() {
        return scheduled;
    }

    public BooleanFilter scheduled() {
        if (scheduled == null) {
            scheduled = new BooleanFilter();
        }
        return scheduled;
    }

    public void setScheduled(BooleanFilter scheduled) {
        this.scheduled = scheduled;
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

    public TransactionTypeFilter getTransactionType() {
        return transactionType;
    }

    public TransactionTypeFilter transactionType() {
        if (transactionType == null) {
            transactionType = new TransactionTypeFilter();
        }
        return transactionType;
    }

    public void setTransactionType(TransactionTypeFilter transactionType) {
        this.transactionType = transactionType;
    }

    public LongFilter getAttachmentId() {
        return attachmentId;
    }

    public LongFilter attachmentId() {
        if (attachmentId == null) {
            attachmentId = new LongFilter();
        }
        return attachmentId;
    }

    public void setAttachmentId(LongFilter attachmentId) {
        this.attachmentId = attachmentId;
    }

    public LongFilter getWalletId() {
        return walletId;
    }

    public LongFilter walletId() {
        if (walletId == null) {
            walletId = new LongFilter();
        }
        return walletId;
    }

    public void setWalletId(LongFilter walletId) {
        this.walletId = walletId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TransactionCriteria that = (TransactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(dateAdded, that.dateAdded) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(originalAmount, that.originalAmount) &&
            Objects.equals(movementType, that.movementType) &&
            Objects.equals(scheduled, that.scheduled) &&
            Objects.equals(addToReports, that.addToReports) &&
            Objects.equals(incomingTransaction, that.incomingTransaction) &&
            Objects.equals(transactionType, that.transactionType) &&
            Objects.equals(attachmentId, that.attachmentId) &&
            Objects.equals(walletId, that.walletId) &&
            Objects.equals(currencyId, that.currencyId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(sourceUserId, that.sourceUserId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            dateAdded,
            amount,
            originalAmount,
            movementType,
            scheduled,
            addToReports,
            incomingTransaction,
            transactionType,
            attachmentId,
            walletId,
            currencyId,
            categoryId,
            sourceUserId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (dateAdded != null ? "dateAdded=" + dateAdded + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (originalAmount != null ? "originalAmount=" + originalAmount + ", " : "") +
            (movementType != null ? "movementType=" + movementType + ", " : "") +
            (scheduled != null ? "scheduled=" + scheduled + ", " : "") +
            (addToReports != null ? "addToReports=" + addToReports + ", " : "") +
            (incomingTransaction != null ? "incomingTransaction=" + incomingTransaction + ", " : "") +
            (transactionType != null ? "transactionType=" + transactionType + ", " : "") +
            (attachmentId != null ? "attachmentId=" + attachmentId + ", " : "") +
            (walletId != null ? "walletId=" + walletId + ", " : "") +
            (currencyId != null ? "currencyId=" + currencyId + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (sourceUserId != null ? "sourceUserId=" + sourceUserId + ", " : "") +
            "}";
    }
}
