import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['../../../assets/landing/styles/styles.css'],
})
export class LandingPageComponent implements OnInit {
  myScriptElement: HTMLScriptElement;
  constructor() {
    this.myScriptElement = document.createElement('script');
    this.myScriptElement.src = '../../../assets/landing/scripts/main.js';
    document.body.appendChild(this.myScriptElement);
  }

  ngOnInit(): void {}
}
