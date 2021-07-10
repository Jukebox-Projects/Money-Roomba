jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SchedulePatternService } from '../service/schedule-pattern.service';
import { ISchedulePattern, SchedulePattern } from '../schedule-pattern.model';
import { IScheduledTransaction } from 'app/entities/scheduled-transaction/scheduled-transaction.model';
import { ScheduledTransactionService } from 'app/entities/scheduled-transaction/service/scheduled-transaction.service';

import { SchedulePatternUpdateComponent } from './schedule-pattern-update.component';

describe('Component Tests', () => {
  describe('SchedulePattern Management Update Component', () => {
    let comp: SchedulePatternUpdateComponent;
    let fixture: ComponentFixture<SchedulePatternUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let schedulePatternService: SchedulePatternService;
    let scheduledTransactionService: ScheduledTransactionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SchedulePatternUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SchedulePatternUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SchedulePatternUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      schedulePatternService = TestBed.inject(SchedulePatternService);
      scheduledTransactionService = TestBed.inject(ScheduledTransactionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ScheduledTransaction query and add missing value', () => {
        const schedulePattern: ISchedulePattern = { id: 456 };
        const scheduleTransaction: IScheduledTransaction = { id: 22732 };
        schedulePattern.scheduleTransaction = scheduleTransaction;

        const scheduledTransactionCollection: IScheduledTransaction[] = [{ id: 27778 }];
        jest.spyOn(scheduledTransactionService, 'query').mockReturnValue(of(new HttpResponse({ body: scheduledTransactionCollection })));
        const additionalScheduledTransactions = [scheduleTransaction];
        const expectedCollection: IScheduledTransaction[] = [...additionalScheduledTransactions, ...scheduledTransactionCollection];
        jest.spyOn(scheduledTransactionService, 'addScheduledTransactionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ schedulePattern });
        comp.ngOnInit();

        expect(scheduledTransactionService.query).toHaveBeenCalled();
        expect(scheduledTransactionService.addScheduledTransactionToCollectionIfMissing).toHaveBeenCalledWith(
          scheduledTransactionCollection,
          ...additionalScheduledTransactions
        );
        expect(comp.scheduledTransactionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const schedulePattern: ISchedulePattern = { id: 456 };
        const scheduleTransaction: IScheduledTransaction = { id: 77557 };
        schedulePattern.scheduleTransaction = scheduleTransaction;

        activatedRoute.data = of({ schedulePattern });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(schedulePattern));
        expect(comp.scheduledTransactionsSharedCollection).toContain(scheduleTransaction);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SchedulePattern>>();
        const schedulePattern = { id: 123 };
        jest.spyOn(schedulePatternService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ schedulePattern });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: schedulePattern }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(schedulePatternService.update).toHaveBeenCalledWith(schedulePattern);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SchedulePattern>>();
        const schedulePattern = new SchedulePattern();
        jest.spyOn(schedulePatternService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ schedulePattern });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: schedulePattern }));
        saveSubject.complete();

        // THEN
        expect(schedulePatternService.create).toHaveBeenCalledWith(schedulePattern);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SchedulePattern>>();
        const schedulePattern = { id: 123 };
        jest.spyOn(schedulePatternService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ schedulePattern });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(schedulePatternService.update).toHaveBeenCalledWith(schedulePattern);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackScheduledTransactionById', () => {
        it('Should return tracked ScheduledTransaction primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackScheduledTransactionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
