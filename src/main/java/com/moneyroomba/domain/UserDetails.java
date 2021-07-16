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
 * A UserDetails.
 */
@Entity
@Table(name = "t_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "api_key")
    private String apiKey;

    @NotNull
    @Column(name = "notifications", nullable = false)
    private Boolean notifications;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "is_temporary_password", nullable = false)
    private Boolean isTemporaryPassword;

    @OneToOne
    @JoinColumn(unique = true)
    private User internalUser;

    @JsonIgnoreProperties(value = { "invoice" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private License license;

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transactions", "user", "currency" }, allowSetters = true)
    private Set<Wallet> wallets = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "categories", "transactions", "parent", "user" }, allowSetters = true)
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "notification", "user" }, allowSetters = true)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "sourceUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attachment", "wallet", "currency", "category", "sourceUser" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "contact")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "internalUser",
            "license",
            "wallets",
            "categories",
            "events",
            "transactions",
            "userDetails",
            "targetContacts",
            "contact",
            "sourceContacts",
        },
        allowSetters = true
    )
    private Set<UserDetails> userDetails = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_t_user__target_contact",
        joinColumns = @JoinColumn(name = "t_user_id"),
        inverseJoinColumns = @JoinColumn(name = "target_contact_id")
    )
    @JsonIgnoreProperties(
        value = {
            "internalUser",
            "license",
            "wallets",
            "categories",
            "events",
            "transactions",
            "userDetails",
            "targetContacts",
            "contact",
            "sourceContacts",
        },
        allowSetters = true
    )
    private Set<UserDetails> targetContacts = new HashSet<>();

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
            "targetContacts",
            "contact",
            "sourceContacts",
        },
        allowSetters = true
    )
    private UserDetails contact;

    @ManyToMany(mappedBy = "targetContacts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "internalUser",
            "license",
            "wallets",
            "categories",
            "events",
            "transactions",
            "userDetails",
            "targetContacts",
            "contact",
            "sourceContacts",
        },
        allowSetters = true
    )
    private Set<UserDetails> sourceContacts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDetails id(Long id) {
        this.id = id;
        return this;
    }

    public String getCountry() {
        return this.country;
    }

    public UserDetails country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return this.phone;
    }

    public UserDetails phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public UserDetails apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Boolean getNotifications() {
        return this.notifications;
    }

    public UserDetails notifications(Boolean notifications) {
        this.notifications = notifications;
        return this;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public UserDetails isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsTemporaryPassword() {
        return this.isTemporaryPassword;
    }

    public UserDetails isTemporaryPassword(Boolean isTemporaryPassword) {
        this.isTemporaryPassword = isTemporaryPassword;
        return this;
    }

    public void setIsTemporaryPassword(Boolean isTemporaryPassword) {
        this.isTemporaryPassword = isTemporaryPassword;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public UserDetails internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public License getLicense() {
        return this.license;
    }

    public UserDetails license(License license) {
        this.setLicense(license);
        return this;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public Set<Wallet> getWallets() {
        return this.wallets;
    }

    public UserDetails wallets(Set<Wallet> wallets) {
        this.setWallets(wallets);
        return this;
    }

    public UserDetails addWallet(Wallet wallet) {
        this.wallets.add(wallet);
        wallet.setUser(this);
        return this;
    }

    public UserDetails removeWallet(Wallet wallet) {
        this.wallets.remove(wallet);
        wallet.setUser(null);
        return this;
    }

    public void setWallets(Set<Wallet> wallets) {
        if (this.wallets != null) {
            this.wallets.forEach(i -> i.setUser(null));
        }
        if (wallets != null) {
            wallets.forEach(i -> i.setUser(this));
        }
        this.wallets = wallets;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public UserDetails categories(Set<Category> categories) {
        this.setCategories(categories);
        return this;
    }

    public UserDetails addCategory(Category category) {
        this.categories.add(category);
        category.setUser(this);
        return this;
    }

    public UserDetails removeCategory(Category category) {
        this.categories.remove(category);
        category.setUser(null);
        return this;
    }

    public void setCategories(Set<Category> categories) {
        if (this.categories != null) {
            this.categories.forEach(i -> i.setUser(null));
        }
        if (categories != null) {
            categories.forEach(i -> i.setUser(this));
        }
        this.categories = categories;
    }

    public Set<Event> getEvents() {
        return this.events;
    }

    public UserDetails events(Set<Event> events) {
        this.setEvents(events);
        return this;
    }

    public UserDetails addEvent(Event event) {
        this.events.add(event);
        event.setUser(this);
        return this;
    }

    public UserDetails removeEvent(Event event) {
        this.events.remove(event);
        event.setUser(null);
        return this;
    }

    public void setEvents(Set<Event> events) {
        if (this.events != null) {
            this.events.forEach(i -> i.setUser(null));
        }
        if (events != null) {
            events.forEach(i -> i.setUser(this));
        }
        this.events = events;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public UserDetails transactions(Set<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public UserDetails addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setSourceUser(this);
        return this;
    }

    public UserDetails removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setSourceUser(null);
        return this;
    }

    public void setTransactions(Set<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setSourceUser(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setSourceUser(this));
        }
        this.transactions = transactions;
    }

    public Set<UserDetails> getUserDetails() {
        return this.userDetails;
    }

    public UserDetails userDetails(Set<UserDetails> userDetails) {
        this.setUserDetails(userDetails);
        return this;
    }

    public UserDetails addUserDetails(UserDetails userDetails) {
        this.userDetails.add(userDetails);
        userDetails.setContact(this);
        return this;
    }

    public UserDetails removeUserDetails(UserDetails userDetails) {
        this.userDetails.remove(userDetails);
        userDetails.setContact(null);
        return this;
    }

    public void setUserDetails(Set<UserDetails> userDetails) {
        if (this.userDetails != null) {
            this.userDetails.forEach(i -> i.setContact(null));
        }
        if (userDetails != null) {
            userDetails.forEach(i -> i.setContact(this));
        }
        this.userDetails = userDetails;
    }

    public Set<UserDetails> getTargetContacts() {
        return this.targetContacts;
    }

    public UserDetails targetContacts(Set<UserDetails> userDetails) {
        this.setTargetContacts(userDetails);
        return this;
    }

    public UserDetails addTargetContact(UserDetails userDetails) {
        this.targetContacts.add(userDetails);
        userDetails.getSourceContacts().add(this);
        return this;
    }

    public UserDetails removeTargetContact(UserDetails userDetails) {
        this.targetContacts.remove(userDetails);
        userDetails.getSourceContacts().remove(this);
        return this;
    }

    public void setTargetContacts(Set<UserDetails> userDetails) {
        this.targetContacts = userDetails;
    }

    public UserDetails getContact() {
        return this.contact;
    }

    public UserDetails contact(UserDetails userDetails) {
        this.setContact(userDetails);
        return this;
    }

    public void setContact(UserDetails userDetails) {
        this.contact = userDetails;
    }

    public Set<UserDetails> getSourceContacts() {
        return this.sourceContacts;
    }

    public UserDetails sourceContacts(Set<UserDetails> userDetails) {
        this.setSourceContacts(userDetails);
        return this;
    }

    public UserDetails addSourceContact(UserDetails userDetails) {
        this.sourceContacts.add(userDetails);
        userDetails.getTargetContacts().add(this);
        return this;
    }

    public UserDetails removeSourceContact(UserDetails userDetails) {
        this.sourceContacts.remove(userDetails);
        userDetails.getTargetContacts().remove(this);
        return this;
    }

    public void setSourceContacts(Set<UserDetails> userDetails) {
        if (this.sourceContacts != null) {
            this.sourceContacts.forEach(i -> i.removeTargetContact(this));
        }
        if (userDetails != null) {
            userDetails.forEach(i -> i.addTargetContact(this));
        }
        this.sourceContacts = userDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDetails)) {
            return false;
        }
        return id != null && id.equals(((UserDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDetails{" +
            "id=" + getId() +
            ", country='" + getCountry() + "'" +
            ", phone='" + getPhone() + "'" +
            ", apiKey='" + getApiKey() + "'" +
            ", notifications='" + getNotifications() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isTemporaryPassword='" + getIsTemporaryPassword() + "'" +
            "}";
    }
}
