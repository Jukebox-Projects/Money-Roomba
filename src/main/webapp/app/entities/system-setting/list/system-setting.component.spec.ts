import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SystemSettingService } from '../service/system-setting.service';

import { SystemSettingComponent } from './system-setting.component';

describe('Component Tests', () => {
  describe('SystemSetting Management Component', () => {
    let comp: SystemSettingComponent;
    let fixture: ComponentFixture<SystemSettingComponent>;
    let service: SystemSettingService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SystemSettingComponent],
      })
        .overrideTemplate(SystemSettingComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SystemSettingComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SystemSettingService);

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
      expect(comp.systemSettings?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
