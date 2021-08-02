import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWallet } from '../wallet.model';
import { IICon } from '../../../shared/icon-picker/icon.model';
import { IconService } from '../../../shared/icon-picker/service/icon.service';

@Component({
  selector: 'jhi-wallet-detail',
  templateUrl: './wallet-detail.component.html',
})
export class WalletDetailComponent implements OnInit {
  wallet: IWallet | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected iconService: IconService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wallet }) => {
      this.wallet = wallet;
    });
  }

  previousState(): void {
    window.history.back();
  }

  getIcon(iconId: number): IICon {
    return this.iconService.getIcon(iconId);
  }
}
