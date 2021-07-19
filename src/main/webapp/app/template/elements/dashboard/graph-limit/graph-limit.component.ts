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
  selector: 'app-graph-limit',
  templateUrl: './graph-limit.component.html',
  styleUrls: ['./graph-limit.component.css'],
})
export class GraphLimitComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  constructor() {
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

  ngOnInit(): void {}
}
