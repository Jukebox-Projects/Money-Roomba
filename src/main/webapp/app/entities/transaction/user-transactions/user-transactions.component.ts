import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IconService } from '../../../shared/icon-picker/service/icon.service';
import { IWallet } from '../../wallet/wallet.model';
import { IICon } from '../../../shared/icon-picker/icon.model';

@Component({
  selector: 'app-user-transactions',
  templateUrl: './user-transactions.component.html',
  styleUrls: ['./user-transactions.component.scss'],
})
export class UserTransactionsComponent implements OnInit {
  wallet: IWallet | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected iconService: IconService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wallet }) => {
      this.wallet = wallet;
    });
  }

  getIcon(iconId: number): IICon {
    return this.iconService.getIcon(iconId);
  }
}
