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
  ApexResponsive,
  ApexStates,
  ApexMarkers,
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
  responsive: ApexResponsive[];
  states: ApexStates;
  markers: ApexMarkers;
  grid: ApexGrid;
};

@Component({
  selector: 'app-graph-bar',
  templateUrl: './graph-bar.component.html',
  styleUrls: ['./graph-bar.component.css'],
})
export class GraphBarComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  constructor() {
    this.chartOptions = {
      series: [
        {
          name: 'My First dataset',
          data: [15, 40, 55, 40, 25, 35, 40, 50, 85, 95, 54, 35],
        },
      ],
      chart: {
        type: 'bar',
        height: 370,
        toolbar: {
          show: false,
        },
        zoom: {
          enabled: false,
        },
      },
      plotOptions: {
        bar: {
          horizontal: false,
          columnWidth: '57%',
        },
      },
      states: {
        hover: {
          filter: {
            type: 'none',
            value: 0,
          },
        },
      },
      colors: ['#3ebcf2'],
      dataLabels: {
        enabled: false,
      },
      markers: {
        shape: 'circle',
      },
      legend: {
        show: false,
        fontSize: '12px',
        labels: {
          colors: '#000000',
        },
        markers: {
          width: 18,
          height: 18,
          strokeWidth: 0,
          strokeColor: '#fff',
          fillColors: undefined,
          radius: 12,
        },
      },
      stroke: {
        show: true,
        width: 12,
        colors: ['transparent'],
      },
      grid: {
        show: false,
        borderColor: '#3ebcf2',
      },
      xaxis: {
        labels: {
          show: false,
        },
        crosshairs: {
          show: false,
        },
      },
      yaxis: {
        show: false,
      },
      fill: {
        opacity: 1,
        colors: ['#3ebcf2'],
      },
      responsive: [
        {
          breakpoint: 575,
          options: {
            chart: {
              height: 250,
            },
          },
        },
      ],
    };
  }

  ngOnInit(): void {}
}
