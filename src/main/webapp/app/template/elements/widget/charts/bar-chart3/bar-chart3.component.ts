import { Component, OnInit, ViewChild } from '@angular/core';
import {
  ApexAxisChartSeries,
  ApexChart,
  ChartComponent,
  ApexDataLabels,
  ApexPlotOptions,
  ApexYAxis,
  ApexLegend,
  ApexStroke,
  ApexXAxis,
  ApexFill,
  ApexTooltip,
  ApexGrid,
} from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  yaxis: ApexYAxis;
  xaxis: ApexXAxis;
  fill: ApexFill;
  tooltip: ApexTooltip;
  stroke: ApexStroke;
  legend: ApexLegend;
  colors: string[];
  grid: ApexGrid;
};

@Component({
  selector: 'app-bar-chart3',
  templateUrl: './bar-chart3.component.html',
  styleUrls: ['./bar-chart3.component.css'],
})
export class BarChart3Component implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  constructor() {
    this.chartOptions = {
      series: [
        {
          name: '',
          data: [65, 59, 80, 81, 56, 55, 40],
        },
      ],
      chart: {
        type: 'bar',
        height: 200,
        width: '100%',
      },
      plotOptions: {
        bar: {
          horizontal: false,
          columnWidth: '55%',
          // endingShape: "rounded"
        },
      },
      colors: ['#FFE0DB'],
      dataLabels: {
        enabled: false,
      },
      stroke: {
        show: false,
        width: 2,
        colors: ['transparent'],
      },
      legend: {
        show: false,
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
        categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'],
      },
      yaxis: {
        show: false,
      },
      fill: {
        opacity: 1,
      },
    };
  }

  ngOnInit(): void {}
}
