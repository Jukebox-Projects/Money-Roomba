import { Component, OnInit, ViewChild } from '@angular/core';

import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexYAxis,
  ApexDataLabels,
  ApexTitleSubtitle,
  ApexStroke,
  ApexGrid,
  ApexFill,
} from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  yaxis: ApexYAxis;
  dataLabels: ApexDataLabels;
  grid: ApexGrid;
  stroke: ApexStroke;
  title: ApexTitleSubtitle;
  fill: ApexFill;
};

@Component({
  selector: 'app-graph-income',
  templateUrl: './graph-income.component.html',
  styleUrls: ['./graph-income.component.css'],
})
export class GraphIncomeComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  constructor() {
    this.chartOptions = {
      series: [
        {
          name: 'Sales Stats',
          data: [10, 15, 11, 16, 13, 19, 8, 14, 10, 20, 15, 17, 9, 30],
        },
      ],
      chart: {
        height: 80,
        type: 'area',
        zoom: {
          enabled: false,
        },
        sparkline: {
          enabled: true,
        },
      },
      dataLabels: {
        enabled: false,
      },
      stroke: {
        width: 5,
        colors: ['#1EAAE7'],
        curve: 'smooth',
      },
      fill: {
        opacity: 0,
        type: 'solid',
      },

      grid: {
        show: false,
      },
      xaxis: {
        labels: {
          show: false,
        },
        axisBorder: {
          show: false,
        },
        axisTicks: {
          show: false,
        },
        categories: [
          'January',
          'February',
          'March',
          'April',
          'May',
          'June',
          'July',
          'August',
          'September',
          'October',
          'January',
          'February',
          'March',
          'April',
          'May',
          'June',
          'July',
          'August',
          'September',
          'October',
          'January',
          'February',
          'March',
          'April',
        ],
      },
      yaxis: {
        show: false,
      },
    };
  }

  ngOnInit(): void {}
}
