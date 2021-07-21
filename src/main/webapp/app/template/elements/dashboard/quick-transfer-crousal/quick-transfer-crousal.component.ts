import { Component, OnInit, Input } from '@angular/core';
import { OwlOptions } from 'ngx-owl-carousel-o';

@Component({
  selector: 'app-quick-transfer-crousal',
  templateUrl: './quick-transfer-crousal.component.html',
  styleUrls: ['./quick-transfer-crousal.component.css'],
})
export class QuickTransferCrousalComponent implements OnInit {
  @Input() data: any;

  constructor() {}

  ngOnInit(): void {}

  customOptions: OwlOptions = {
    loop: true,
    autoplay: true,
    margin: 10,
    nav: false,
    center: true,
    dots: false,
    navText: ['<i class="fa fa-caret-left"></i>', '<i class="fa fa-caret-right"></i>'],
    responsive: {
      0: {
        items: 2,
      },
      400: {
        items: 3,
      },
      450: {
        items: 4,
      },
      560: {
        items: 5,
      },
      700: {
        items: 5,
      },
    },
  };
}
