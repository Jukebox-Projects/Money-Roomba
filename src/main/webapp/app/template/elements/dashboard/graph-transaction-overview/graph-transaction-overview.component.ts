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
  selector: 'app-graph-transaction-overview',
  templateUrl: './graph-transaction-overview.component.html',
  styleUrls: ['./graph-transaction-overview.component.css'],
})
export class GraphTransactionOverviewComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  constructor() {
    this.chartOptions = {
      series: [
        {
          name: 'Running',
          data: [50, 18, 70, 40, 90, 70, 20],
        },
        {
          name: 'Cycling',
          data: [80, 40, 55, 20, 45, 30, 80],
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
          borderRadius: 10,
          // endingShape: 'rounded'
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
      colors: ['#D2D2D2', '#EBEBEB'],
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
        width: 4,
        colors: ['transparent'],
      },
      grid: {
        borderColor: '#eee',
      },
      xaxis: {
        categories: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
        labels: {
          style: {
            colors: '#787878',
            fontSize: '13px',
            fontFamily: 'poppins',
            fontWeight: 100,
            cssClass: 'apexcharts-xaxis-label',
          },
        },
        crosshairs: {
          show: false,
        },
      },
      yaxis: {
        labels: {
          offsetX: -16,
          style: {
            colors: '#787878',
            fontSize: '13px',
            fontFamily: 'poppins',
            fontWeight: 100,
            cssClass: 'apexcharts-xaxis-label',
          },
        },
      },
      fill: {
        opacity: 1,
        colors: ['#D2D2D2', '#EBEBEB'],
      },
      tooltip: {
        y: {
          formatter: function (val) {
            return '$ ' + val + ' thousands';
          },
        },
      },
      responsive: [
        {
          breakpoint: 575,
          options: {
            series: [
              {
                name: 'Running',
                data: [50, 18, 70, 40, 90, 70, 20],
              },
              {
                name: 'Cycling',
                data: [80, 40, 55, 20, 45, 30, 80],
              },
            ],
            chart: {
              height: 250,
            },
            plotOptions: {
              bar: {
                columnWidth: '65%',
              },
            },
            xaxis: {
              categories: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
            },
          },
        },
      ],
    };
  }

  ngOnInit(): void {}
}
