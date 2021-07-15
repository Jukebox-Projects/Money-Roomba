import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITransaction, Transaction } from '../transaction.model';
import { TransactionService } from '../service/transaction.service';
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

@Component({
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html',
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;

  attachmentsCollection: IAttachment[] = [];
  walletsSharedCollection: IWallet[] = [];
  currenciesSharedCollection: ICurrency[] = [];
  categoriesSharedCollection: ICategory[] = [];
  userDetailsSharedCollection: IUserDetails[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    dateAdded: [null, [Validators.required]],
    amount: [null, [Validators.min(0)]],
    originalAmount: [null, [Validators.required, Validators.min(0)]],
    movementType: [null, [Validators.required]],
    scheduled: [null, [Validators.required]],
    addToReports: [null, [Validators.required]],
    incomingTransaction: [null, [Validators.required]],
    transactionType: [null, [Validators.required]],
    attachment: [],
    wallet: [null, Validators.required],
    currency: [],
    category: [],
    sourceUser: [],
  });

  constructor(
    protected transactionService: TransactionService,
    protected attachmentService: AttachmentService,
    protected walletService: WalletService,
    protected currencyService: CurrencyService,
    protected categoryService: CategoryService,
    protected userDetailsService: UserDetailsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.updateForm(transaction);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaction = this.createFromForm();
    if (transaction.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  trackAttachmentById(index: number, item: IAttachment): number {
    return item.id!;
  }

  trackWalletById(index: number, item: IWallet): number {
    return item.id!;
  }

  trackCurrencyById(index: number, item: ICurrency): number {
    return item.id!;
  }

  trackCategoryById(index: number, item: ICategory): number {
    return item.id!;
  }

  trackUserDetailsById(index: number, item: IUserDetails): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
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

  protected updateForm(transaction: ITransaction): void {
    this.editForm.patchValue({
      id: transaction.id,
      name: transaction.name,
      description: transaction.description,
      dateAdded: transaction.dateAdded,
      amount: transaction.amount,
      originalAmount: transaction.originalAmount,
      movementType: transaction.movementType,
      scheduled: transaction.scheduled,
      addToReports: transaction.addToReports,
      incomingTransaction: transaction.incomingTransaction,
      transactionType: transaction.transactionType,
      attachment: transaction.attachment,
      wallet: transaction.wallet,
      currency: transaction.currency,
      category: transaction.category,
      sourceUser: transaction.sourceUser,
    });

    this.attachmentsCollection = this.attachmentService.addAttachmentToCollectionIfMissing(
      this.attachmentsCollection,
      transaction.attachment
    );
    this.walletsSharedCollection = this.walletService.addWalletToCollectionIfMissing(this.walletsSharedCollection, transaction.wallet);
    this.currenciesSharedCollection = this.currencyService.addCurrencyToCollectionIfMissing(
      this.currenciesSharedCollection,
      transaction.currency
    );
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(
      this.categoriesSharedCollection,
      transaction.category
    );
    this.userDetailsSharedCollection = this.userDetailsService.addUserDetailsToCollectionIfMissing(
      this.userDetailsSharedCollection,
      transaction.sourceUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.attachmentService
      .query({ filter: 'transaction-is-null' })
      .pipe(map((res: HttpResponse<IAttachment[]>) => res.body ?? []))
      .pipe(
        map((attachments: IAttachment[]) =>
          this.attachmentService.addAttachmentToCollectionIfMissing(attachments, this.editForm.get('attachment')!.value)
        )
      )
      .subscribe((attachments: IAttachment[]) => (this.attachmentsCollection = attachments));

    this.walletService
      .query()
      .pipe(map((res: HttpResponse<IWallet[]>) => res.body ?? []))
      .pipe(map((wallets: IWallet[]) => this.walletService.addWalletToCollectionIfMissing(wallets, this.editForm.get('wallet')!.value)))
      .subscribe((wallets: IWallet[]) => (this.walletsSharedCollection = wallets));

    this.currencyService
      .query()
      .pipe(map((res: HttpResponse<ICurrency[]>) => res.body ?? []))
      .pipe(
        map((currencies: ICurrency[]) =>
          this.currencyService.addCurrencyToCollectionIfMissing(currencies, this.editForm.get('currency')!.value)
        )
      )
      .subscribe((currencies: ICurrency[]) => (this.currenciesSharedCollection = currencies));

    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));

    this.userDetailsService
      .query()
      .pipe(map((res: HttpResponse<IUserDetails[]>) => res.body ?? []))
      .pipe(
        map((userDetails: IUserDetails[]) =>
          this.userDetailsService.addUserDetailsToCollectionIfMissing(userDetails, this.editForm.get('sourceUser')!.value)
        )
      )
      .subscribe((userDetails: IUserDetails[]) => (this.userDetailsSharedCollection = userDetails));
  }

  protected createFromForm(): ITransaction {
    return {
      ...new Transaction(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      dateAdded: this.editForm.get(['dateAdded'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      originalAmount: this.editForm.get(['originalAmount'])!.value,
      movementType: this.editForm.get(['movementType'])!.value,
      scheduled: this.editForm.get(['scheduled'])!.value,
      addToReports: this.editForm.get(['addToReports'])!.value,
      incomingTransaction: this.editForm.get(['incomingTransaction'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      attachment: this.editForm.get(['attachment'])!.value,
      wallet: this.editForm.get(['wallet'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      category: this.editForm.get(['category'])!.value,
      sourceUser: this.editForm.get(['sourceUser'])!.value,
    };
  }
}
