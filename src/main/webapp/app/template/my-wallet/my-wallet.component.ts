import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-my-wallet',
  templateUrl: './my-wallet.component.html',
  styleUrls: ['./my-wallet.component.css'],
})
export class MyWalletComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}

  invoices = [
    {
      name: 'FSoziety',
      timeago: '4h ago',
      price: '$45',
      image: 'assets/images/avatar/15.png',
      url: 'admin/invoices',
    },
    {
      name: 'David Ignis',
      timeago: '7h ago',
      price: '$672',
      image: 'assets/images/avatar/17.png',
      url: 'admin/invoices',
    },
    {
      name: 'Olivia Johan..',
      timeago: '4h ago',
      price: '$769',
      image: 'assets/images/avatar/18.png',
      url: 'admin/invoices',
    },
    {
      name: 'Stevan Store',
      timeago: '4h ago',
      price: '$562',
      image: 'assets/images/avatar/16.png',
      url: 'admin/invoices',
    },
    {
      name: 'Kidz Zoo',
      timeago: '4h ago',
      price: '$776',
      image: 'assets/images/avatar/17.png',
      url: 'admin/invoices',
    },
  ];
}
