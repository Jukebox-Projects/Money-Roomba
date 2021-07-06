import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ScheduledTransactionService } from '../service/scheduled-transaction.service';

import { ScheduledTransactionComponent } from './scheduled-transaction.component';

describe('Component Tests', () => {
  describe('ScheduledTransaction Management Component', () => {
    let comp: ScheduledTransactionComponent;
    let fixture: ComponentFixture<ScheduledTransactionComponent>;
    let service: ScheduledTransactionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ScheduledTransactionComponent],
      })
        .overrideTemplate(ScheduledTransactionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ScheduledTransactionComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ScheduledTransactionService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.scheduledTransactions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
