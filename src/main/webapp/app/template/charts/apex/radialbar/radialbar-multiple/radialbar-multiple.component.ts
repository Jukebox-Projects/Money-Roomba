import { Component, OnInit, ViewChild } from '@angular/core';
import { ChartComponent } from 'ng-apexcharts';

import { ApexNonAxisChartSeries, ApexPlotOptions, ApexChart } from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  labels: string[];
  plotOptions: ApexPlotOptions;
};

@Component({
  selector: 'app-radialbar-multiple',
  templateUrl: './radialbar-multiple.component.html',
  styleUrls: ['./radialbar-multiple.component.css'],
})
export class RadialbarMultipleComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  constructor() {
    this.chartOptions = {
      series: [44, 55, 67, 83],
      chart: {
        height: 350,
        type: 'radialBar',
      },
      plotOptions: {
        radialBar: {
          dataLabels: {
            name: {
              fontSize: '22px',
            },
            value: {
              fontSize: '16px',
            },
            total: {
              show: true,
              label: 'Total',
              formatter: function (w) {
                return '249';
              },
            },
          },
        },
      },
      labels: ['Apples', 'Oranges', 'Bananas', 'Berries'],
    };
  }

  ngOnInit(): void {}
}
