import { Component, OnInit, ViewChild } from '@angular/core';

import { ChartComponent, ApexNonAxisChartSeries, ApexPlotOptions, ApexChart, ApexFill, ApexStroke, ApexResponsive } from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  labels: string[];
  plotOptions: ApexPlotOptions;
  fill: ApexFill;
  stroke: ApexStroke;
  responsive: ApexResponsive[];
};

@Component({
  selector: 'app-wallet-property',
  templateUrl: './wallet-property.component.html',
  styleUrls: ['./wallet-property.component.css'],
})
export class WalletPropertyComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  constructor() {
    this.chartOptions = {
      series: [34],
      chart: {
        height: 80,
        width: 80,
        type: 'radialBar',
        zoom: {
          enabled: true,
        },
        toolbar: {
          show: false,
        },
        sparkline: {
          enabled: true,
        },
      },
      plotOptions: {
        radialBar: {
          hollow: {
            margin: 0,
            size: '50%',
            background: '#fff',
            dropShadow: {
              enabled: true,
              top: 0,
              left: 0,
              blur: 3,
              opacity: 0.1,
            },
          },
          track: {
            background: '#f4f2fe',
            strokeWidth: '70%',
            margin: 0, // margin is in pixels
          },

          dataLabels: {
            show: true,
            value: {
              offsetY: -10,
              color: '#461EE7',
              fontSize: '14px',
              show: true,
            },
          },
        },
      },
      fill: {
        colors: ['#441de3'],
      },
      stroke: {},
      labels: [''],
      responsive: [
        {
          breakpoint: 575,
          options: {
            chart: {
              height: 80,
              width: 80,
            },
          },
        },
      ],
    };
  }

  ngOnInit(): void {}
}
