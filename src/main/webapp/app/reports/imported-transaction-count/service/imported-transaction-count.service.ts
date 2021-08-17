import { IImportedTransactionCount } from './../imported-transaction-count.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { Observable } from 'rxjs';
export type EntityResponseType = HttpResponse<IImportedTransactionCount>;
export type EntityArrayResponseType = HttpResponse<IImportedTransactionCount[]>;

@Injectable({
  providedIn: 'root',
})
export class ImportedTransactionCountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reports/imported-transactions-count');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  query(): Observable<EntityResponseType> {
    return this.http.get<IImportedTransactionCount>(this.resourceUrl, { observe: 'response' });
  }
}
