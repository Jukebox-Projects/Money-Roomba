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
  selector: 'app-graph-weekly-wallet-usage',
  templateUrl: './graph-weekly-wallet-usage.component.html',
  styleUrls: ['./graph-weekly-wallet-usage.component.css'],
})
export class GraphWeeklyWalletUsageComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  constructor() {
    this.chartOptions = {
      series: [
        {
          name: 'Sales Stats',
          data: [10, 20, 15, 25, 10, 20, 12, 24, 10, 26, 8, 18, 13, 28, 10, 20],
        },
      ],
      chart: {
        height: 100,
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
      colors: ['#70c9f0'],
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

  ngOnInit(): void {}
}
