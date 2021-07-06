jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InvoiceService } from '../service/invoice.service';
import { IInvoice, Invoice } from '../invoice.model';
import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';

import { InvoiceUpdateComponent } from './invoice-update.component';

describe('Component Tests', () => {
  describe('Invoice Management Update Component', () => {
    let comp: InvoiceUpdateComponent;
    let fixture: ComponentFixture<InvoiceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let invoiceService: InvoiceService;
    let currencyService: CurrencyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InvoiceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(InvoiceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InvoiceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      invoiceService = TestBed.inject(InvoiceService);
      currencyService = TestBed.inject(CurrencyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Currency query and add missing value', () => {
        const invoice: IInvoice = { id: 456 };
        const currency: ICurrency = { id: 45338 };
        invoice.currency = currency;

        const currencyCollection: ICurrency[] = [{ id: 8252 }];
        jest.spyOn(currencyService, 'query').mockReturnValue(of(new HttpResponse({ body: currencyCollection })));
        const additionalCurrencies = [currency];
        const expectedCollection: ICurrency[] = [...additionalCurrencies, ...currencyCollection];
        jest.spyOn(currencyService, 'addCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(currencyService.query).toHaveBeenCalled();
        expect(currencyService.addCurrencyToCollectionIfMissing).toHaveBeenCalledWith(currencyCollection, ...additionalCurrencies);
        expect(comp.currenciesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const invoice: IInvoice = { id: 456 };
        const currency: ICurrency = { id: 35605 };
        invoice.currency = currency;

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(invoice));
        expect(comp.currenciesSharedCollection).toContain(currency);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Invoice>>();
        const invoice = { id: 123 };
        jest.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: invoice }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(invoiceService.update).toHaveBeenCalledWith(invoice);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Invoice>>();
        const invoice = new Invoice();
        jest.spyOn(invoiceService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: invoice }));
        saveSubject.complete();

        // THEN
        expect(invoiceService.create).toHaveBeenCalledWith(invoice);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Invoice>>();
        const invoice = { id: 123 };
        jest.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(invoiceService.update).toHaveBeenCalledWith(invoice);
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
