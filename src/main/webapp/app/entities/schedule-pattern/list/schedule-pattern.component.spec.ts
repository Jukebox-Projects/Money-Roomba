import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SchedulePatternService } from '../service/schedule-pattern.service';

import { SchedulePatternComponent } from './schedule-pattern.component';

describe('Component Tests', () => {
  describe('SchedulePattern Management Component', () => {
    let comp: SchedulePatternComponent;
    let fixture: ComponentFixture<SchedulePatternComponent>;
    let service: SchedulePatternService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SchedulePatternComponent],
      })
        .overrideTemplate(SchedulePatternComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SchedulePatternComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SchedulePatternService);

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
      expect(comp.schedulePatterns?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
