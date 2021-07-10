jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EventService } from '../service/event.service';
import { IEvent, Event } from '../event.model';
import { INotification } from 'app/entities/notification/notification.model';
import { NotificationService } from 'app/entities/notification/service/notification.service';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';

import { EventUpdateComponent } from './event-update.component';

describe('Component Tests', () => {
  describe('Event Management Update Component', () => {
    let comp: EventUpdateComponent;
    let fixture: ComponentFixture<EventUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let eventService: EventService;
    let notificationService: NotificationService;
    let userDetailsService: UserDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EventUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EventUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EventUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      eventService = TestBed.inject(EventService);
      notificationService = TestBed.inject(NotificationService);
      userDetailsService = TestBed.inject(UserDetailsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call notification query and add missing value', () => {
        const event: IEvent = { id: 456 };
        const notification: INotification = { id: 70918 };
        event.notification = notification;

        const notificationCollection: INotification[] = [{ id: 47303 }];
        jest.spyOn(notificationService, 'query').mockReturnValue(of(new HttpResponse({ body: notificationCollection })));
        const expectedCollection: INotification[] = [notification, ...notificationCollection];
        jest.spyOn(notificationService, 'addNotificationToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ event });
        comp.ngOnInit();

        expect(notificationService.query).toHaveBeenCalled();
        expect(notificationService.addNotificationToCollectionIfMissing).toHaveBeenCalledWith(notificationCollection, notification);
        expect(comp.notificationsCollection).toEqual(expectedCollection);
      });

      it('Should call UserDetails query and add missing value', () => {
        const event: IEvent = { id: 456 };
        const user: IUserDetails = { id: 71463 };
        event.user = user;

        const userDetailsCollection: IUserDetails[] = [{ id: 88592 }];
        jest.spyOn(userDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: userDetailsCollection })));
        const additionalUserDetails = [user];
        const expectedCollection: IUserDetails[] = [...additionalUserDetails, ...userDetailsCollection];
        jest.spyOn(userDetailsService, 'addUserDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ event });
        comp.ngOnInit();

        expect(userDetailsService.query).toHaveBeenCalled();
        expect(userDetailsService.addUserDetailsToCollectionIfMissing).toHaveBeenCalledWith(
          userDetailsCollection,
          ...additionalUserDetails
        );
        expect(comp.userDetailsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const event: IEvent = { id: 456 };
        const notification: INotification = { id: 55478 };
        event.notification = notification;
        const user: IUserDetails = { id: 11079 };
        event.user = user;

        activatedRoute.data = of({ event });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(event));
        expect(comp.notificationsCollection).toContain(notification);
        expect(comp.userDetailsSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Event>>();
        const event = { id: 123 };
        jest.spyOn(eventService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ event });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: event }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(eventService.update).toHaveBeenCalledWith(event);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Event>>();
        const event = new Event();
        jest.spyOn(eventService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ event });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: event }));
        saveSubject.complete();

        // THEN
        expect(eventService.create).toHaveBeenCalledWith(event);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Event>>();
        const event = { id: 123 };
        jest.spyOn(eventService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ event });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(eventService.update).toHaveBeenCalledWith(event);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackNotificationById', () => {
        it('Should return tracked Notification primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackNotificationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserDetailsById', () => {
        it('Should return tracked UserDetails primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserDetailsById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
