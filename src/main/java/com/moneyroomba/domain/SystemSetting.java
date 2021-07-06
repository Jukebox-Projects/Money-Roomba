package com.moneyroomba.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SystemSetting.
 */
@Entity
@Table(name = "system_setting")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SystemSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_key", nullable = false)
    private String key;

    @NotNull
    @Column(name = "value", nullable = false)
    private Double value;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SystemSetting id(Long id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return this.key;
    }

    public SystemSetting key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Double getValue() {
        return this.value;
    }

    public SystemSetting value(Double value) {
        this.value = value;
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemSetting)) {
            return false;
        }
        return id != null && id.equals(((SystemSetting) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemSetting{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
