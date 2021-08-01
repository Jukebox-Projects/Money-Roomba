import { Component, OnInit, Input } from '@angular/core';
import { OwlOptions } from 'ngx-owl-carousel-o';
import { IWallet } from '../../wallet.model';
import { IICon } from '../../../../shared/icon-picker/icon.model';
import { IconService } from '../../../../shared/icon-picker/service/icon.service';
@Component({
  selector: 'app-wallet-slider',
  templateUrl: './wallet-slider.component.html',
  styleUrls: ['./wallet-slider.component.scss'],
})
export class WalletSliderComponent implements OnInit {
  @Input() walletsCarousel: IWallet[];

  customOptions: OwlOptions;

  constructor(protected iconService: IconService) {}

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
    }, 1);
  }
  getIcon(iconId: number): IICon {
    return this.iconService.getIcon(iconId);
  }
}
