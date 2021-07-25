import { Component, OnInit } from '@angular/core';
/*
import '../../../assets/js/jquery.min.js';
import '../../../assets/js/jquery-migrate-3.0.1.min.js';
import '../../../assets/js/jquery.waypoints.min.js';
import '../../../assets/js/jquery.stellar.min.js';
import '../../../assets/js/owl.carousel.min.js';
import '../../../assets/js/jquery.magnific-popup.min.js';
import '../../../assets/js/aos.js';
import '../../../assets/js/bootstrap-datepicker.js';
import '../../../assets/js/jquery.timepicker.min.js';
import '../../../assets/js/main.js';*/

@Component({
  selector: 'jhi-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: [
    '../../../assets/css/landing/animate.css',
    '../../../assets/css/landing/aos.css',
    '../../../assets/css/landing/bootstrap.min.css',
    '../../../assets/css/landing/bootstrap-datepicker.css',
    '../../../assets/css/landing/flaticon.css',
    '../../../assets/css/landing/icomoon.css',
    '../../../assets/css/landing/ionicons.min.css',
    '../../../assets/css/landing/jquery.timepicker.css',
    '../../../assets/css/landing/magnific-popup.css',
    '../../../assets/css/landing/open-iconic-bootstrap.min.css',
    '../../../assets/css/landing/owl.carousel.min.css',
    '../../../assets/css/landing/owl.theme.default.min.css',
    '../../../assets/css/landing/style.css',
  ],
})
export class LandingPageComponent implements OnInit {
  myScriptElement: HTMLScriptElement;
  myScriptElement1: HTMLScriptElement;
  myScriptElement2: HTMLScriptElement;
  myScriptElement3: HTMLScriptElement;
  myScriptElement4: HTMLScriptElement;
  myScriptElement5: HTMLScriptElement;
  myScriptElement6: HTMLScriptElement;
  myScriptElement7: HTMLScriptElement;
  myScriptElement8: HTMLScriptElement;
  myScriptElement9: HTMLScriptElement;
  constructor() {
    this.myScriptElement = document.createElement('script');
    this.myScriptElement1 = document.createElement('script');
    this.myScriptElement2 = document.createElement('script');
    this.myScriptElement3 = document.createElement('script');
    this.myScriptElement4 = document.createElement('script');
    this.myScriptElement5 = document.createElement('script');
    this.myScriptElement6 = document.createElement('script');
    this.myScriptElement7 = document.createElement('script');
    this.myScriptElement8 = document.createElement('script');
    this.myScriptElement9 = document.createElement('script');

    this.myScriptElement.src = '../../../assets/js/jquery.min.js';
    this.myScriptElement1.src = '../../../assets/js/jquery-migrate-3.0.1.min.js';
    this.myScriptElement2.src = '../../../assets/js/jquery.waypoints.min.js';
    this.myScriptElement3.src = '../../../assets/js/jquery.stellar.min.js';
    this.myScriptElement4.src = '../../../assets/js/owl.carousel.min.js';
    this.myScriptElement5.src = '../../../assets/js/jquery.magnific-popup.min.js';
    this.myScriptElement6.src = '../../../assets/js/aos.js';
    this.myScriptElement7.src = '../../../assets/js/bootstrap-datepicker.js';
    this.myScriptElement8.src = '../../../assets/css/landing/icomoon.css';
    this.myScriptElement9.src = '../../../assets/js/main.js';

    document.body.appendChild(this.myScriptElement);
    document.body.appendChild(this.myScriptElement1);
    document.body.appendChild(this.myScriptElement2);
    document.body.appendChild(this.myScriptElement3);
    document.body.appendChild(this.myScriptElement4);
    document.body.appendChild(this.myScriptElement5);
    document.body.appendChild(this.myScriptElement6);
    document.body.appendChild(this.myScriptElement7);
    document.body.appendChild(this.myScriptElement8);
    document.body.appendChild(this.myScriptElement9);
  }

  ngOnInit(): void {}
}
