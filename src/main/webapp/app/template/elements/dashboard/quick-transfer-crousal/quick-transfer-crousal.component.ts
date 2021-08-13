import { Component, OnInit, Input } from '@angular/core';
import { OwlOptions } from 'ngx-owl-carousel-o';
import { ContactService } from '../../../../entities/contact/service/contact.service';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { IContact } from '../../../../entities/contact/contact.model';
import { AddContactModalComponent } from '../../../../entities/contact/add-contact-modal/add-contact-modal/add-contact-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { faCaretLeft } from '@fortawesome/free-solid-svg-icons';
import { faCaretRight } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-quick-transfer-crousal',
  templateUrl: './quick-transfer-crousal.component.html',
  styleUrls: ['./quick-transfer-crousal.component.css'],
})
export class QuickTransferCrousalComponent implements OnInit {
  @Input() data: any;
  contacts?: IContact[];
  faCaretLeft = faCaretLeft;
  faCaretRight = faCaretRight;

  constructor(protected contactService: ContactService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.contactService.query().subscribe((res: HttpResponse<IContact[]>) => {
      this.contacts = res.body ?? [];
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  open(): void {
    const modalRef = this.modalService.open(AddContactModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.closed.subscribe(reason => {
      if (reason === 'activated') {
        setTimeout(() => {
          window.location.reload();
        }, 3500);
      }
    });
  }

  customOptions: OwlOptions = {
    loop: true,
    autoplay: true,
    margin: 10,
    nav: false,
    center: true,
    dots: false,
    navText: ['<', '>'],
    responsive: {
      0: {
        items: 2,
      },
      400: {
        items: 3,
      },
      450: {
        items: 4,
      },
      560: {
        items: 5,
      },
      700: {
        items: 5,
      },
    },
  };
}
