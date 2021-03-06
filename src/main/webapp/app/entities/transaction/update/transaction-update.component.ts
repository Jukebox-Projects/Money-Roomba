import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
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
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { TransactionState } from 'app/entities/enumerations/transaction-state.model';
import { AddContactModalComponent } from 'app/entities/contact/add-contact-modal/add-contact-modal/add-contact-modal.component';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';

@Component({
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html',
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;
  isOutgoing = false;
  attachmentsCollection: IAttachment[] = [];
  walletsSharedCollection: IWallet[] = [];
  currenciesSharedCollection: ICurrency[] = [];
  categoriesSharedCollection: ICategory[] = [];
  userDetailsSharedCollection: IUserDetails[] = [];
  contactSharedCollection: IContact[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    dateAdded: [null, [Validators.required]],
    amount: [],
    originalAmount: [null, [Validators.required, Validators.min(0)]],
    movementType: [null, [Validators.required]],
    scheduled: [],
    addToReports: [null, [Validators.required]],
    incomingTransaction: [],
    transactionType: [],
    attachment: [],
    wallet: [null, [Validators.required]],
    currency: [null, [Validators.required]],
    category: [],
    sourceUser: [],
    transactionState: [],
    recievingUser: [],
  });

  constructor(
    protected transactionService: TransactionService,
    protected attachmentService: AttachmentService,
    protected walletService: WalletService,
    protected currencyService: CurrencyService,
    protected categoryService: CategoryService,
    protected userDetailsService: UserDetailsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected contactService: ContactService
  ) {}

  toggleOutgoing(): void {
    this.isOutgoing = !this.isOutgoing;
  }

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

  trackContactById(index: number, item: IContact): number {
    return item.id;
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
      recievingUser: transaction.recievingUser,
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
      transaction.recievingUser
    );
    this.contactSharedCollection = this.contactService.addContactToCollectionIfMissing(
      this.contactSharedCollection,
      transaction.recievingUser
    );
  }

  isIncoming(): boolean {
    return this.editForm.get(['incomingTransaction'])!.value;
  }

  isAPIorEmail(): boolean {
    return this.editForm.get('transactionType')!.value == 'API' || this.editForm.get('transactionType')!.value == 'EMAIL';
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
          this.userDetailsService.addUserDetailsToCollectionIfMissing(userDetails, this.editForm.get('recievingUser')!.value)
        )
      )
      .subscribe((userDetails: IUserDetails[]) => (this.userDetailsSharedCollection = userDetails));

    this.contactService
      .query()
      .pipe(map((res: HttpResponse<IContact[]>) => res.body ?? []))
      .pipe(
        map((contact: IContact[]) =>
          this.contactService.addContactToCollectionIfMissing(contact, this.editForm.get('recievingUser')!.value)
        )
      )
      .subscribe((contact: IContact[]) => (this.contactSharedCollection = contact));
  }

  protected createFromForm(): ITransaction {
    return {
      ...new Transaction(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      dateAdded: this.editForm.get(['dateAdded'])!.value,
      amount: 0,
      originalAmount: this.editForm.get(['originalAmount'])!.value,
      movementType: this.editForm.get(['movementType'])!.value,
      scheduled: false,
      addToReports: this.editForm.get(['addToReports'])!.value,
      incomingTransaction: false,
      transactionType: this.editForm.get(['transactionType'])!.value == null ? 'MANUAL' : this.editForm.get(['transactionType'])!.value,
      attachment: this.editForm.get(['attachment'])!.value,
      wallet: this.editForm.get(['wallet'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      category: this.editForm.get(['category'])!.value,
      sourceUser: this.editForm.get(['sourceUser'])!.value,
      recievingUser: this.editForm.get(['recievingUser'])!.value,
      state: this.editForm.get(['transactionState'])!.value,
    };
  }

  isPending(transactionState): boolean {
    /* eslint-disable no-console */
    console.log(transactionState);
    if (transactionState.toString() === 'PENDING_APPROVAL') {
      return true;
    } else {
      return false;
    }
  }

  open(): void {
    const modalRef = this.modalService.open(AddContactModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.closed.subscribe(reason => {
      if (reason === 'activated') {
        setTimeout(() => {
          window.location.reload();
        }, 3500);
      }
    });
  }
}
