package com.moneyroomba.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Invoice.
 */
@Entity
@Table(name = "t_invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotNull
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotNull
    @Column(name = "user_last_name", nullable = false)
    private String userLastName;

    @NotNull
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @NotNull
    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total", nullable = false)
    private Double total;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "sub_total", nullable = false)
    private Double subTotal;

    @DecimalMin(value = "0")
    @Column(name = "tax")
    private Double tax;

    @Column(name = "purchase_description")
    private String purchaseDescription;

    @NotNull
    @Min(value = 0)
    @Column(name = "item_quantity", nullable = false)
    private Integer itemQuantity;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "item_price", nullable = false)
    private Double itemPrice;

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

    public Invoice id(Long id) {
        this.id = id;
        return this;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Invoice companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUserName() {
        return this.userName;
    }

    public Invoice userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLastName() {
        return this.userLastName;
    }

    public Invoice userLastName(String userLastName) {
        this.userLastName = userLastName;
        return this;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public Invoice userEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDate getDateCreated() {
        return this.dateCreated;
    }

    public Invoice dateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Double getTotal() {
        return this.total;
    }

    public Invoice total(Double total) {
        this.total = total;
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getSubTotal() {
        return this.subTotal;
    }

    public Invoice subTotal(Double subTotal) {
        this.subTotal = subTotal;
        return this;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getTax() {
        return this.tax;
    }

    public Invoice tax(Double tax) {
        this.tax = tax;
        return this;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public String getPurchaseDescription() {
        return this.purchaseDescription;
    }

    public Invoice purchaseDescription(String purchaseDescription) {
        this.purchaseDescription = purchaseDescription;
        return this;
    }

    public void setPurchaseDescription(String purchaseDescription) {
        this.purchaseDescription = purchaseDescription;
    }

    public Integer getItemQuantity() {
        return this.itemQuantity;
    }

    public Invoice itemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
        return this;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Double getItemPrice() {
        return this.itemPrice;
    }

    public Invoice itemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
        return this;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public Invoice currency(Currency currency) {
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
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", userName='" + getUserName() + "'" +
            ", userLastName='" + getUserLastName() + "'" +
            ", userEmail='" + getUserEmail() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", total=" + getTotal() +
            ", subTotal=" + getSubTotal() +
            ", tax=" + getTax() +
            ", purchaseDescription='" + getPurchaseDescription() + "'" +
            ", itemQuantity=" + getItemQuantity() +
            ", itemPrice=" + getItemPrice() +
            "}";
    }
}
