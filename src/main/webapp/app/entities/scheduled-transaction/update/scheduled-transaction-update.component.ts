import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IScheduledTransaction, ScheduledTransaction } from '../scheduled-transaction.model';
import { ScheduledTransactionService } from '../service/scheduled-transaction.service';
import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { WalletService } from 'app/entities/wallet/service/wallet.service';

@Component({
  selector: 'jhi-scheduled-transaction-update',
  templateUrl: './scheduled-transaction-update.component.html',
})
export class ScheduledTransactionUpdateComponent implements OnInit {
  isSaving = false;

  currenciesSharedCollection: ICurrency[] = [];
  userDetailsSharedCollection: IUserDetails[] = [];
  categoriesSharedCollection: ICategory[] = [];
  walletsSharedCollection: IWallet[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    originalAmount: [null, [Validators.required, Validators.min(0)]],
    movementType: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [],
    addToReports: [null, [Validators.required]],
    recurringType: [null, [Validators.required]],
    separationCount: [null, [Validators.min(0)]],
    maxNumberOfOcurrences: [],
    dayOfWeek: [null, [Validators.min(0), Validators.max(6)]],
    weekOfMonth: [null, [Validators.min(0), Validators.max(5)]],
    dayOfMonth: [null, [Validators.min(0), Validators.max(31)]],
    monthOfYear: [null, [Validators.min(0), Validators.max(11)]],
    currency: [],
    sourceUser: [],
    category: [],
    wallet: [null, [Validators.required]],
  });

  constructor(
    protected scheduledTransactionService: ScheduledTransactionService,
    protected currencyService: CurrencyService,
    protected userDetailsService: UserDetailsService,
    protected walletService: WalletService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scheduledTransaction }) => {
      this.updateForm(scheduledTransaction);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const scheduledTransaction = this.createFromForm();
    if (scheduledTransaction.id !== undefined) {
      this.subscribeToSaveResponse(this.scheduledTransactionService.update(scheduledTransaction));
    } else {
      this.subscribeToSaveResponse(this.scheduledTransactionService.create(scheduledTransaction));
    }
  }

  trackCurrencyById(index: number, item: ICurrency): number {
    return item.id!;
  }

  trackUserDetailsById(index: number, item: IUserDetails): number {
    return item.id!;
  }

  trackWalletById(index: number, item: IWallet): number {
    return item.id!;
  }

  trackCategoryById(index: number, item: ICategory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IScheduledTransaction>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(scheduledTransaction: IScheduledTransaction): void {
    this.editForm.patchValue({
      id: scheduledTransaction.id,
      name: scheduledTransaction.name,
      description: scheduledTransaction.description,
      originalAmount: scheduledTransaction.originalAmount,
      movementType: scheduledTransaction.movementType,
      startDate: scheduledTransaction.startDate,
      endDate: scheduledTransaction.endDate,
      addToReports: scheduledTransaction.addToReports,
      recurringType: scheduledTransaction.recurringType,
      separationCount: scheduledTransaction.separationCount,
      maxNumberOfOcurrences: scheduledTransaction.maxNumberOfOcurrences,
      dayOfWeek: scheduledTransaction.dayOfWeek,
      weekOfMonth: scheduledTransaction.weekOfMonth,
      dayOfMonth: scheduledTransaction.dayOfMonth,
      monthOfYear: scheduledTransaction.monthOfYear,
      currency: scheduledTransaction.currency,
      sourceUser: scheduledTransaction.sourceUser,
      category: scheduledTransaction.category,
      wallet: scheduledTransaction.wallet,
    });

    this.currenciesSharedCollection = this.currencyService.addCurrencyToCollectionIfMissing(
      this.currenciesSharedCollection,
      scheduledTransaction.currency
    );
    this.userDetailsSharedCollection = this.userDetailsService.addUserDetailsToCollectionIfMissing(
      this.userDetailsSharedCollection,
      scheduledTransaction.sourceUser
    );
    this.walletsSharedCollection = this.walletService.addWalletToCollectionIfMissing(
      this.walletsSharedCollection,
      scheduledTransaction.wallet
    );
    this.currenciesSharedCollection = this.currencyService.addCurrencyToCollectionIfMissing(
      this.currenciesSharedCollection,
      scheduledTransaction.currency
    );
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(
      this.categoriesSharedCollection,
      scheduledTransaction.category
    );
  }

  protected loadRelationshipsOptions(): void {
    this.currencyService
      .query()
      .pipe(map((res: HttpResponse<ICurrency[]>) => res.body ?? []))
      .pipe(
        map((currencies: ICurrency[]) =>
          this.currencyService.addCurrencyToCollectionIfMissing(currencies, this.editForm.get('currency')!.value)
        )
      )
      .subscribe((currencies: ICurrency[]) => (this.currenciesSharedCollection = currencies));

    this.walletService
      .query()
      .pipe(map((res: HttpResponse<IWallet[]>) => res.body ?? []))
      .pipe(map((wallets: IWallet[]) => this.walletService.addWalletToCollectionIfMissing(wallets, this.editForm.get('wallet')!.value)))
      .subscribe((wallets: IWallet[]) => (this.walletsSharedCollection = wallets));

    this.userDetailsService
      .query()
      .pipe(map((res: HttpResponse<IUserDetails[]>) => res.body ?? []))
      .pipe(
        map((userDetails: IUserDetails[]) =>
          this.userDetailsService.addUserDetailsToCollectionIfMissing(userDetails, this.editForm.get('sourceUser')!.value)
        )
      )
      .subscribe((userDetails: IUserDetails[]) => (this.userDetailsSharedCollection = userDetails));

    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));
  }

  isDaily(): Boolean {
    if (this.editForm.get(['recurringType'])!.value !== 'DAILY') {
      return false;
    } else {
      return true;
    }
  }

  isWeekly(): Boolean {
    if (this.editForm.get(['recurringType'])!.value !== 'WEEKLY') {
      return false;
    } else {
      return true;
    }
  }

  isMonthly(): Boolean {
    if (this.editForm.get(['recurringType'])!.value !== 'MONTHLY') {
      return false;
    } else {
      return true;
    }
  }

  isYearly(): Boolean {
    if (this.editForm.get(['recurringType'])!.value !== 'YEARLY') {
      return false;
    } else {
      return true;
    }
  }

  protected createFromForm(): IScheduledTransaction {
    return {
      ...new ScheduledTransaction(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      originalAmount: this.editForm.get(['originalAmount'])!.value,
      movementType: this.editForm.get(['movementType'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      addToReports: this.editForm.get(['addToReports'])!.value,
      recurringType: this.editForm.get(['recurringType'])!.value,
      separationCount: this.editForm.get(['separationCount'])!.value,
      maxNumberOfOcurrences: this.editForm.get(['maxNumberOfOcurrences'])!.value,
      dayOfWeek: this.editForm.get(['dayOfWeek'])!.value,
      weekOfMonth: this.editForm.get(['weekOfMonth'])!.value,
      dayOfMonth: this.editForm.get(['dayOfMonth'])!.value,
      monthOfYear: this.editForm.get(['monthOfYear'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      sourceUser: this.editForm.get(['sourceUser'])!.value,
      category: this.editForm.get(['category'])!.value,
      wallet: this.editForm.get(['wallet'])!.value,
    };
  }
}
