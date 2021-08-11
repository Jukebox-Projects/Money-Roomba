package com.moneyroomba.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notification.
 */
@Entity
@Table(name = "t_notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    public Notification(LocalDate dateOpened, Boolean opened) {
        this.dateOpened = dateOpened;
        this.opened = opened;
    }

    public Notification() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_opened", nullable = false)
    private LocalDate dateOpened;

    @NotNull
    @Column(name = "opened", nullable = false)
    private Boolean opened;

    @JsonIgnoreProperties(value = { "notification", "user" }, allowSetters = true)
    @OneToOne(mappedBy = "notification")
    private Event event;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Notification id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDateOpened() {
        return this.dateOpened;
    }

    public Notification dateOpened(LocalDate dateOpened) {
        this.dateOpened = dateOpened;
        return this;
    }

    public void setDateOpened(LocalDate dateOpened) {
        this.dateOpened = dateOpened;
    }

    public Boolean getOpened() {
        return this.opened;
    }

    public Notification opened(Boolean opened) {
        this.opened = opened;
        return this;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }

    public Event getEvent() {
        return this.event;
    }

    public Notification event(Event event) {
        this.setEvent(event);
        return this;
    }

    public void setEvent(Event event) {
        if (this.event != null) {
            this.event.setNotification(null);
        }
        if (event != null) {
            event.setNotification(this);
        }
        this.event = event;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", dateOpened='" + getDateOpened() + "'" +
            ", opened='" + getOpened() + "'" +
            "}";
    }
}
