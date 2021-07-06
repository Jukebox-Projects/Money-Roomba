import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SchedulePatternDetailComponent } from './schedule-pattern-detail.component';

describe('Component Tests', () => {
  describe('SchedulePattern Management Detail Component', () => {
    let comp: SchedulePatternDetailComponent;
    let fixture: ComponentFixture<SchedulePatternDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SchedulePatternDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ schedulePattern: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SchedulePatternDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SchedulePatternDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load schedulePattern on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.schedulePattern).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
