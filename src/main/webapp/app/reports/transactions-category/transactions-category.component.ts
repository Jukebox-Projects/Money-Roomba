import { Component, OnInit } from '@angular/core';
import { ITransactionsByCategory } from './transactions-category.model';
import { HttpResponse } from '@angular/common/http';
import { TransactionsCategoryService } from './service/transactions-category.service';

@Component({
  selector: 'jhi-transactions-category',
  templateUrl: './transactions-category.component.html',
  styleUrls: ['./transactions-category.component.scss'],
})
export class TransactionsCategoryComponent implements OnInit {
  reportDataIncome: ITransactionsByCategory[];
  reportDataExpense: ITransactionsByCategory[];
  categoryExpense0: ITransactionsByCategory;
  categoryExpense1: ITransactionsByCategory;
  categoryExpense2: ITransactionsByCategory;
  categoryExpense3: ITransactionsByCategory;
  categoryIncome0: ITransactionsByCategory;
  categoryIncome1: ITransactionsByCategory;
  categoryIncome2: ITransactionsByCategory;
  categoryIncome3: ITransactionsByCategory;

  constructor(protected transactionsCategoryService: TransactionsCategoryService) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    /* eslint-disable no-console */
    this.transactionsCategoryService.queryAll(1).subscribe(
      (res: HttpResponse<ITransactionsByCategory[]>) => {
        this.reportDataExpense = res.body ?? [];
        this.resolveDataExpense(this.reportDataExpense);
      },
      error => {}
    );
    this.transactionsCategoryService.queryAll(2).subscribe(
      (res: HttpResponse<ITransactionsByCategory[]>) => {
        this.reportDataIncome = res.body ?? [];
        this.resolveDataIncome(this.reportDataIncome);
      },
      error => {}
    );
  }

  protected resolveDataExpense(reportDataExpense: ITransactionsByCategory[]): void {
    /* eslint-disable no-console */
    this.categoryExpense0 = reportDataExpense[0];
    this.categoryExpense1 = reportDataExpense[1];
    this.categoryExpense2 = reportDataExpense[2];
    this.categoryExpense3 = reportDataExpense[3];
  }

  protected resolveDataIncome(reportDataIncome: ITransactionsByCategory[]): void {
    /* eslint-disable no-console */
    this.categoryIncome0 = reportDataIncome[0];
    this.categoryIncome1 = reportDataIncome[1];
    this.categoryIncome2 = reportDataIncome[2];
    this.categoryIncome3 = reportDataIncome[3];
  }

  public isEmpty(obj) {
    if (Object.keys(this.categoryIncome0).length === 0) {
      return false;
    } else {
      return true;
    }
  }
}
