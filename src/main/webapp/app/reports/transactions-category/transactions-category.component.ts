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
  reportData: ITransactionsByCategory[];
  transactionCount: number = 0;
  transactionCategory1: ITransactionsByCategory[];
  transactionCategory2: ITransactionsByCategory[];
  transactionCategory3: ITransactionsByCategory[];
  transactionCategory4: ITransactionsByCategory[];
  category1Percentage: number = 0;
  category2Percentage: number = 0;
  category3Percentage: number = 0;
  category4Percentage: number = 0;
  constructor(protected transactionsCategoryService: TransactionsCategoryService) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    /* eslint-disable no-console */
    this.transactionsCategoryService.queryAll().subscribe(
      (res: HttpResponse<ITransactionsByCategory[]>) => {
        this.reportData = res.body ?? [];
        this.resolveData(this.reportData);
      },
      error => {}
    );
  }

  protected resolveData(reportData: ITransactionsByCategory[]): void {
    /* eslint-disable no-console */
    let current = null;
    let counter = 0;
    for (let i in reportData) {
      this.transactionCount = this.transactionCount + reportData[i].count;
    }

    this.calculatePercentage(this.transactionCount, this.reportData);
  }

  protected calculatePercentage(transactionCount: number, reportData: ITransactionsByCategory[]): void {
    for (let i in reportData) {
    }
  }
}
