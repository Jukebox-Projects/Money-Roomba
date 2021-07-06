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
 * Criteria class for the {@link com.moneyroomba.domain.Wallet} entity. This class is used
 * in {@link com.moneyroomba.web.rest.WalletResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /wallets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WalletCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private BooleanFilter inReports;

    private BooleanFilter isActive;

    private DoubleFilter balance;

    private LongFilter transactionId;

    private LongFilter userId;

    private LongFilter iconId;

    private LongFilter currencyId;

    public WalletCriteria() {}

    public WalletCriteria(WalletCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.inReports = other.inReports == null ? null : other.inReports.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.balance = other.balance == null ? null : other.balance.copy();
        this.transactionId = other.transactionId == null ? null : other.transactionId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.iconId = other.iconId == null ? null : other.iconId.copy();
        this.currencyId = other.currencyId == null ? null : other.currencyId.copy();
    }

    @Override
    public WalletCriteria copy() {
        return new WalletCriteria(this);
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

    public BooleanFilter getInReports() {
        return inReports;
    }

    public BooleanFilter inReports() {
        if (inReports == null) {
            inReports = new BooleanFilter();
        }
        return inReports;
    }

    public void setInReports(BooleanFilter inReports) {
        this.inReports = inReports;
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

    public DoubleFilter getBalance() {
        return balance;
    }

    public DoubleFilter balance() {
        if (balance == null) {
            balance = new DoubleFilter();
        }
        return balance;
    }

    public void setBalance(DoubleFilter balance) {
        this.balance = balance;
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

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getIconId() {
        return iconId;
    }

    public LongFilter iconId() {
        if (iconId == null) {
            iconId = new LongFilter();
        }
        return iconId;
    }

    public void setIconId(LongFilter iconId) {
        this.iconId = iconId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WalletCriteria that = (WalletCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(inReports, that.inReports) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(balance, that.balance) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(iconId, that.iconId) &&
            Objects.equals(currencyId, that.currencyId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, inReports, isActive, balance, transactionId, userId, iconId, currencyId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WalletCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (inReports != null ? "inReports=" + inReports + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (balance != null ? "balance=" + balance + ", " : "") +
            (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (iconId != null ? "iconId=" + iconId + ", " : "") +
            (currencyId != null ? "currencyId=" + currencyId + ", " : "") +
            "}";
    }
}
