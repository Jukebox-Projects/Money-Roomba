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
  categoryExpense: ITransactionsByCategory[];
  categoryIncome: ITransactionsByCategory[];
  colorsLabel = ['#AC39D4', '#40D4A8', '#461EE7', '#1EB6E7'];

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
    this.categoryExpense = reportDataExpense;
  }

  protected resolveDataIncome(reportDataIncome: ITransactionsByCategory[]): void {
    /* eslint-disable no-console */
    this.categoryIncome = reportDataIncome;
  }

  public isEmpty(obj) {
    if (Object.keys(this.categoryIncome[0]).length === 0) {
      return false;
    } else {
      return true;
    }
  }
}
