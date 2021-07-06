jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISystemSetting, SystemSetting } from '../system-setting.model';
import { SystemSettingService } from '../service/system-setting.service';

import { SystemSettingRoutingResolveService } from './system-setting-routing-resolve.service';

describe('Service Tests', () => {
  describe('SystemSetting routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SystemSettingRoutingResolveService;
    let service: SystemSettingService;
    let resultSystemSetting: ISystemSetting | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SystemSettingRoutingResolveService);
      service = TestBed.inject(SystemSettingService);
      resultSystemSetting = undefined;
    });

    describe('resolve', () => {
      it('should return ISystemSetting returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSystemSetting = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSystemSetting).toEqual({ id: 123 });
      });

      it('should return new ISystemSetting if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSystemSetting = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSystemSetting).toEqual(new SystemSetting());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SystemSetting })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSystemSetting = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSystemSetting).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
