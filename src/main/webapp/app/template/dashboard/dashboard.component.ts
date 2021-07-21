import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}

  quickTransfer = [
    {
      name: 'David',
      username: '@davidxc',
      image: 'assets/images/avatar/1.jpg',
      url: 'admin/transactions-details',
    },
    {
      name: 'Cindy',
      username: '@cindyss',
      image: 'assets/images/avatar/2.jpg',
      url: 'admin/transactions-details',
    },
    {
      name: 'Samuel',
      username: '@sam224',
      image: 'assets/images/avatar/3.jpg',
      url: 'admin/transactions-details',
    },
    {
      name: 'Olivia',
      username: '@oliv62',
      image: 'assets/images/avatar/4.jpg',
      url: 'admin/transactions-details',
    },
    {
      name: 'Martha',
      username: '@marthaa',
      image: 'assets/images/avatar/5.jpg',
      url: 'admin/transactions-details',
    },
  ];
}
