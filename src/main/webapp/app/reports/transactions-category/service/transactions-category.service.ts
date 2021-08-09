import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { createRequestOption } from '../../../core/request/request-util';
import { ITransactionsByCategory } from '../transactions-category.model';
import { DatePipe } from '@angular/common';

export type EntityResponseType = HttpResponse<ITransactionsByCategory>;
export type EntityArrayResponseType = HttpResponse<ITransactionsByCategory[]>;

@Injectable({
  providedIn: 'root',
})
export class TransactionsCategoryService {
  constructor() {}
}
