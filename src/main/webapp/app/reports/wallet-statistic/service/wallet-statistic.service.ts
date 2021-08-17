import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { IWalletStatistic } from '../wallet-statistic.model';
import { DatePipe } from '@angular/common';
import { WalletStatisticModel } from '../wallet-statistic.model';
import { TotalBalanceModel } from '../../total-balance/total-balance.model';

export type EntityResponseType = HttpResponse<IWalletStatistic>;
export type EntityArrayResponseType = HttpResponse<IWalletStatistic[]>;

@Injectable({
  providedIn: 'root',
})
export class WalletStatisticService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reports/wallet-statistic');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService, protected datePipe: DatePipe) {}

  queryAll(): Observable<EntityArrayResponseType> {
    return this.http.get<WalletStatisticModel[]>(this.resourceUrl, { observe: 'response' });
  }
}
