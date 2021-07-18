jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UserDetailsService } from '../service/user-details.service';
import { IUserDetails, UserDetails } from '../user-details.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ILicense } from 'app/entities/license/license.model';
import { LicenseService } from 'app/entities/license/service/license.service';

import { UserDetailsUpdateComponent } from './user-details-update.component';

describe('Component Tests', () => {
  describe('UserDetails Management Update Component', () => {
    let comp: UserDetailsUpdateComponent;
    let fixture: ComponentFixture<UserDetailsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let userDetailsService: UserDetailsService;
    let userService: UserService;
    let licenseService: LicenseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UserDetailsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UserDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserDetailsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      userDetailsService = TestBed.inject(UserDetailsService);
      userService = TestBed.inject(UserService);
      licenseService = TestBed.inject(LicenseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const userDetails: IUserDetails = { id: 456 };
        const internalUser: IUser = { id: 48561 };
        userDetails.internalUser = internalUser;

        const userCollection: IUser[] = [{ id: 28523 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [internalUser];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ userDetails });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call license query and add missing value', () => {
        const userDetails: IUserDetails = { id: 456 };
        const license: ILicense = { id: 74540 };
        userDetails.license = license;

        const licenseCollection: ILicense[] = [{ id: 85661 }];
        jest.spyOn(licenseService, 'query').mockReturnValue(of(new HttpResponse({ body: licenseCollection })));
        const expectedCollection: ILicense[] = [license, ...licenseCollection];
        jest.spyOn(licenseService, 'addLicenseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ userDetails });
        comp.ngOnInit();

        expect(licenseService.query).toHaveBeenCalled();
        expect(licenseService.addLicenseToCollectionIfMissing).toHaveBeenCalledWith(licenseCollection, license);
        expect(comp.licensesCollection).toEqual(expectedCollection);
      });

      it('Should call UserDetails query and add missing value', () => {
        const userDetails: IUserDetails = { id: 456 };
        const targetContacts: IUserDetails[] = [{ id: 14159 }];
        userDetails.targetContacts = targetContacts;
        const contact: IUserDetails = { id: 32723 };
        userDetails.contact = contact;

        const userDetailsCollection: IUserDetails[] = [{ id: 18271 }];
        jest.spyOn(userDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: userDetailsCollection })));
        const additionalUserDetails = [...targetContacts, contact];
        const expectedCollection: IUserDetails[] = [...additionalUserDetails, ...userDetailsCollection];
        jest.spyOn(userDetailsService, 'addUserDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ userDetails });
        comp.ngOnInit();

        expect(userDetailsService.query).toHaveBeenCalled();
        expect(userDetailsService.addUserDetailsToCollectionIfMissing).toHaveBeenCalledWith(
          userDetailsCollection,
          ...additionalUserDetails
        );
        expect(comp.userDetailsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const userDetails: IUserDetails = { id: 456 };
        const internalUser: IUser = { id: 76901 };
        userDetails.internalUser = internalUser;
        const license: ILicense = { id: 79450 };
        userDetails.license = license;
        const targetContacts: IUserDetails = { id: 43077 };
        userDetails.targetContacts = [targetContacts];
        const contact: IUserDetails = { id: 32695 };
        userDetails.contact = contact;

        activatedRoute.data = of({ userDetails });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(userDetails));
        expect(comp.usersSharedCollection).toContain(internalUser);
        expect(comp.licensesCollection).toContain(license);
        expect(comp.userDetailsSharedCollection).toContain(targetContacts);
        expect(comp.userDetailsSharedCollection).toContain(contact);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserDetails>>();
        const userDetails = { id: 123 };
        jest.spyOn(userDetailsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userDetails }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(userDetailsService.update).toHaveBeenCalledWith(userDetails);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserDetails>>();
        const userDetails = new UserDetails();
        jest.spyOn(userDetailsService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userDetails }));
        saveSubject.complete();

        // THEN
        expect(userDetailsService.create).toHaveBeenCalledWith(userDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserDetails>>();
        const userDetails = { id: 123 };
        jest.spyOn(userDetailsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(userDetailsService.update).toHaveBeenCalledWith(userDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLicenseById', () => {
        it('Should return tracked License primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLicenseById(0, entity);
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

    describe('Getting selected relationships', () => {
      describe('getSelectedUserDetails', () => {
        it('Should return option if no UserDetails is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedUserDetails(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected UserDetails for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedUserDetails(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this UserDetails is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedUserDetails(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
