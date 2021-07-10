package com.moneyroomba.service.criteria;

import com.moneyroomba.domain.enumeration.EventType;
import com.moneyroomba.domain.enumeration.SourceEntity;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.moneyroomba.domain.Event} entity. This class is used
 * in {@link com.moneyroomba.web.rest.EventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EventType
     */
    public static class EventTypeFilter extends Filter<EventType> {

        public EventTypeFilter() {}

        public EventTypeFilter(EventTypeFilter filter) {
            super(filter);
        }

        @Override
        public EventTypeFilter copy() {
            return new EventTypeFilter(this);
        }
    }

    /**
     * Class for filtering SourceEntity
     */
    public static class SourceEntityFilter extends Filter<SourceEntity> {

        public SourceEntityFilter() {}

        public SourceEntityFilter(SourceEntityFilter filter) {
            super(filter);
        }

        @Override
        public SourceEntityFilter copy() {
            return new SourceEntityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private EventTypeFilter eventType;

    private LocalDateFilter dateAdded;

    private LongFilter sourceId;

    private SourceEntityFilter sourceEntity;

    private StringFilter userName;

    private StringFilter userLastName;

    private LongFilter notificationId;

    private LongFilter userId;

    public EventCriteria() {}

    public EventCriteria(EventCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.eventType = other.eventType == null ? null : other.eventType.copy();
        this.dateAdded = other.dateAdded == null ? null : other.dateAdded.copy();
        this.sourceId = other.sourceId == null ? null : other.sourceId.copy();
        this.sourceEntity = other.sourceEntity == null ? null : other.sourceEntity.copy();
        this.userName = other.userName == null ? null : other.userName.copy();
        this.userLastName = other.userLastName == null ? null : other.userLastName.copy();
        this.notificationId = other.notificationId == null ? null : other.notificationId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public EventCriteria copy() {
        return new EventCriteria(this);
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

    public EventTypeFilter getEventType() {
        return eventType;
    }

    public EventTypeFilter eventType() {
        if (eventType == null) {
            eventType = new EventTypeFilter();
        }
        return eventType;
    }

    public void setEventType(EventTypeFilter eventType) {
        this.eventType = eventType;
    }

    public LocalDateFilter getDateAdded() {
        return dateAdded;
    }

    public LocalDateFilter dateAdded() {
        if (dateAdded == null) {
            dateAdded = new LocalDateFilter();
        }
        return dateAdded;
    }

    public void setDateAdded(LocalDateFilter dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LongFilter getSourceId() {
        return sourceId;
    }

    public LongFilter sourceId() {
        if (sourceId == null) {
            sourceId = new LongFilter();
        }
        return sourceId;
    }

    public void setSourceId(LongFilter sourceId) {
        this.sourceId = sourceId;
    }

    public SourceEntityFilter getSourceEntity() {
        return sourceEntity;
    }

    public SourceEntityFilter sourceEntity() {
        if (sourceEntity == null) {
            sourceEntity = new SourceEntityFilter();
        }
        return sourceEntity;
    }

    public void setSourceEntity(SourceEntityFilter sourceEntity) {
        this.sourceEntity = sourceEntity;
    }

    public StringFilter getUserName() {
        return userName;
    }

    public StringFilter userName() {
        if (userName == null) {
            userName = new StringFilter();
        }
        return userName;
    }

    public void setUserName(StringFilter userName) {
        this.userName = userName;
    }

    public StringFilter getUserLastName() {
        return userLastName;
    }

    public StringFilter userLastName() {
        if (userLastName == null) {
            userLastName = new StringFilter();
        }
        return userLastName;
    }

    public void setUserLastName(StringFilter userLastName) {
        this.userLastName = userLastName;
    }

    public LongFilter getNotificationId() {
        return notificationId;
    }

    public LongFilter notificationId() {
        if (notificationId == null) {
            notificationId = new LongFilter();
        }
        return notificationId;
    }

    public void setNotificationId(LongFilter notificationId) {
        this.notificationId = notificationId;
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
        final EventCriteria that = (EventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventType, that.eventType) &&
            Objects.equals(dateAdded, that.dateAdded) &&
            Objects.equals(sourceId, that.sourceId) &&
            Objects.equals(sourceEntity, that.sourceEntity) &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(userLastName, that.userLastName) &&
            Objects.equals(notificationId, that.notificationId) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventType, dateAdded, sourceId, sourceEntity, userName, userLastName, notificationId, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (eventType != null ? "eventType=" + eventType + ", " : "") +
            (dateAdded != null ? "dateAdded=" + dateAdded + ", " : "") +
            (sourceId != null ? "sourceId=" + sourceId + ", " : "") +
            (sourceEntity != null ? "sourceEntity=" + sourceEntity + ", " : "") +
            (userName != null ? "userName=" + userName + ", " : "") +
            (userLastName != null ? "userLastName=" + userLastName + ", " : "") +
            (notificationId != null ? "notificationId=" + notificationId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
