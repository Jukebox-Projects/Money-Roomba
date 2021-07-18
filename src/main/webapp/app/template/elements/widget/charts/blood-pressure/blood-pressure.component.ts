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
  selector: 'app-blood-pressure',
  templateUrl: './blood-pressure.component.html',
  styleUrls: ['./blood-pressure.component.css'],
})
export class BloodPressureComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  constructor() {
    this.chartOptions = {
      series: [
        {
          name: '',
          data: [33, 22, 68, 54, 8, 30, 74, 7, 36, 5, 41, 19, 43, 29, 38],
        },
      ],
      chart: {
        type: 'bar',
        height: 100,
        width: 200,
        zoom: {
          enabled: false,
        },
        sparkline: {
          enabled: true,
        },
      },
      plotOptions: {
        bar: {
          horizontal: false,
          columnWidth: '55%',
          // endingShape: "rounded"
        },
      },
      colors: ['#1bd084'],
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
        categories: ['Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct'],
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
