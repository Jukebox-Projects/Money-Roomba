import { Component, OnInit, ViewChild } from '@angular/core';

import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexDataLabels,
  ApexFill,
  ApexStroke,
  ApexYAxis,
  ApexTitleSubtitle,
  ApexLegend,
  ApexTooltip,
  ApexGrid,
} from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  stroke: ApexStroke;
  dataLabels: ApexDataLabels;
  yaxis: ApexYAxis;
  title: ApexTitleSubtitle;
  labels: string[];
  fill: ApexFill;
  legend: ApexLegend;
  subtitle: ApexTitleSubtitle;
  grid: ApexGrid;
  colors: string[];
  tooltip: ApexTooltip;
};

@Component({
  selector: 'app-sales-status',
  templateUrl: './sales-status.component.html',
  styleUrls: ['./sales-status.component.css'],
})
export class SalesStatusComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  constructor() {
    this.chartOptions = {
      series: [
        {
          name: 'Sales Stats',
          data: [25, 60, 30, 71, 26, 85, 50],
        },
      ],
      chart: {
        type: 'area',
        height: 300,
        width: '100%',
        zoom: {
          enabled: false,
        },
      },
      dataLabels: {
        enabled: false,
      },
      stroke: {
        curve: 'smooth',
        width: 1,
      },
      grid: {
        show: false,
      },
      colors: ['#48A9F8'],
      tooltip: {
        enabled: true,
      },
      /* title: {
        text: "Title",
        align: "left"
      }, */
      /* subtitle: {
        text: "Sub title",
        align: "left"
      }, */
      labels: ['1', '2', '3', '4', '5', '6', '7', '8'],
      xaxis: {
        labels: {
          show: false,
        },
        axisBorder: {
          show: false,
        },
        categories: ['Jan', 'Febr', 'Mar', 'Apr', 'May', 'Jun', 'Jul'],
      },
      yaxis: {
        opposite: false,
        show: false,
      },
      fill: {
        opacity: 1,
        type: 'solid',
        colors: ['#48A9F8'],
        gradient: {
          shade: 'light',
        },
      },
      legend: {
        horizontalAlign: 'left',
        show: false,
      },
    };
  }

  ngOnInit(): void {}
}
