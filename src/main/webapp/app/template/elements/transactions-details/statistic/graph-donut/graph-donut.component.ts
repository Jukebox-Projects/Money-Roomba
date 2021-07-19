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
  selector: 'app-graph-donut',
  templateUrl: './graph-donut.component.html',
  styleUrls: ['./graph-donut.component.css'],
})
export class GraphDonutComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  constructor() {
    this.chartOptions = {
      series: [85],
      chart: {
        height: 300,
        type: 'radialBar',
        zoom: {
          enabled: true,
        },
        toolbar: {
          show: false,
        },
      },
      plotOptions: {
        radialBar: {
          hollow: {
            margin: 0,
            size: '30%',
            background: '#fff',
            image: undefined,
            imageOffsetX: 0,
            imageOffsetY: 0,
            position: 'front',
            dropShadow: {
              enabled: true,
              top: 3,
              left: 0,
              blur: 4,
              opacity: 0.24,
            },
          },
          track: {
            background: '#E5E5E5',
            strokeWidth: '45%',
            margin: 0, // margin is in pixels
          },

          dataLabels: {
            show: true,
            name: {
              offsetY: -10,
              show: true,
              color: '#888',
              fontSize: '17px',
            },
            value: {
              offsetY: -5,
              color: '#111',
              fontSize: '24px',
              show: true,
            },
          },
        },
      },
      /* fill: {
                colors: ['#a837cf'],
            },
            stroke: {
            },*/
      labels: [''],
      responsive: [
        {
          breakpoint: 575,
          options: {
            chart: {
              height: 90,
            },
          },
        },
      ],
    };
  }

  ngOnInit(): void {}
}
