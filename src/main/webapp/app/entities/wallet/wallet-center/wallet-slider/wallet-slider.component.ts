import { Component, OnInit, Input } from '@angular/core';
//import { OwlOptions } from 'ngx-owl-carousel-o';

@Component({
  selector: 'app-waller-slider',
  templateUrl: './wallet-slider.component.html',
  styleUrls: ['./wallet-slider.component.scss'],
})
export class WalletSliderComponent implements OnInit {
  @Input() data: any;

  //customOptions: OwlOptions;

  constructor() {}

  ngOnInit() {
    /*setTimeout(() => {

      this.customOptions = {
        loop:true,
        margin:30,
        nav:true,
        center:true,
        autoplay:false,
        dots: false,
        navText: ['', ''],
        responsiveRefreshRate: 100,
        autoWidth:true,
        // items:3


      }


    }, 300);*/
  }
}
