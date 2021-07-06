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

@Component({
  selector: 'jhi-scheduled-transaction-update',
  templateUrl: './scheduled-transaction-update.component.html',
})
export class ScheduledTransactionUpdateComponent implements OnInit {
  isSaving = false;

  currenciesSharedCollection: ICurrency[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    originalAmount: [null, [Validators.required, Validators.min(0)]],
    movementType: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [],
    addToReports: [null, [Validators.required]],
    incomingTransaction: [null, [Validators.required]],
    currency: [],
  });

  constructor(
    protected scheduledTransactionService: ScheduledTransactionService,
    protected currencyService: CurrencyService,
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
      incomingTransaction: scheduledTransaction.incomingTransaction,
      currency: scheduledTransaction.currency,
    });

    this.currenciesSharedCollection = this.currencyService.addCurrencyToCollectionIfMissing(
      this.currenciesSharedCollection,
      scheduledTransaction.currency
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
      incomingTransaction: this.editForm.get(['incomingTransaction'])!.value,
      currency: this.editForm.get(['currency'])!.value,
    };
  }
}
