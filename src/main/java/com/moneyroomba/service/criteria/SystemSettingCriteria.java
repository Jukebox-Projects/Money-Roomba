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
 * Criteria class for the {@link com.moneyroomba.domain.SystemSetting} entity. This class is used
 * in {@link com.moneyroomba.web.rest.SystemSettingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /system-settings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SystemSettingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter key;

    private DoubleFilter value;

    public SystemSettingCriteria() {}

    public SystemSettingCriteria(SystemSettingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.key = other.key == null ? null : other.key.copy();
        this.value = other.value == null ? null : other.value.copy();
    }

    @Override
    public SystemSettingCriteria copy() {
        return new SystemSettingCriteria(this);
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

    public StringFilter getKey() {
        return key;
    }

    public StringFilter key() {
        if (key == null) {
            key = new StringFilter();
        }
        return key;
    }

    public void setKey(StringFilter key) {
        this.key = key;
    }

    public DoubleFilter getValue() {
        return value;
    }

    public DoubleFilter value() {
        if (value == null) {
            value = new DoubleFilter();
        }
        return value;
    }

    public void setValue(DoubleFilter value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SystemSettingCriteria that = (SystemSettingCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(key, that.key) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key, value);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemSettingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (key != null ? "key=" + key + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            "}";
    }
}
