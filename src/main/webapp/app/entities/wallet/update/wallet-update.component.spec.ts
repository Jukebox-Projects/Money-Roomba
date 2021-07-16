jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WalletService } from '../service/wallet.service';
import { IWallet, Wallet } from '../wallet.model';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';

import { WalletUpdateComponent } from './wallet-update.component';

describe('Component Tests', () => {
  describe('Wallet Management Update Component', () => {
    let comp: WalletUpdateComponent;
    let fixture: ComponentFixture<WalletUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let walletService: WalletService;
    let userDetailsService: UserDetailsService;
    let currencyService: CurrencyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WalletUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WalletUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WalletUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      walletService = TestBed.inject(WalletService);
      userDetailsService = TestBed.inject(UserDetailsService);
      currencyService = TestBed.inject(CurrencyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call UserDetails query and add missing value', () => {
        const wallet: IWallet = { id: 456 };
        const user: IUserDetails = { id: 77018 };
        wallet.user = user;

        const userDetailsCollection: IUserDetails[] = [{ id: 96844 }];
        jest.spyOn(userDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: userDetailsCollection })));
        const additionalUserDetails = [user];
        const expectedCollection: IUserDetails[] = [...additionalUserDetails, ...userDetailsCollection];
        jest.spyOn(userDetailsService, 'addUserDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ wallet });
        comp.ngOnInit();

        expect(userDetailsService.query).toHaveBeenCalled();
        expect(userDetailsService.addUserDetailsToCollectionIfMissing).toHaveBeenCalledWith(
          userDetailsCollection,
          ...additionalUserDetails
        );
        expect(comp.userDetailsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Currency query and add missing value', () => {
        const wallet: IWallet = { id: 456 };
        const currency: ICurrency = { id: 24490 };
        wallet.currency = currency;

        const currencyCollection: ICurrency[] = [{ id: 96 }];
        jest.spyOn(currencyService, 'query').mockReturnValue(of(new HttpResponse({ body: currencyCollection })));
        const additionalCurrencies = [currency];
        const expectedCollection: ICurrency[] = [...additionalCurrencies, ...currencyCollection];
        jest.spyOn(currencyService, 'addCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ wallet });
        comp.ngOnInit();

        expect(currencyService.query).toHaveBeenCalled();
        expect(currencyService.addCurrencyToCollectionIfMissing).toHaveBeenCalledWith(currencyCollection, ...additionalCurrencies);
        expect(comp.currenciesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const wallet: IWallet = { id: 456 };
        const user: IUserDetails = { id: 30871 };
        wallet.user = user;
        const currency: ICurrency = { id: 29149 };
        wallet.currency = currency;

        activatedRoute.data = of({ wallet });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(wallet));
        expect(comp.userDetailsSharedCollection).toContain(user);
        expect(comp.currenciesSharedCollection).toContain(currency);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Wallet>>();
        const wallet = { id: 123 };
        jest.spyOn(walletService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ wallet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: wallet }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(walletService.update).toHaveBeenCalledWith(wallet);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Wallet>>();
        const wallet = new Wallet();
        jest.spyOn(walletService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ wallet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: wallet }));
        saveSubject.complete();

        // THEN
        expect(walletService.create).toHaveBeenCalledWith(wallet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Wallet>>();
        const wallet = { id: 123 };
        jest.spyOn(walletService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ wallet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(walletService.update).toHaveBeenCalledWith(wallet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserDetailsById', () => {
        it('Should return tracked UserDetails primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserDetailsById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

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
