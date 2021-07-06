jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ScheduledTransactionService } from '../service/scheduled-transaction.service';
import { IScheduledTransaction, ScheduledTransaction } from '../scheduled-transaction.model';
import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';

import { ScheduledTransactionUpdateComponent } from './scheduled-transaction-update.component';

describe('Component Tests', () => {
  describe('ScheduledTransaction Management Update Component', () => {
    let comp: ScheduledTransactionUpdateComponent;
    let fixture: ComponentFixture<ScheduledTransactionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let scheduledTransactionService: ScheduledTransactionService;
    let currencyService: CurrencyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ScheduledTransactionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ScheduledTransactionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ScheduledTransactionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      scheduledTransactionService = TestBed.inject(ScheduledTransactionService);
      currencyService = TestBed.inject(CurrencyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Currency query and add missing value', () => {
        const scheduledTransaction: IScheduledTransaction = { id: 456 };
        const currency: ICurrency = { id: 59130 };
        scheduledTransaction.currency = currency;

        const currencyCollection: ICurrency[] = [{ id: 4834 }];
        jest.spyOn(currencyService, 'query').mockReturnValue(of(new HttpResponse({ body: currencyCollection })));
        const additionalCurrencies = [currency];
        const expectedCollection: ICurrency[] = [...additionalCurrencies, ...currencyCollection];
        jest.spyOn(currencyService, 'addCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ scheduledTransaction });
        comp.ngOnInit();

        expect(currencyService.query).toHaveBeenCalled();
        expect(currencyService.addCurrencyToCollectionIfMissing).toHaveBeenCalledWith(currencyCollection, ...additionalCurrencies);
        expect(comp.currenciesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const scheduledTransaction: IScheduledTransaction = { id: 456 };
        const currency: ICurrency = { id: 31698 };
        scheduledTransaction.currency = currency;

        activatedRoute.data = of({ scheduledTransaction });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(scheduledTransaction));
        expect(comp.currenciesSharedCollection).toContain(currency);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ScheduledTransaction>>();
        const scheduledTransaction = { id: 123 };
        jest.spyOn(scheduledTransactionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ scheduledTransaction });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: scheduledTransaction }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(scheduledTransactionService.update).toHaveBeenCalledWith(scheduledTransaction);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ScheduledTransaction>>();
        const scheduledTransaction = new ScheduledTransaction();
        jest.spyOn(scheduledTransactionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ scheduledTransaction });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: scheduledTransaction }));
        saveSubject.complete();

        // THEN
        expect(scheduledTransactionService.create).toHaveBeenCalledWith(scheduledTransaction);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ScheduledTransaction>>();
        const scheduledTransaction = { id: 123 };
        jest.spyOn(scheduledTransactionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ scheduledTransaction });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(scheduledTransactionService.update).toHaveBeenCalledWith(scheduledTransaction);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCurrencyById', () => {
        it('Should return tracked Currency primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCurrencyById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
