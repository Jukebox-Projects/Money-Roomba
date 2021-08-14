import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { WalletStatisticService } from './service/wallet-statistic.service';
import { IWalletStatistic } from './wallet-statistic.model';
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
import { each } from 'chart.js/helpers';

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
  reportData: IWalletStatistic[];
  percentages: number[] = [];
  colorList: string[] = [];
  nameList: string[] = [];
  constructor(protected walletStatisticService: WalletStatisticService) {
    this.chartOptions = {
      series: this.percentages,
      chart: {
        width: 300,
        type: 'donut',
      },
      dataLabels: {
        enabled: false,
      },
      labels: this.nameList,
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
      colors: ['#1EAAE7', '#2BC155', '#6418C3', '#FFA500', '#808080'],
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

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.walletStatisticService.queryAll().subscribe(
      (res: HttpResponse<IWalletStatistic[]>) => {
        this.reportData = res.body ?? [];
        this.resolveData(this.reportData);
      },
      error => {}
    );
  }

  protected resolveData(reportData: IWalletStatistic[]): void {
    /* eslint-disable no-console */
    console.log(reportData);
    for (let i = 0; i < reportData.length; i++) {
      this.percentages[i] = reportData[i].percentage;
      this.nameList[i] = reportData[i].name;
    }
    console.log(this.percentages);
    this.colorList = ['#1EAAE7', '#6418C3', '#2BC155'];
  }
}
