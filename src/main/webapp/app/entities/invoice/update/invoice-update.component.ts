import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInvoice, Invoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';
import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';

@Component({
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html',
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;

  currenciesSharedCollection: ICurrency[] = [];

  editForm = this.fb.group({
    id: [],
    companyName: [null, [Validators.required]],
    userName: [null, [Validators.required]],
    userLastName: [null, [Validators.required]],
    userEmail: [null, [Validators.required]],
    dateCreated: [null, [Validators.required]],
    total: [null, [Validators.required, Validators.min(0)]],
    subTotal: [null, [Validators.required, Validators.min(0)]],
    tax: [null, [Validators.min(0)]],
    purchaseDescription: [],
    itemQuantity: [null, [Validators.required, Validators.min(0)]],
    itemPrice: [null, [Validators.required, Validators.min(0)]],
    currency: [],
  });

  constructor(
    protected invoiceService: InvoiceService,
    protected currencyService: CurrencyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.updateForm(invoice);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.createFromForm();
    if (invoice.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  trackCurrencyById(index: number, item: ICurrency): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
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

  protected updateForm(invoice: IInvoice): void {
    this.editForm.patchValue({
      id: invoice.id,
      companyName: invoice.companyName,
      userName: invoice.userName,
      userLastName: invoice.userLastName,
      userEmail: invoice.userEmail,
      dateCreated: invoice.dateCreated,
      total: invoice.total,
      subTotal: invoice.subTotal,
      tax: invoice.tax,
      purchaseDescription: invoice.purchaseDescription,
      itemQuantity: invoice.itemQuantity,
      itemPrice: invoice.itemPrice,
      currency: invoice.currency,
    });

    this.currenciesSharedCollection = this.currencyService.addCurrencyToCollectionIfMissing(
      this.currenciesSharedCollection,
      invoice.currency
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

  protected createFromForm(): IInvoice {
    return {
      ...new Invoice(),
      id: this.editForm.get(['id'])!.value,
      companyName: this.editForm.get(['companyName'])!.value,
      userName: this.editForm.get(['userName'])!.value,
      userLastName: this.editForm.get(['userLastName'])!.value,
      userEmail: this.editForm.get(['userEmail'])!.value,
      dateCreated: this.editForm.get(['dateCreated'])!.value,
      total: this.editForm.get(['total'])!.value,
      subTotal: this.editForm.get(['subTotal'])!.value,
      tax: this.editForm.get(['tax'])!.value,
      purchaseDescription: this.editForm.get(['purchaseDescription'])!.value,
      itemQuantity: this.editForm.get(['itemQuantity'])!.value,
      itemPrice: this.editForm.get(['itemPrice'])!.value,
      currency: this.editForm.get(['currency'])!.value,
    };
  }
}
