import { AccountService } from './../../../core/auth/account.service';
import { Authority } from './../../../config/authority.constants';
import { Validators, FormBuilder } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILicense } from '../license.model';
import { LicenseService } from '../service/license.service';
import { IPayPalConfig, ICreateOrderRequest } from 'ngx-paypal';
import { ISystemSetting } from './../../system-setting/system-setting.model';
import { SystemSettingService } from './../../system-setting/service/system-setting.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  templateUrl: './license-buy-or-activate-dialog.component.html',
})
export class LicenseBuyOrActivateDialogComponent {
  license?: ILicense;
  public payPalConfig?: IPayPalConfig;
  showSuccess: boolean;
  isBuying: boolean = false;
  isGift: boolean;
  creationForm = this.fb.group({
    code: [{ value: null, disabled: this.isPremium() }, [Validators.required]],
  });
  giftForm = this.fb.group({
    email: [null, [Validators.required, Validators.email]],
  });
  constructor(
    protected licenseService: LicenseService,
    protected accountService: AccountService,
    protected systemSettingService: SystemSettingService,
    protected activeModal: NgbActiveModal,
    protected fb: FormBuilder
  ) {}

  systemSettings?: ISystemSetting[];
  isLoading = false;

  toggle(type: String): void {
    this.isGift = type == 'gift';
    this.isBuying = this.isGift ? false : true;
  }

  getPrice(): number {
    return this.systemSettings?.find(kv => kv?.key === 'price')?.value || 0;
  }

  getTax(): number {
    return this.systemSettings?.find(kv => kv?.key === 'tax')?.value || 0;
  }

  isPremium(): boolean {
    return this.accountService.hasAnyAuthority(Authority.PREMIUM_USER) || this.accountService.hasAnyAuthority(Authority.ADMIN);
  }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.isLoading = true;

    this.systemSettingService.query().subscribe(
      (res: HttpResponse<ISystemSetting[]>) => {
        this.isLoading = false;
        this.systemSettings = res.body ?? [];
        this.initConfig();
      },
      () => {
        this.isLoading = false;
        this.initConfig();
      }
    );
  }

  private initConfig(): void {
    /* eslint-disable no-console */
    const subTotal = this.getPrice();
    const tax = this.getTax() / 100;
    const total = (subTotal * tax + subTotal).toString();
    console.log(total);
    this.payPalConfig = {
      currency: 'USD',
      clientId: 'AVTp_lpVAZ5LfLyPaPWJXrAKpst48MsE1eDa7mK31i_uOjohlybgxe9LKdDpp4UKlXsVJZLGJewv0M2M',
      createOrderOnClient: data =>
        <ICreateOrderRequest>{
          intent: 'CAPTURE',
          purchase_units: [
            {
              amount: {
                currency_code: 'USD',
                value: total,
                breakdown: {
                  item_total: {
                    currency_code: 'USD',
                    value: total,
                  },
                },
              },
              items: [
                {
                  name: 'MoneyRoomba Premium',
                  quantity: '1',
                  category: 'DIGITAL_GOODS',
                  unit_amount: {
                    currency_code: 'USD',
                    value: total,
                  },
                },
              ],
            },
          ],
        },
      advanced: {
        commit: 'true',
      },
      style: {
        label: 'paypal',
        layout: 'vertical',
      },
      onApprove: (data, actions) => {
        console.log('onApprove - transaction was approved, but not authorized', data, actions);
        actions.order.get().then(details => {
          console.log('onApprove - you can get full order details inside onApprove: ', details);
          this.payPal();
        });
      },
      onClientAuthorization: data => {
        console.log('onClientAuthorization - you should probably inform your server about completed transaction at this point', data);
        this.showSuccess = true;
      },
      onCancel: (data, actions) => {
        console.log('OnCancel', data, actions);
      },
      onError: err => {
        console.log('OnError', err);
      },
      onClick: (data, actions) => {
        console.log('onClick', data, actions);
      },
    };
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  activate(): void {
    let licenseCode = this.creationForm.get('code').value || 1;
    this.licenseService.activate({ code: licenseCode }).subscribe(() => {
      this.activeModal.close('activated');
    });
  }

  sendEmailGift(): void {
    this.isBuying = true;
  }

  payPal(): void {
    let paypal = {
      isGiftString: `${this.isGift}`,
      email: this.giftForm.get('email').value,
    };
    this.licenseService.payPal(paypal).subscribe(() => {
      this.activeModal.close('bought');
    });
  }
}
