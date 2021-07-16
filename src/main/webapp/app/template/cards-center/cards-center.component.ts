import { Component, OnInit } from '@angular/core';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-cards-center',
  templateUrl: './cards-center.component.html',
  styleUrls: ['./cards-center.component.css'],
})
export class CardsCenterComponent implements OnInit {
  constructor(private modalService: NgbModal) {}

  ngOnInit(): void {}

  cardsArray = [
    {
      image: 'assets/images/card/card1.png',
      card_title: 'Main Balance',
      price: '$673,412.66',
      card_no: '**** **** **** 1234',
      valid_date: '08/21',
      name: 'Franklin Jr.',
    },
    {
      image: 'assets/images/card/card2.png',
      card_title: 'Purple Card',
      price: '$45,662',
      card_no: '**** **** **** 1234',
      valid_date: '08/21',
      name: 'Franklin Jr.',
    },
    {
      image: 'assets/images/card/card3.png',
      card_title: 'Green Card',
      price: '$23,511',
      card_no: '**** **** **** 1234',
      valid_date: '08/21',
      name: 'Franklin Jr.',
    },
    {
      image: 'assets/images/card/card4.png',
      card_title: 'Orange Card',
      price: '$340',
      card_no: '**** **** **** 1234',
      valid_date: '08/21',
      name: 'Franklin Jr.',
    },
  ];

  cardsListArray = [
    {
      image: 'assets/images/card/1.jpg',
      card_type: 'Primary',
      bank: 'ABC Bank',
      card_no: '**** **** **** 2256',
      name: 'Franklin Jr.',
      url: 'admin/transactions-details',
    },
    {
      image: 'assets/images/card/2.jpg',
      card_type: 'Secondary',
      bank: 'Finefine Bank',
      card_no: '**** **** **** 6551',
      name: 'Franklin Jr.',
      url: 'admin/transactions-details',
    },
    {
      image: 'assets/images/card/3.jpg',
      card_type: 'Secondary',
      bank: 'Makan Bank',
      card_no: '**** **** **** 6783',
      name: 'Franklin Jr.',
      url: 'admin/transactions-details',
    },
    {
      image: 'assets/images/card/4.jpg',
      card_type: 'Secondary',
      bank: 'Where Bank',
      card_no: '**** **** **** 8843',
      name: 'Franklin Jr.',
      url: 'admin/transactions-details',
    },
  ];

  open(content) {
    this.modalService.open(content);
  }
}
