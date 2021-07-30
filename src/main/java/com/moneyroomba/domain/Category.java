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
 * A Category.
 */
@Entity
@Table(name = "t_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "user_created", nullable = false)
    private Boolean userCreated;

    @Min(value = 0)
    @Column(name = "icon")
    private Integer icon;

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "categories", "transactions", "scheduledTransactions", "parent", "user" }, allowSetters = true)
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attachment", "wallet", "currency", "category", "sourceUser", "recievingUser" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "currency", "sourceUser", "category", "wallet" }, allowSetters = true)
    private Set<ScheduledTransaction> scheduledTransactions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "categories", "transactions", "scheduledTransactions", "parent", "user" }, allowSetters = true)
    private Category parent;

    @ManyToOne
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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Category name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Category isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getUserCreated() {
        return this.userCreated;
    }

    public Category userCreated(Boolean userCreated) {
        this.userCreated = userCreated;
        return this;
    }

    public void setUserCreated(Boolean userCreated) {
        this.userCreated = userCreated;
    }

    public Integer getIcon() {
        return this.icon;
    }

    public Category icon(Integer icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public Category categories(Set<Category> categories) {
        this.setCategories(categories);
        return this;
    }

    public Category addCategory(Category category) {
        this.categories.add(category);
        category.setParent(this);
        return this;
    }

    public Category removeCategory(Category category) {
        this.categories.remove(category);
        category.setParent(null);
        return this;
    }

    public void setCategories(Set<Category> categories) {
        if (this.categories != null) {
            this.categories.forEach(i -> i.setParent(null));
        }
        if (categories != null) {
            categories.forEach(i -> i.setParent(this));
        }
        this.categories = categories;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public Category transactions(Set<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public Category addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setCategory(this);
        return this;
    }

    public Category removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setCategory(null);
        return this;
    }

    public void setTransactions(Set<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setCategory(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setCategory(this));
        }
        this.transactions = transactions;
    }

    public Set<ScheduledTransaction> getScheduledTransactions() {
        return this.scheduledTransactions;
    }

    public Category scheduledTransactions(Set<ScheduledTransaction> scheduledTransactions) {
        this.setScheduledTransactions(scheduledTransactions);
        return this;
    }

    public Category addScheduledTransaction(ScheduledTransaction scheduledTransaction) {
        this.scheduledTransactions.add(scheduledTransaction);
        scheduledTransaction.setCategory(this);
        return this;
    }

    public Category removeScheduledTransaction(ScheduledTransaction scheduledTransaction) {
        this.scheduledTransactions.remove(scheduledTransaction);
        scheduledTransaction.setCategory(null);
        return this;
    }

    public void setScheduledTransactions(Set<ScheduledTransaction> scheduledTransactions) {
        if (this.scheduledTransactions != null) {
            this.scheduledTransactions.forEach(i -> i.setCategory(null));
        }
        if (scheduledTransactions != null) {
            scheduledTransactions.forEach(i -> i.setCategory(this));
        }
        this.scheduledTransactions = scheduledTransactions;
    }

    public Category getParent() {
        return this.parent;
    }

    public Category parent(Category category) {
        this.setParent(category);
        return this;
    }

    public void setParent(Category category) {
        this.parent = category;
    }

    public UserDetails getUser() {
        return this.user;
    }

    public Category user(UserDetails userDetails) {
        this.setUser(userDetails);
        return this;
    }

    public void setUser(UserDetails userDetails) {
        this.user = userDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", userCreated='" + getUserCreated() + "'" +
            ", icon=" + getIcon() +
            "}";
    }
}
