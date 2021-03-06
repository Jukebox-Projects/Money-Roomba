import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWallet, Wallet } from '../wallet.model';
import { WalletService } from '../service/wallet.service';
import { IUserDetails, UserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';
import { IconService } from '../../../shared/icon-picker/service/icon.service';
import { IICon } from '../../../shared/icon-picker/icon.model';
import { IconPickerComponent } from '../../../shared/icon-picker/icon-picker.component';

@Component({
  selector: 'jhi-wallet-update',
  templateUrl: './wallet-update.component.html',
  styleUrls: ['./wallet-update.component.css'],
})
export class WalletUpdateComponent implements OnInit {
  isSaving = false;

  userDetailsSharedCollection: IUserDetails[] = [];
  currenciesSharedCollection: ICurrency[] = [];
  selectedIcon: IICon;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    inReports: [null, [Validators.required]],
    isActive: [null, [Validators.required]],
    balance: [null, [Validators.required]],
    icon: [null, [Validators.min(0), Validators.required]],
    user: [],
    currency: [null, [Validators.required]],
  });

  constructor(
    protected walletService: WalletService,
    protected userDetailsService: UserDetailsService,
    protected currencyService: CurrencyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected iconService: IconService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wallet }) => {
      this.updateForm(wallet);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wallet = this.createFromForm();
    if (wallet.id !== undefined) {
      this.subscribeToSaveResponse(this.walletService.update(wallet));
    } else {
      this.subscribeToSaveResponse(this.walletService.create(wallet));
    }
  }

  trackUserDetailsById(index: number, item: IUserDetails): number {
    return item.id!;
  }

  trackCurrencyById(index: number, item: ICurrency): number {
    return item.id!;
  }

  openIconPickerModal(): void {
    const modalRef = this.modalService.open(IconPickerComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.icon = this.selectedIcon;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(icon => {
      this.selectedIcon = icon;
      this.editForm.get(['icon']).patchValue(icon.id);
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWallet>>): void {
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

  protected updateForm(wallet: IWallet): void {
    this.editForm.patchValue({
      id: wallet.id,
      name: wallet.name,
      description: wallet.description,
      inReports: wallet.inReports,
      isActive: wallet.isActive,
      balance: wallet.balance,
      icon: wallet.icon,
      user: wallet.user,
      currency: wallet.currency,
    });

    this.userDetailsSharedCollection = this.userDetailsService.addUserDetailsToCollectionIfMissing(
      this.userDetailsSharedCollection,
      wallet.user
    );
    this.currenciesSharedCollection = this.currencyService.addCurrencyToCollectionIfMissing(
      this.currenciesSharedCollection,
      wallet.currency
    );

    this.selectedIcon = this.iconService.getIcon(wallet.icon);
  }

  protected loadRelationshipsOptions(): void {
    this.userDetailsService
      .query()
      .pipe(map((res: HttpResponse<IUserDetails[]>) => res.body ?? []))
      .pipe(
        map((userDetails: IUserDetails[]) =>
          this.userDetailsService.addUserDetailsToCollectionIfMissing(userDetails, this.editForm.get('user')!.value)
        )
      )
      .subscribe((userDetails: IUserDetails[]) => (this.userDetailsSharedCollection = userDetails));

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

  protected createFromForm(): IWallet {
    return {
      ...new Wallet(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      inReports: this.editForm.get(['inReports'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      balance: this.editForm.get(['balance'])!.value,
      icon: this.editForm.get(['icon'])!.value,
      user: new UserDetails(),
      currency: this.editForm.get(['currency'])!.value,
    };
  }
}
