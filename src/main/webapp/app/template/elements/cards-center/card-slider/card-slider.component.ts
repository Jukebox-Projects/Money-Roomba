import { Component, OnInit, Input } from '@angular/core';
import { OwlOptions } from 'ngx-owl-carousel-o';

@Component({
  selector: 'app-card-slider',
  templateUrl: './card-slider.component.html',
  styleUrls: ['./card-slider.component.css'],
})
export class CardSliderComponent implements OnInit {
  @Input() data: any;

  customOptions: OwlOptions;

  constructor() {}

  ngOnInit() {
    setTimeout(() => {
      this.customOptions = {
        loop: true,
        margin: 30,
        nav: true,
        center: true,
        autoplay: false,
        dots: false,
        navText: ['', ''],
        responsiveRefreshRate: 100,
        autoWidth: true,
        // items:3
      };
    }, 300);
  }
}
