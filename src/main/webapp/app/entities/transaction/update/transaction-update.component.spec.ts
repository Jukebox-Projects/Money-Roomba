jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TransactionService } from '../service/transaction.service';
import { ITransaction, Transaction } from '../transaction.model';
import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { WalletService } from 'app/entities/wallet/service/wallet.service';
import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';

import { TransactionUpdateComponent } from './transaction-update.component';

describe('Component Tests', () => {
  describe('Transaction Management Update Component', () => {
    let comp: TransactionUpdateComponent;
    let fixture: ComponentFixture<TransactionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let transactionService: TransactionService;
    let attachmentService: AttachmentService;
    let walletService: WalletService;
    let currencyService: CurrencyService;
    let categoryService: CategoryService;
    let userDetailsService: UserDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TransactionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TransactionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TransactionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      transactionService = TestBed.inject(TransactionService);
      attachmentService = TestBed.inject(AttachmentService);
      walletService = TestBed.inject(WalletService);
      currencyService = TestBed.inject(CurrencyService);
      categoryService = TestBed.inject(CategoryService);
      userDetailsService = TestBed.inject(UserDetailsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call attachment query and add missing value', () => {
        const transaction: ITransaction = { id: 456 };
        const attachment: IAttachment = { id: 25543 };
        transaction.attachment = attachment;

        const attachmentCollection: IAttachment[] = [{ id: 16281 }];
        jest.spyOn(attachmentService, 'query').mockReturnValue(of(new HttpResponse({ body: attachmentCollection })));
        const expectedCollection: IAttachment[] = [attachment, ...attachmentCollection];
        jest.spyOn(attachmentService, 'addAttachmentToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        expect(attachmentService.query).toHaveBeenCalled();
        expect(attachmentService.addAttachmentToCollectionIfMissing).toHaveBeenCalledWith(attachmentCollection, attachment);
        expect(comp.attachmentsCollection).toEqual(expectedCollection);
      });

      it('Should call Wallet query and add missing value', () => {
        const transaction: ITransaction = { id: 456 };
        const wallet: IWallet = { id: 31777 };
        transaction.wallet = wallet;

        const walletCollection: IWallet[] = [{ id: 6866 }];
        jest.spyOn(walletService, 'query').mockReturnValue(of(new HttpResponse({ body: walletCollection })));
        const additionalWallets = [wallet];
        const expectedCollection: IWallet[] = [...additionalWallets, ...walletCollection];
        jest.spyOn(walletService, 'addWalletToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        expect(walletService.query).toHaveBeenCalled();
        expect(walletService.addWalletToCollectionIfMissing).toHaveBeenCalledWith(walletCollection, ...additionalWallets);
        expect(comp.walletsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Currency query and add missing value', () => {
        const transaction: ITransaction = { id: 456 };
        const currency: ICurrency = { id: 82506 };
        transaction.currency = currency;

        const currencyCollection: ICurrency[] = [{ id: 86273 }];
        jest.spyOn(currencyService, 'query').mockReturnValue(of(new HttpResponse({ body: currencyCollection })));
        const additionalCurrencies = [currency];
        const expectedCollection: ICurrency[] = [...additionalCurrencies, ...currencyCollection];
        jest.spyOn(currencyService, 'addCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        expect(currencyService.query).toHaveBeenCalled();
        expect(currencyService.addCurrencyToCollectionIfMissing).toHaveBeenCalledWith(currencyCollection, ...additionalCurrencies);
        expect(comp.currenciesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Category query and add missing value', () => {
        const transaction: ITransaction = { id: 456 };
        const category: ICategory = { id: 51552 };
        transaction.category = category;

        const categoryCollection: ICategory[] = [{ id: 84170 }];
        jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
        const additionalCategories = [category];
        const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
        jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        expect(categoryService.query).toHaveBeenCalled();
        expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(categoryCollection, ...additionalCategories);
        expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call UserDetails query and add missing value', () => {
        const transaction: ITransaction = { id: 456 };
        const sourceUser: IUserDetails = { id: 48093 };
        transaction.sourceUser = sourceUser;

        const userDetailsCollection: IUserDetails[] = [{ id: 88273 }];
        jest.spyOn(userDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: userDetailsCollection })));
        const additionalUserDetails = [sourceUser];
        const expectedCollection: IUserDetails[] = [...additionalUserDetails, ...userDetailsCollection];
        jest.spyOn(userDetailsService, 'addUserDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        expect(userDetailsService.query).toHaveBeenCalled();
        expect(userDetailsService.addUserDetailsToCollectionIfMissing).toHaveBeenCalledWith(
          userDetailsCollection,
          ...additionalUserDetails
        );
        expect(comp.userDetailsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const transaction: ITransaction = { id: 456 };
        const attachment: IAttachment = { id: 59527 };
        transaction.attachment = attachment;
        const wallet: IWallet = { id: 22721 };
        transaction.wallet = wallet;
        const currency: ICurrency = { id: 82812 };
        transaction.currency = currency;
        const category: ICategory = { id: 23686 };
        transaction.category = category;
        const sourceUser: IUserDetails = { id: 87378 };
        transaction.sourceUser = sourceUser;

        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(transaction));
        expect(comp.attachmentsCollection).toContain(attachment);
        expect(comp.walletsSharedCollection).toContain(wallet);
        expect(comp.currenciesSharedCollection).toContain(currency);
        expect(comp.categoriesSharedCollection).toContain(category);
        expect(comp.userDetailsSharedCollection).toContain(sourceUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Transaction>>();
        const transaction = { id: 123 };
        jest.spyOn(transactionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: transaction }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(transactionService.update).toHaveBeenCalledWith(transaction);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Transaction>>();
        const transaction = new Transaction();
        jest.spyOn(transactionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: transaction }));
        saveSubject.complete();

        // THEN
        expect(transactionService.create).toHaveBeenCalledWith(transaction);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Transaction>>();
        const transaction = { id: 123 };
        jest.spyOn(transactionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(transactionService.update).toHaveBeenCalledWith(transaction);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAttachmentById', () => {
        it('Should return tracked Attachment primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAttachmentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackWalletById', () => {
        it('Should return tracked Wallet primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackWalletById(0, entity);
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

      describe('trackCategoryById', () => {
        it('Should return tracked Category primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCategoryById(0, entity);
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
