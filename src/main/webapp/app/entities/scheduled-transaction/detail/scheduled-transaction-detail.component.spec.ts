import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ScheduledTransactionDetailComponent } from './scheduled-transaction-detail.component';

describe('Component Tests', () => {
  describe('ScheduledTransaction Management Detail Component', () => {
    let comp: ScheduledTransactionDetailComponent;
    let fixture: ComponentFixture<ScheduledTransactionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ScheduledTransactionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ scheduledTransaction: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ScheduledTransactionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ScheduledTransactionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load scheduledTransaction on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.scheduledTransaction).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
