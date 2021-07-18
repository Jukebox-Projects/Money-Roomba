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
  selector: 'app-graph-outome',
  templateUrl: './graph-outome.component.html',
  styleUrls: ['./graph-outome.component.css'],
})
export class GraphOutomeComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  constructor() {
    this.chartOptions = {
      series: [
        {
          name: 'Sales Stats',
          data: [10, 15, 12, 16, 12, 18, 14, 10, 15, 12, 19, 13, 15, 12],
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
