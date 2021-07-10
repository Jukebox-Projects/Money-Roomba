import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISchedulePattern, SchedulePattern } from '../schedule-pattern.model';
import { SchedulePatternService } from '../service/schedule-pattern.service';
import { IScheduledTransaction } from 'app/entities/scheduled-transaction/scheduled-transaction.model';
import { ScheduledTransactionService } from 'app/entities/scheduled-transaction/service/scheduled-transaction.service';

@Component({
  selector: 'jhi-schedule-pattern-update',
  templateUrl: './schedule-pattern-update.component.html',
})
export class SchedulePatternUpdateComponent implements OnInit {
  isSaving = false;

  scheduledTransactionsSharedCollection: IScheduledTransaction[] = [];

  editForm = this.fb.group({
    id: [],
    recurringType: [null, [Validators.required]],
    separationCount: [null, [Validators.min(0)]],
    maxNumberOfOcurrences: [],
    dayOfWeek: [null, [Validators.min(0), Validators.max(6)]],
    weekOfMonth: [null, [Validators.min(0), Validators.max(5)]],
    dayOfMonth: [null, [Validators.min(0), Validators.max(31)]],
    monthOfYear: [null, [Validators.min(0), Validators.max(11)]],
    scheduleTransaction: [],
  });

  constructor(
    protected schedulePatternService: SchedulePatternService,
    protected scheduledTransactionService: ScheduledTransactionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schedulePattern }) => {
      this.updateForm(schedulePattern);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const schedulePattern = this.createFromForm();
    if (schedulePattern.id !== undefined) {
      this.subscribeToSaveResponse(this.schedulePatternService.update(schedulePattern));
    } else {
      this.subscribeToSaveResponse(this.schedulePatternService.create(schedulePattern));
    }
  }

  trackScheduledTransactionById(index: number, item: IScheduledTransaction): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchedulePattern>>): void {
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

  protected updateForm(schedulePattern: ISchedulePattern): void {
    this.editForm.patchValue({
      id: schedulePattern.id,
      recurringType: schedulePattern.recurringType,
      separationCount: schedulePattern.separationCount,
      maxNumberOfOcurrences: schedulePattern.maxNumberOfOcurrences,
      dayOfWeek: schedulePattern.dayOfWeek,
      weekOfMonth: schedulePattern.weekOfMonth,
      dayOfMonth: schedulePattern.dayOfMonth,
      monthOfYear: schedulePattern.monthOfYear,
      scheduleTransaction: schedulePattern.scheduleTransaction,
    });

    this.scheduledTransactionsSharedCollection = this.scheduledTransactionService.addScheduledTransactionToCollectionIfMissing(
      this.scheduledTransactionsSharedCollection,
      schedulePattern.scheduleTransaction
    );
  }

  protected loadRelationshipsOptions(): void {
    this.scheduledTransactionService
      .query()
      .pipe(map((res: HttpResponse<IScheduledTransaction[]>) => res.body ?? []))
      .pipe(
        map((scheduledTransactions: IScheduledTransaction[]) =>
          this.scheduledTransactionService.addScheduledTransactionToCollectionIfMissing(
            scheduledTransactions,
            this.editForm.get('scheduleTransaction')!.value
          )
        )
      )
      .subscribe((scheduledTransactions: IScheduledTransaction[]) => (this.scheduledTransactionsSharedCollection = scheduledTransactions));
  }

  protected createFromForm(): ISchedulePattern {
    return {
      ...new SchedulePattern(),
      id: this.editForm.get(['id'])!.value,
      recurringType: this.editForm.get(['recurringType'])!.value,
      separationCount: this.editForm.get(['separationCount'])!.value,
      maxNumberOfOcurrences: this.editForm.get(['maxNumberOfOcurrences'])!.value,
      dayOfWeek: this.editForm.get(['dayOfWeek'])!.value,
      weekOfMonth: this.editForm.get(['weekOfMonth'])!.value,
      dayOfMonth: this.editForm.get(['dayOfMonth'])!.value,
      monthOfYear: this.editForm.get(['monthOfYear'])!.value,
      scheduleTransaction: this.editForm.get(['scheduleTransaction'])!.value,
    };
  }
}
