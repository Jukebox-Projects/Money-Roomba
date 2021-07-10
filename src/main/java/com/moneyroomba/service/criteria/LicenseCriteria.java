package com.moneyroomba.service.criteria;

import com.moneyroomba.domain.enumeration.LicenseCreateMethod;
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
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link com.moneyroomba.domain.License} entity. This class is used
 * in {@link com.moneyroomba.web.rest.LicenseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /licenses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LicenseCriteria implements Serializable, Criteria {

    /**
     * Class for filtering LicenseCreateMethod
     */
    public static class LicenseCreateMethodFilter extends Filter<LicenseCreateMethod> {

        public LicenseCreateMethodFilter() {}

        public LicenseCreateMethodFilter(LicenseCreateMethodFilter filter) {
            super(filter);
        }

        @Override
        public LicenseCreateMethodFilter copy() {
            return new LicenseCreateMethodFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter code;

    private BooleanFilter isAssigned;

    private BooleanFilter isActive;

    private LicenseCreateMethodFilter createMethod;

    private LongFilter invoiceId;

    public LicenseCriteria() {}

    public LicenseCriteria(LicenseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.isAssigned = other.isAssigned == null ? null : other.isAssigned.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.createMethod = other.createMethod == null ? null : other.createMethod.copy();
        this.invoiceId = other.invoiceId == null ? null : other.invoiceId.copy();
    }

    @Override
    public LicenseCriteria copy() {
        return new LicenseCriteria(this);
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

    public UUIDFilter getCode() {
        return code;
    }

    public UUIDFilter code() {
        if (code == null) {
            code = new UUIDFilter();
        }
        return code;
    }

    public void setCode(UUIDFilter code) {
        this.code = code;
    }

    public BooleanFilter getIsAssigned() {
        return isAssigned;
    }

    public BooleanFilter isAssigned() {
        if (isAssigned == null) {
            isAssigned = new BooleanFilter();
        }
        return isAssigned;
    }

    public void setIsAssigned(BooleanFilter isAssigned) {
        this.isAssigned = isAssigned;
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

    public LicenseCreateMethodFilter getCreateMethod() {
        return createMethod;
    }

    public LicenseCreateMethodFilter createMethod() {
        if (createMethod == null) {
            createMethod = new LicenseCreateMethodFilter();
        }
        return createMethod;
    }

    public void setCreateMethod(LicenseCreateMethodFilter createMethod) {
        this.createMethod = createMethod;
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
        final LicenseCriteria that = (LicenseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(isAssigned, that.isAssigned) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(createMethod, that.createMethod) &&
            Objects.equals(invoiceId, that.invoiceId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, isAssigned, isActive, createMethod, invoiceId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LicenseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (isAssigned != null ? "isAssigned=" + isAssigned + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (createMethod != null ? "createMethod=" + createMethod + ", " : "") +
            (invoiceId != null ? "invoiceId=" + invoiceId + ", " : "") +
            "}";
    }
}
