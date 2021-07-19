import { Component, OnInit } from '@angular/core';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

interface transaction {
  id: string;
  date: string;
  recipient_image: string;
  recipient: string;
  recipient_post: string;
  url: string;
  amount: string;
  type: string;
  type_image: string;
  type_class: string;
  location: string;
  status: string;
  status_class: string;
  isSelected: boolean;
}

const TRANSACTIONS: transaction[] = [
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    recipient_image: 'assets/images/avatar/11.png',
    recipient: 'David Oconner',
    recipient_post: '',
    url: 'admin/transactions-details',
    amount: '$128.89',
    type: 'Income',
    type_image: 'assets/images/ic_in.svg',
    type_class: 'bgl-success',
    location: 'Medan, <br>Sumut Indonesia',
    status: 'Pending',
    status_class: 'btn-outline-warning',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    recipient_image: 'assets/images/avatar/17.png',
    recipient: 'Julia Esteh',
    recipient_post: '',
    url: 'admin/transactions-details',
    amount: '$128.89',
    type: 'Income',
    type_image: 'assets/images/ic_in.svg',
    type_class: 'bgl-success',
    location: 'Bangladesh,<br>India',
    status: 'Canceled',
    status_class: 'btn-outline-dark',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    recipient_image: 'assets/images/avatar/25.png',
    recipient: 'Clown Studio',
    recipient_post: 'Freelancer',
    url: 'admin/transactions-details',
    amount: '$560.67',
    type: 'Outcome',
    type_image: 'assets/images/ic_out.svg',
    type_class: 'bgl-danger',
    location: 'London, <br>England',
    status: 'Completed',
    status_class: 'btn-outline-success',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    recipient_image: 'assets/images/avatar/27.png',
    recipient: 'Jeremy Tedy',
    recipient_post: '',
    url: 'admin/transactions-details',
    amount: '$783.22',
    type: 'Income',
    type_image: 'assets/images/ic_in.svg',
    type_class: 'bgl-success',
    location: 'Medan, <br>Sumut Indonesia',
    status: 'Pending',
    status_class: 'btn-outline-warning',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    recipient_image: 'assets/images/avatar/23.png',
    recipient: 'Samuel Bro',
    recipient_post: '',
    url: 'admin/transactions-details',
    amount: '$128.89',
    type: 'Income',
    type_image: 'assets/images/ic_in.svg',
    type_class: 'bgl-success',
    location: 'Bangladesh, <br>India',
    status: 'Canceled',
    status_class: 'btn-outline-dark',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    recipient_image: 'assets/images/avatar/28.png',
    recipient: 'Yellow Caw',
    recipient_post: 'Studio',
    url: 'admin/transactions-details',
    amount: '$560.67',
    type: 'Income',
    type_image: 'assets/images/ic_in.svg',
    type_class: 'bgl-success',
    location: 'London,<br>England',
    status: 'Completed',
    status_class: 'btn-outline-success',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    recipient_image: 'assets/images/avatar/29.png',
    recipient: 'Cindy Seea',
    recipient_post: '',
    url: 'admin/transactions-details',
    amount: '$128.89',
    type: 'Outcome',
    type_image: 'assets/images/ic_out.svg',
    type_class: 'bgl-danger',
    location: 'Bangladesh, <br>India',
    status: 'Canceled',
    status_class: 'btn-outline-dark',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    recipient_image: 'assets/images/avatar/22.png',
    recipient: 'Nurkomariah',
    recipient_post: '',
    url: 'admin/transactions-details',
    amount: '$783.22',
    type: 'Income',
    type_image: 'assets/images/ic_in.svg',
    type_class: 'bgl-success',
    location: 'Bangladesh, <br>India',
    status: 'Canceled',
    status_class: 'btn-outline-dark',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    recipient_image: 'assets/images/avatar/25.png',
    recipient: 'XYZ Store ID',
    recipient_post: 'Online Shop',
    url: 'admin/transactions-details',
    amount: '$560.67',
    type: 'Outcome',
    type_image: 'assets/images/ic_out.svg',
    type_class: 'bgl-danger',
    location: 'London, <br>England',
    status: 'Completed',
    status_class: 'btn-outline-success',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    recipient_image: 'assets/images/avatar/20.png',
    recipient: 'Romeo Wayudi',
    recipient_post: '',
    url: 'admin/transactions-details',
    amount: '$783.22',
    type: 'Income',
    type_image: 'assets/images/ic_in.svg',
    type_class: 'bgl-success',
    location: 'Medan, <br>Sumut Indonesia',
    status: 'Pending',
    status_class: 'btn-outline-warning',
    isSelected: false,
  },
];

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css'],
})
export class TransactionsComponent implements OnInit {
  checkedTransactionList: any;

  isMasterSel: boolean;
  checkSingleItem: boolean = true;

  constructor(private modalService: NgbModal) {
    this.isMasterSel = false;

    this.updateTransactionListing();

    this.getCheckedItemList();
  }

  ngOnInit(): void {}

  page = 1;
  pageSize = 5;
  collectionSize = TRANSACTIONS.length;
  transactions: transaction[];

  updateTransactionListing() {
    this.transactions = TRANSACTIONS.map((customer, i) => ({ id: i + 1, ...customer })).slice(
      (this.page - 1) * this.pageSize,
      (this.page - 1) * this.pageSize + this.pageSize
    );
  }

  /* Check Uncheck all checkbox on main check box click*/

  checkUncheckAll() {
    for (var i = 0; i < this.transactions.length; i++) {
      this.transactions[i].isSelected = this.isMasterSel;
    }
    this.getCheckedItemList();
  }

  isAllSelected() {
    this.isMasterSel = this.transactions.every(function (item: any) {
      return item.isSelected == true;
    });
    this.getCheckedItemList();
  }

  getCheckedItemList() {
    this.checkedTransactionList = [];

    for (var i = 0; i < this.transactions.length; i++) {
      if (this.transactions[i].isSelected) this.checkedTransactionList.push(this.transactions[i]);
      else this.checkSingleItem = false;
    }

    if (this.checkSingleItem) {
      this.isMasterSel = true;
    }
    this.checkedTransactionList = JSON.stringify(this.checkedTransactionList);
  }

  open(content) {
    this.modalService.open(content);
  }
}
