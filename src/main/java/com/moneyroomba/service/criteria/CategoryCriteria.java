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
 * Criteria class for the {@link com.moneyroomba.domain.Category} entity. This class is used
 * in {@link com.moneyroomba.web.rest.CategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BooleanFilter isActive;

    private BooleanFilter userCreated;

    private IntegerFilter icon;

    private LongFilter categoryId;

    private LongFilter transactionId;

    private LongFilter scheduledTransactionId;

    private LongFilter parentId;

    private LongFilter userId;

    public CategoryCriteria() {}

    public CategoryCriteria(CategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.userCreated = other.userCreated == null ? null : other.userCreated.copy();
        this.icon = other.icon == null ? null : other.icon.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.transactionId = other.transactionId == null ? null : other.transactionId.copy();
        this.scheduledTransactionId = other.scheduledTransactionId == null ? null : other.scheduledTransactionId.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public CategoryCriteria copy() {
        return new CategoryCriteria(this);
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

    public BooleanFilter getUserCreated() {
        return userCreated;
    }

    public BooleanFilter userCreated() {
        if (userCreated == null) {
            userCreated = new BooleanFilter();
        }
        return userCreated;
    }

    public void setUserCreated(BooleanFilter userCreated) {
        this.userCreated = userCreated;
    }

    public IntegerFilter getIcon() {
        return icon;
    }

    public IntegerFilter icon() {
        if (icon == null) {
            icon = new IntegerFilter();
        }
        return icon;
    }

    public void setIcon(IntegerFilter icon) {
        this.icon = icon;
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

    public LongFilter getTransactionId() {
        return transactionId;
    }

    public LongFilter transactionId() {
        if (transactionId == null) {
            transactionId = new LongFilter();
        }
        return transactionId;
    }

    public void setTransactionId(LongFilter transactionId) {
        this.transactionId = transactionId;
    }

    public LongFilter getScheduledTransactionId() {
        return scheduledTransactionId;
    }

    public LongFilter scheduledTransactionId() {
        if (scheduledTransactionId == null) {
            scheduledTransactionId = new LongFilter();
        }
        return scheduledTransactionId;
    }

    public void setScheduledTransactionId(LongFilter scheduledTransactionId) {
        this.scheduledTransactionId = scheduledTransactionId;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public LongFilter parentId() {
        if (parentId == null) {
            parentId = new LongFilter();
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CategoryCriteria that = (CategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(userCreated, that.userCreated) &&
            Objects.equals(icon, that.icon) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(scheduledTransactionId, that.scheduledTransactionId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isActive, userCreated, icon, categoryId, transactionId, scheduledTransactionId, parentId, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (userCreated != null ? "userCreated=" + userCreated + ", " : "") +
            (icon != null ? "icon=" + icon + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
            (scheduledTransactionId != null ? "scheduledTransactionId=" + scheduledTransactionId + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
