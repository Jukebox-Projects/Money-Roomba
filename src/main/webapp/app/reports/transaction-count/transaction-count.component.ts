import { Component, OnInit } from '@angular/core';
import { ITransactionCount } from './transaction-count.model';
import { TransactionCountService } from './service/transaction-count.service';
import { MovementType } from '../../entities/enumerations/movement-type.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-transaction-count',
  templateUrl: './transaction-count.component.html',
  styleUrls: ['./transaction-count.component.scss'],
})
export class TransactionCountComponent implements OnInit {
  reportData: ITransactionCount[];
  income: ITransactionCount;
  expense: ITransactionCount;
  constructor(protected transactionCountService: TransactionCountService) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.transactionCountService.queryAll().subscribe(
      (res: HttpResponse<ITransactionCount[]>) => {
        this.reportData = res.body ?? [];
        this.resolveData(this.reportData);
      },
      error => {}
    );
  }

  protected resolveData(reportData: ITransactionCount[]): void {
    if (reportData.length == 2) {
      if (reportData[0].movementType == MovementType.INCOME && reportData[1].movementType == MovementType.EXPENSE) {
        this.income = reportData[0];
        this.expense = reportData[1];
      } else if (reportData[1].movementType == MovementType.INCOME && reportData[0].movementType == MovementType.EXPENSE) {
        this.income = reportData[1];
        this.expense = reportData[0];
      }
    } else if (reportData.length == 1) {
      if (reportData[0].movementType == MovementType.INCOME) {
        this.income = reportData[0];
      } else {
        this.expense = reportData[0];
      }
    } else {
      this.income = { count: 0, movementType: MovementType.INCOME };
      this.expense = { count: 0, movementType: MovementType.EXPENSE };
    }
  }
}
