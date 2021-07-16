import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-invoices-sent',
  templateUrl: './invoices-sent.component.html',
  styleUrls: ['./invoices-sent.component.css'],
})
export class InvoicesSentComponent implements OnInit {
  @Input() data: any;

  constructor() {}

  ngOnInit(): void {}
}
