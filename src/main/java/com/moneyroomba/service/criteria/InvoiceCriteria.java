package com.moneyroomba.service.criteria;

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
 * Criteria class for the {@link com.moneyroomba.domain.Invoice} entity. This class is used
 * in {@link com.moneyroomba.web.rest.InvoiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InvoiceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter companyName;

    private StringFilter userName;

    private StringFilter userLastName;

    private StringFilter userEmail;

    private LocalDateFilter dateCreated;

    private DoubleFilter total;

    private DoubleFilter subTotal;

    private DoubleFilter tax;

    private StringFilter purchaseDescription;

    private IntegerFilter itemQuantity;

    private DoubleFilter itemPrice;

    private LongFilter currencyId;

    public InvoiceCriteria() {}

    public InvoiceCriteria(InvoiceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.companyName = other.companyName == null ? null : other.companyName.copy();
        this.userName = other.userName == null ? null : other.userName.copy();
        this.userLastName = other.userLastName == null ? null : other.userLastName.copy();
        this.userEmail = other.userEmail == null ? null : other.userEmail.copy();
        this.dateCreated = other.dateCreated == null ? null : other.dateCreated.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.subTotal = other.subTotal == null ? null : other.subTotal.copy();
        this.tax = other.tax == null ? null : other.tax.copy();
        this.purchaseDescription = other.purchaseDescription == null ? null : other.purchaseDescription.copy();
        this.itemQuantity = other.itemQuantity == null ? null : other.itemQuantity.copy();
        this.itemPrice = other.itemPrice == null ? null : other.itemPrice.copy();
        this.currencyId = other.currencyId == null ? null : other.currencyId.copy();
    }

    @Override
    public InvoiceCriteria copy() {
        return new InvoiceCriteria(this);
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

    public StringFilter getCompanyName() {
        return companyName;
    }

    public StringFilter companyName() {
        if (companyName == null) {
            companyName = new StringFilter();
        }
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
    }

    public StringFilter getUserName() {
        return userName;
    }

    public StringFilter userName() {
        if (userName == null) {
            userName = new StringFilter();
        }
        return userName;
    }

    public void setUserName(StringFilter userName) {
        this.userName = userName;
    }

    public StringFilter getUserLastName() {
        return userLastName;
    }

    public StringFilter userLastName() {
        if (userLastName == null) {
            userLastName = new StringFilter();
        }
        return userLastName;
    }

    public void setUserLastName(StringFilter userLastName) {
        this.userLastName = userLastName;
    }

    public StringFilter getUserEmail() {
        return userEmail;
    }

    public StringFilter userEmail() {
        if (userEmail == null) {
            userEmail = new StringFilter();
        }
        return userEmail;
    }

    public void setUserEmail(StringFilter userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateFilter getDateCreated() {
        return dateCreated;
    }

    public LocalDateFilter dateCreated() {
        if (dateCreated == null) {
            dateCreated = new LocalDateFilter();
        }
        return dateCreated;
    }

    public void setDateCreated(LocalDateFilter dateCreated) {
        this.dateCreated = dateCreated;
    }

    public DoubleFilter getTotal() {
        return total;
    }

    public DoubleFilter total() {
        if (total == null) {
            total = new DoubleFilter();
        }
        return total;
    }

    public void setTotal(DoubleFilter total) {
        this.total = total;
    }

    public DoubleFilter getSubTotal() {
        return subTotal;
    }

    public DoubleFilter subTotal() {
        if (subTotal == null) {
            subTotal = new DoubleFilter();
        }
        return subTotal;
    }

    public void setSubTotal(DoubleFilter subTotal) {
        this.subTotal = subTotal;
    }

    public DoubleFilter getTax() {
        return tax;
    }

    public DoubleFilter tax() {
        if (tax == null) {
            tax = new DoubleFilter();
        }
        return tax;
    }

    public void setTax(DoubleFilter tax) {
        this.tax = tax;
    }

    public StringFilter getPurchaseDescription() {
        return purchaseDescription;
    }

    public StringFilter purchaseDescription() {
        if (purchaseDescription == null) {
            purchaseDescription = new StringFilter();
        }
        return purchaseDescription;
    }

    public void setPurchaseDescription(StringFilter purchaseDescription) {
        this.purchaseDescription = purchaseDescription;
    }

    public IntegerFilter getItemQuantity() {
        return itemQuantity;
    }

    public IntegerFilter itemQuantity() {
        if (itemQuantity == null) {
            itemQuantity = new IntegerFilter();
        }
        return itemQuantity;
    }

    public void setItemQuantity(IntegerFilter itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public DoubleFilter getItemPrice() {
        return itemPrice;
    }

    public DoubleFilter itemPrice() {
        if (itemPrice == null) {
            itemPrice = new DoubleFilter();
        }
        return itemPrice;
    }

    public void setItemPrice(DoubleFilter itemPrice) {
        this.itemPrice = itemPrice;
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
        final InvoiceCriteria that = (InvoiceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(userLastName, that.userLastName) &&
            Objects.equals(userEmail, that.userEmail) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(total, that.total) &&
            Objects.equals(subTotal, that.subTotal) &&
            Objects.equals(tax, that.tax) &&
            Objects.equals(purchaseDescription, that.purchaseDescription) &&
            Objects.equals(itemQuantity, that.itemQuantity) &&
            Objects.equals(itemPrice, that.itemPrice) &&
            Objects.equals(currencyId, that.currencyId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            companyName,
            userName,
            userLastName,
            userEmail,
            dateCreated,
            total,
            subTotal,
            tax,
            purchaseDescription,
            itemQuantity,
            itemPrice,
            currencyId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (companyName != null ? "companyName=" + companyName + ", " : "") +
            (userName != null ? "userName=" + userName + ", " : "") +
            (userLastName != null ? "userLastName=" + userLastName + ", " : "") +
            (userEmail != null ? "userEmail=" + userEmail + ", " : "") +
            (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
            (total != null ? "total=" + total + ", " : "") +
            (subTotal != null ? "subTotal=" + subTotal + ", " : "") +
            (tax != null ? "tax=" + tax + ", " : "") +
            (purchaseDescription != null ? "purchaseDescription=" + purchaseDescription + ", " : "") +
            (itemQuantity != null ? "itemQuantity=" + itemQuantity + ", " : "") +
            (itemPrice != null ? "itemPrice=" + itemPrice + ", " : "") +
            (currencyId != null ? "currencyId=" + currencyId + ", " : "") +
            "}";
    }
}
