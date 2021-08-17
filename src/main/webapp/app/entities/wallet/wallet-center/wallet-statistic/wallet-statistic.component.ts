import { Component, OnInit, ViewChild } from '@angular/core';
import { TotalBalanceService } from '../../../../reports/total-balance/Service/total-balance.service';
import { ITotalBalance } from '../../../../reports/total-balance/total-balance.model';
import {
  ChartComponent,
  ApexNonAxisChartSeries,
  ApexResponsive,
  ApexChart,
  ApexFill,
  ApexDataLabels,
  ApexLegend,
  ApexStroke,
} from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  responsive: ApexResponsive[];
  labels: any;
  fill: ApexFill;
  legend: ApexLegend;
  dataLabels: ApexDataLabels;
  colors: string[];
  stroke: ApexStroke;
};

@Component({
  selector: 'app-wallet-statistic',
  templateUrl: './wallet-statistic.component.html',
  styleUrls: ['./wallet-statistic.component.scss'],
})
export class WalletStatisticComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;
  reportData: ITotalBalance;
  totalBalance: number;

  constructor(protected totalBalanceService: TotalBalanceService) {
    this.chartOptions = {
      series: [34, 12, 41, 22, 15],
      chart: {
        width: 300,
        type: 'donut',
      },
      dataLabels: {
        enabled: false,
      },
      stroke: {
        width: 0,
      },
      fill: {
        type: 'solid',
      },
      legend: {
        position: 'bottom',
        show: false,
      },
      colors: ['#1EAAE7', '#6418C3', '#2BC155', '#FF7A30', '#FFEF5F'],
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: {
              width: 200,
            },
            legend: {
              position: 'bottom',
              show: false,
            },
          },
        },
      ],
    };
  }

  ngOnInit(): void {}
}
