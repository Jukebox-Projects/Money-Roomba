package com.moneyroomba.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.moneyroomba.domain.Currency} entity. This class is used
 * in {@link com.moneyroomba.web.rest.CurrencyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /currencies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CurrencyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private FloatFilter conversionRate;

    private BooleanFilter adminCreated;

    private BooleanFilter isActive;

    private LongFilter transactionId;

    private LongFilter scheduledTransactionId;

    private LongFilter walletId;

    private LongFilter invoiceId;

    public CurrencyCriteria() {}

    public CurrencyCriteria(CurrencyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.conversionRate = other.conversionRate == null ? null : other.conversionRate.copy();
        this.adminCreated = other.adminCreated == null ? null : other.adminCreated.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.transactionId = other.transactionId == null ? null : other.transactionId.copy();
        this.scheduledTransactionId = other.scheduledTransactionId == null ? null : other.scheduledTransactionId.copy();
        this.walletId = other.walletId == null ? null : other.walletId.copy();
        this.invoiceId = other.invoiceId == null ? null : other.invoiceId.copy();
    }

    @Override
    public CurrencyCriteria copy() {
        return new CurrencyCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
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

    public FloatFilter getConversionRate() {
        return conversionRate;
    }

    public FloatFilter conversionRate() {
        if (conversionRate == null) {
            conversionRate = new FloatFilter();
        }
        return conversionRate;
    }

    public void setConversionRate(FloatFilter conversionRate) {
        this.conversionRate = conversionRate;
    }

    public BooleanFilter getAdminCreated() {
        return adminCreated;
    }

    public BooleanFilter adminCreated() {
        if (adminCreated == null) {
            adminCreated = new BooleanFilter();
        }
        return adminCreated;
    }

    public void setAdminCreated(BooleanFilter adminCreated) {
        this.adminCreated = adminCreated;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getTransactionId() {
        return transactionId;
    }

    public LongFilter transactionId() {
        if (transactionId == null) {
            transactionId = new LongFilter();
        }
        return transactionId;
    }

    public void setTransactionId(LongFilter transactionId) {
        this.transactionId = transactionId;
    }

    public LongFilter getScheduledTransactionId() {
        return scheduledTransactionId;
    }

    public LongFilter scheduledTransactionId() {
        if (scheduledTransactionId == null) {
            scheduledTransactionId = new LongFilter();
        }
        return scheduledTransactionId;
    }

    public void setScheduledTransactionId(LongFilter scheduledTransactionId) {
        this.scheduledTransactionId = scheduledTransactionId;
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

    public LongFilter getInvoiceId() {
        return invoiceId;
    }

    public LongFilter invoiceId() {
        if (invoiceId == null) {
            invoiceId = new LongFilter();
        }
        return invoiceId;
    }

    public void setInvoiceId(LongFilter invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CurrencyCriteria that = (CurrencyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(conversionRate, that.conversionRate) &&
            Objects.equals(adminCreated, that.adminCreated) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(scheduledTransactionId, that.scheduledTransactionId) &&
            Objects.equals(walletId, that.walletId) &&
            Objects.equals(invoiceId, that.invoiceId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            name,
            conversionRate,
            adminCreated,
            isActive,
            transactionId,
            scheduledTransactionId,
            walletId,
            invoiceId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (conversionRate != null ? "conversionRate=" + conversionRate + ", " : "") +
            (adminCreated != null ? "adminCreated=" + adminCreated + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
            (scheduledTransactionId != null ? "scheduledTransactionId=" + scheduledTransactionId + ", " : "") +
            (walletId != null ? "walletId=" + walletId + ", " : "") +
            (invoiceId != null ? "invoiceId=" + invoiceId + ", " : "") +
            "}";
    }
}
