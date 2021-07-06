jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IScheduledTransaction, ScheduledTransaction } from '../scheduled-transaction.model';
import { ScheduledTransactionService } from '../service/scheduled-transaction.service';

import { ScheduledTransactionRoutingResolveService } from './scheduled-transaction-routing-resolve.service';

describe('Service Tests', () => {
  describe('ScheduledTransaction routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ScheduledTransactionRoutingResolveService;
    let service: ScheduledTransactionService;
    let resultScheduledTransaction: IScheduledTransaction | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ScheduledTransactionRoutingResolveService);
      service = TestBed.inject(ScheduledTransactionService);
      resultScheduledTransaction = undefined;
    });

    describe('resolve', () => {
      it('should return IScheduledTransaction returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultScheduledTransaction = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultScheduledTransaction).toEqual({ id: 123 });
      });

      it('should return new IScheduledTransaction if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultScheduledTransaction = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultScheduledTransaction).toEqual(new ScheduledTransaction());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ScheduledTransaction })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultScheduledTransaction = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultScheduledTransaction).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
