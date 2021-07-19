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
  selector: 'app-weekly-sales',
  templateUrl: './weekly-sales.component.html',
  styleUrls: ['./weekly-sales.component.css'],
})
export class WeeklySalesComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  constructor() {
    this.chartOptions = {
      series: [
        {
          name: 'Sales Stats',
          data: [0, 18, 14, 24, 16, 30],
        },
      ],
      chart: {
        type: 'area',
        /* height: 200, */
        width: '100%',
        zoom: {
          enabled: false,
        },
      },
      dataLabels: {
        enabled: false,
      },
      stroke: {
        curve: 'straight',
        width: 2,
      },
      grid: {
        show: false,
      },
      colors: ['#FE634E'],
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
        categories: ['January', 'February', 'March', 'April', 'May', 'June'],
      },
      yaxis: {
        opposite: false,
        show: false,
      },
      fill: {
        opacity: 1,
        type: 'solid',
        colors: ['#FEB1A6'],
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
