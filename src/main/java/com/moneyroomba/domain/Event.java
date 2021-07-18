package com.moneyroomba.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moneyroomba.domain.enumeration.EventType;
import com.moneyroomba.domain.enumeration.SourceEntity;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @NotNull
    @Column(name = "date_added", nullable = false)
    private LocalDate dateAdded;

    @NotNull
    @Column(name = "source_id", nullable = false)
    private Long sourceId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "source_entity", nullable = false)
    private SourceEntity sourceEntity;

    @NotNull
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotNull
    @Column(name = "user_last_name", nullable = false)
    private String userLastName;

    @JsonIgnoreProperties(value = { "event" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Notification notification;

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
    private UserDetails user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event id(Long id) {
        this.id = id;
        return this;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public Event eventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public Event dateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Long getSourceId() {
        return this.sourceId;
    }

    public Event sourceId(Long sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public SourceEntity getSourceEntity() {
        return this.sourceEntity;
    }

    public Event sourceEntity(SourceEntity sourceEntity) {
        this.sourceEntity = sourceEntity;
        return this;
    }

    public void setSourceEntity(SourceEntity sourceEntity) {
        this.sourceEntity = sourceEntity;
    }

    public String getUserName() {
        return this.userName;
    }

    public Event userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLastName() {
        return this.userLastName;
    }

    public Event userLastName(String userLastName) {
        this.userLastName = userLastName;
        return this;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public Notification getNotification() {
        return this.notification;
    }

    public Event notification(Notification notification) {
        this.setNotification(notification);
        return this;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public UserDetails getUser() {
        return this.user;
    }

    public Event user(UserDetails userDetails) {
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
        if (!(o instanceof Event)) {
            return false;
        }
        return id != null && id.equals(((Event) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", eventType='" + getEventType() + "'" +
            ", dateAdded='" + getDateAdded() + "'" +
            ", sourceId=" + getSourceId() +
            ", sourceEntity='" + getSourceEntity() + "'" +
            ", userName='" + getUserName() + "'" +
            ", userLastName='" + getUserLastName() + "'" +
            "}";
    }
}
