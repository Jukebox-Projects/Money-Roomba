import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { Currency, ICurrency } from '../currency/currency.model';
import { CurrencyConverterService } from './service/currency-converter.service';
import { CurrencyConverter, ICurrencyConverter } from './currency-converter.model';

@Component({
  selector: 'jhi-currency-converter',
  templateUrl: './currency-converter.component.html',
})
export class CurrencyConverterComponent implements OnInit {
  isSaving = false;
  currencies: ICurrency[] = [];

  conversionData: ICurrencyConverter;

  editForm = this.fb.group({
    sourceCurrency: [null, [Validators.required]],
    targetCurrency: [null, [Validators.required]],
    conversionRate: [null, [Validators.required]],
    amount: [null, [Validators.required]],
  });

  targetCurrencyValue: number = 0;

  constructor(
    public currencyConverterService: CurrencyConverterService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}
  ngOnInit(): void {
    this.loadRelationshipsOptions();
    this.currencies = this.currencyConverterService.addCurrencyToCollectionIfMissing(this.currencies);
  }

  convert(): void {
    this.isSaving = true;
    const conversionData = this.createFromForm();
    this.conversion(conversionData);
  }

  conversion(conversionData: ICurrencyConverter): void {
    if (conversionData.sourceCurrency && conversionData.targetCurrency && conversionData.amount) {
      const sourceCurrency = conversionData.sourceCurrency;
      const targetCurrency = conversionData.targetCurrency;
      const amount = conversionData.amount;
      const conversionRate = conversionData.conversionRate;
      conversionData.result = ((amount * conversionRate) / sourceCurrency.conversionRate).toFixed(2);
      this.conversionData = conversionData;
    }
    this.isSaving = false;
  }

  resetForm(): void {
    this.editForm.reset();
    this.conversionData = null;
  }

  changeTargetCurrency(): void {
    this.editForm.patchValue({ conversionRate: this.editForm.get(['targetCurrency'])!.value.conversionRate });
  }

  trackCurrencyById(index: number, item: ICurrency): number {
    return item.id!;
  }

  previousState(): void {
    window.history.back();
  }

  protected createFromForm(): ICurrencyConverter {
    return {
      ...new CurrencyConverter(),
      sourceCurrency: this.editForm.get(['sourceCurrency'])!.value,
      targetCurrency: this.editForm.get(['targetCurrency'])!.value,
      conversionRate: this.editForm.get(['conversionRate'])!.value,
      amount: this.editForm.get(['amount'])!.value,
    };
  }

  protected loadRelationshipsOptions(): void {
    this.currencyConverterService
      .query()
      .pipe(map((res: HttpResponse<ICurrency[]>) => res.body ?? []))
      .pipe(
        map((currencies: ICurrency[]) =>
          this.currencyConverterService.addCurrencyToCollectionIfMissing(currencies, this.editForm.get('sourceCurrency')!.value)
        )
      )
      .subscribe((currencies: ICurrency[]) => (this.currencies = currencies));
  }
}
