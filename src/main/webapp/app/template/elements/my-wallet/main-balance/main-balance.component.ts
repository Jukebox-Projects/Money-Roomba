import { Component, OnInit } from '@angular/core';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-main-balance',
  templateUrl: './main-balance.component.html',
  styleUrls: ['./main-balance.component.css'],
})
export class MainBalanceComponent implements OnInit {
  constructor(private modalService: NgbModal) {}

  ngOnInit(): void {}

  open(content) {
    this.modalService.open(content);
  }
}
