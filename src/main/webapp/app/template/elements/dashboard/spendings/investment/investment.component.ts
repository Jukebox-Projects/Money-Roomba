import { Component, Input, OnInit, ViewChild } from '@angular/core';

import { ChartComponent, ApexNonAxisChartSeries, ApexPlotOptions, ApexChart, ApexFill, ApexStroke, ApexResponsive } from 'ng-apexcharts';
import { IWallet } from '../../../../../entities/wallet/wallet.model';
import { ITransactionsByCategory } from '../../../../../reports/transactions-category/transactions-category.model';

export type ChartOptions = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  labels: string[];
  plotOptions: ApexPlotOptions;
  fill: ApexFill;
  stroke: ApexStroke;
  responsive: ApexResponsive[];
};

@Component({
  selector: 'app-investment',
  templateUrl: './investment.component.html',
  styleUrls: ['./investment.component.css'],
})
export class InvestmentComponent implements OnInit {
  @Input() transactionsByCategories: ITransactionsByCategory;
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;
  percentage: number;

  constructor() {}

  ngOnInit(): void {
    this.percentage = this.transactionsByCategories.percentage;
    this.percentage = Math.round(this.percentage);
    this.chartOptions = {
      series: [this.percentage],
      chart: {
        height: 140,
        type: 'radialBar',
        zoom: {
          enabled: true,
        },
        toolbar: {
          show: false,
        },
      },
      plotOptions: {
        radialBar: {
          hollow: {
            size: '65%',
            image: undefined,
            imageOffsetX: 0,
            imageOffsetY: 0,
            position: 'front',
            dropShadow: {
              enabled: true,
              top: 3,
              left: 0,
              blur: 10,
              opacity: 0.1,
            },
          },
          track: {
            background: '#bd61dd',
            strokeWidth: '100%',
            margin: 0, // margin is in pixels
          },

          dataLabels: {
            show: true,
            value: {
              offsetY: -7,
              color: '#fff',
              fontSize: '16px',
              show: true,
            },
          },
        },
      },
      fill: {
        colors: ['#fff'],
      },
      stroke: {},
      labels: [''],
      responsive: [
        {
          breakpoint: 575,
          options: {
            chart: {
              height: 120,
            },
          },
        },
      ],
    };
  }
}
