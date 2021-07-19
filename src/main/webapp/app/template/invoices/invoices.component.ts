import { Component, OnInit } from '@angular/core';

interface invoice {
  id: string;
  date: string;
  image: string;
  title: string;
  subtitle: string;
  email: string;
  service_type: string;
  status: string;
  status_class: string;
  isSelected: boolean;
}

const INVOICES: invoice[] = [
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    image: 'assets/images/avatar/25.png',
    title: 'XYZ Store ID',
    subtitle: 'Online Shop',
    email: 'xyzstore@mail.com',
    service_type: 'Server Maintenance',
    status: 'Completed',
    status_class: 'btn-success',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    image: 'assets/images/avatar/19.png',
    title: 'David Oconner',
    subtitle: '',
    email: 'davidocon@mail.com',
    service_type: 'Clean Up',
    status: 'Pending',
    status_class: 'btn-warning',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    image: 'assets/images/avatar/20.png',
    title: 'Julia Esteh',
    subtitle: '',
    email: 'juliaesteh@mail.com',
    service_type: 'Upgrade Component',
    status: 'Canceled',
    status_class: 'btn-dark',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    image: 'assets/images/avatar/26.png',
    title: 'Power Supp Store',
    subtitle: 'Online Shop',
    email: 'xyzstore@mail.com',
    service_type: 'Server Maintenance',
    status: 'Completed',
    status_class: 'btn-success',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    image: 'assets/images/avatar/21.png',
    title: 'James Known',
    subtitle: '',
    email: 'davidocon@mail.com',
    service_type: 'Clean Up',
    status: 'Pending',
    status_class: 'btn-warning',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    image: 'assets/images/avatar/22.png',
    title: 'Rock Lee',
    subtitle: '',
    email: 'juliaesteh@mail.com',
    service_type: 'Upgrade Component',
    status: 'Canceled',
    status_class: 'btn-dark',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    image: 'assets/images/avatar/23.png',
    title: 'Geovanny Jr.',
    subtitle: '',
    email: 'davidocon@mail.com',
    service_type: 'Clean Up',
    status: 'Pending',
    status_class: 'btn-warning',
    isSelected: false,
  },
  {
    id: '#123412451',
    date: '#June 1, 2020, 08:22 AM',
    image: 'assets/images/avatar/24.png',
    title: 'Bella Ingrid',
    subtitle: '',
    email: 'juliaesteh@mail.com',
    service_type: 'Upgrade Component',
    status: 'Canceled',
    status_class: 'btn-dark',
    isSelected: false,
  },
];

@Component({
  selector: 'app-invoices',
  templateUrl: './invoices.component.html',
  styleUrls: ['./invoices.component.css'],
})
export class InvoicesComponent implements OnInit {
  checkedInvoiceList: any;

  isMasterSel: boolean;
  checkSingleItem: boolean = true;

  constructor() {
    this.isMasterSel = false;

    this.updateInvoiceListing();

    this.getCheckedItemList();
  }

  ngOnInit(): void {}

  page = 1;
  pageSize = 5;
  collectionSize = INVOICES.length;
  invoices: invoice[];

  updateInvoiceListing() {
    this.invoices = INVOICES.map((customer, i) => ({ id: i + 1, ...customer })).slice(
      (this.page - 1) * this.pageSize,
      (this.page - 1) * this.pageSize + this.pageSize
    );
  }

  /* Check Uncheck all checkbox on main check box click*/

  checkUncheckAll() {
    for (var i = 0; i < this.invoices.length; i++) {
      this.invoices[i].isSelected = this.isMasterSel;
    }
    this.getCheckedItemList();
  }

  isAllSelected() {
    this.isMasterSel = this.invoices.every(function (item: any) {
      return item.isSelected == true;
    });
    this.getCheckedItemList();
  }

  getCheckedItemList() {
    this.checkedInvoiceList = [];

    for (var i = 0; i < this.invoices.length; i++) {
      if (this.invoices[i].isSelected) this.checkedInvoiceList.push(this.invoices[i]);
      else this.checkSingleItem = false;
    }

    if (this.checkSingleItem) {
      this.isMasterSel = true;
    }
    this.checkedInvoiceList = JSON.stringify(this.checkedInvoiceList);
  }
}
