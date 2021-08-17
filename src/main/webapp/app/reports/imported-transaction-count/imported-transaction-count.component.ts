import { HttpResponse } from '@angular/common/http';
import { IImportedTransactionCount } from './imported-transaction-count.model';
import { ImportedTransactionCountService } from './service/imported-transaction-count.service';
import { Component, OnInit, ViewChild } from '@angular/core';

import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexYAxis,
  ApexDataLabels,
  ApexTooltip,
  ApexStroke,
  ApexFill,
  ApexGrid,
} from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  yaxis: ApexYAxis;
  stroke: ApexStroke;
  tooltip: ApexTooltip;
  dataLabels: ApexDataLabels;
  colors: string[];
  fill: ApexFill;
  grid: ApexGrid;
};

@Component({
  selector: 'jhi-imported-transaction-count',
  templateUrl: './imported-transaction-count.component.html',
  styleUrls: ['./imported-transaction-count.component.scss'],
})
export class ImportedTransactionCountComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;
  reportData: IImportedTransactionCount = { count: 0 };

  constructor(protected importedTransactionCountService: ImportedTransactionCountService) {
    this.chartOptions = {
      series: [
        {
          name: 'My First dataset',
          data: [60, 70, 60, 70, 60, 70, 60],
        },
        {
          name: 'My First dataset',
          data: [50, 60, 50, 60, 50, 60, 50],
        },
        {
          name: 'My First dataset',
          data: [40, 50, 40, 50, 40, 50, 40],
        },
      ],
      chart: {
        height: 220,
        type: 'area',
        sparkline: {
          enabled: true,
        },
      },
      dataLabels: {
        enabled: false,
      },
      stroke: {
        curve: 'smooth',
      },
      colors: ['#3584ee', '#6cb6f1', '#addef4'],
      grid: {
        show: false,
      },
      fill: {
        opacity: 1,
        type: 'solid',
      },
      xaxis: {
        labels: {
          show: false,
        },
      },
      yaxis: {
        show: false,
      },
      tooltip: {
        enabled: false,
      },
    };
  }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.importedTransactionCountService.query().subscribe(
      (res: HttpResponse<IImportedTransactionCount>) => {
        this.reportData = res.body ?? null;
      },
      error => {}
    );
  }
}
