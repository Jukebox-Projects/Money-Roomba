package com.moneyroomba.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moneyroomba.domain.enumeration.LicenseCreateMethod;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A License.
 */
@Entity
@Table(name = "t_license")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class License implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Type(type = "uuid-char")
    @Column(name = "code", length = 36, nullable = false, unique = true)
    private UUID code;

    @NotNull
    @Column(name = "is_assigned", nullable = false)
    private Boolean isAssigned;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "create_method", nullable = false)
    private LicenseCreateMethod createMethod;

    @JsonIgnoreProperties(value = { "currency" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public License id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getCode() {
        return this.code;
    }

    public License code(UUID code) {
        this.code = code;
        return this;
    }

    public void setCode(UUID code) {
        this.code = code;
    }

    public Boolean getIsAssigned() {
        return this.isAssigned;
    }

    public License isAssigned(Boolean isAssigned) {
        this.isAssigned = isAssigned;
        return this;
    }

    public void setIsAssigned(Boolean isAssigned) {
        this.isAssigned = isAssigned;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public License isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LicenseCreateMethod getCreateMethod() {
        return this.createMethod;
    }

    public License createMethod(LicenseCreateMethod createMethod) {
        this.createMethod = createMethod;
        return this;
    }

    public void setCreateMethod(LicenseCreateMethod createMethod) {
        this.createMethod = createMethod;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public License invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof License)) {
            return false;
        }
        return id != null && id.equals(((License) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "License{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", isAssigned='" + getIsAssigned() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createMethod='" + getCreateMethod() + "'" +
            "}";
    }
}
