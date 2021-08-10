import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { DatePipe } from '@angular/common';
import { ITransactionsByCategory } from '../../transactions-category/transactions-category.model';
import { TransactionsByCategoryModel } from '../../transactions-category/transactions-category.model';
import { createRequestOption } from '../../../core/request/request-util';

export type EntityResponseType = HttpResponse<ITransactionsByCategory>;
export type EntityArrayResponseType = HttpResponse<ITransactionsByCategory[]>;

@Injectable({
  providedIn: 'root',
})
export class TransactionsCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reports/transactions-by-category');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService, protected datePipe: DatePipe) {}

  queryAll(): Observable<EntityArrayResponseType> {
    var endDate = new Date();
    var startDate = new Date(new Date().setDate(endDate.getDate() - 30));
    const options = createRequestOption({
      startDate: this.datePipe.transform(startDate, 'yyyy-MM-dd'),
      endDate: this.datePipe.transform(endDate, 'yyyy-MM-dd'),
    });
    return this.http.get<TransactionsByCategoryModel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}
