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
 * Criteria class for the {@link com.moneyroomba.domain.Icon} entity. This class is used
 * in {@link com.moneyroomba.web.rest.IconResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /icons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IconCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter url;

    private StringFilter name;

    private LongFilter categoryId;

    private LongFilter walletId;

    public IconCriteria() {}

    public IconCriteria(IconCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.walletId = other.walletId == null ? null : other.walletId.copy();
    }

    @Override
    public IconCriteria copy() {
        return new IconCriteria(this);
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

    public StringFilter getUrl() {
        return url;
    }

    public StringFilter url() {
        if (url == null) {
            url = new StringFilter();
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getWalletId() {
        return walletId;
    }

    public LongFilter walletId() {
        if (walletId == null) {
            walletId = new LongFilter();
        }
        return walletId;
    }

    public void setWalletId(LongFilter walletId) {
        this.walletId = walletId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IconCriteria that = (IconCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(url, that.url) &&
            Objects.equals(name, that.name) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(walletId, that.walletId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, name, categoryId, walletId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IconCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (walletId != null ? "walletId=" + walletId + ", " : "") +
            "}";
    }
}
