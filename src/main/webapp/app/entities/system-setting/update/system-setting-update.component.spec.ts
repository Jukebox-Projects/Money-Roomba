jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SystemSettingService } from '../service/system-setting.service';
import { ISystemSetting, SystemSetting } from '../system-setting.model';

import { SystemSettingUpdateComponent } from './system-setting-update.component';

describe('Component Tests', () => {
  describe('SystemSetting Management Update Component', () => {
    let comp: SystemSettingUpdateComponent;
    let fixture: ComponentFixture<SystemSettingUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let systemSettingService: SystemSettingService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SystemSettingUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SystemSettingUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SystemSettingUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      systemSettingService = TestBed.inject(SystemSettingService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const systemSetting: ISystemSetting = { id: 456 };

        activatedRoute.data = of({ systemSetting });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(systemSetting));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SystemSetting>>();
        const systemSetting = { id: 123 };
        jest.spyOn(systemSettingService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ systemSetting });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: systemSetting }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(systemSettingService.update).toHaveBeenCalledWith(systemSetting);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SystemSetting>>();
        const systemSetting = new SystemSetting();
        jest.spyOn(systemSettingService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ systemSetting });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: systemSetting }));
        saveSubject.complete();

        // THEN
        expect(systemSettingService.create).toHaveBeenCalledWith(systemSetting);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SystemSetting>>();
        const systemSetting = { id: 123 };
        jest.spyOn(systemSettingService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ systemSetting });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(systemSettingService.update).toHaveBeenCalledWith(systemSetting);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
