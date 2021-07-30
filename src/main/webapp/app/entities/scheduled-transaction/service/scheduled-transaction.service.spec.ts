import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { MovementType } from 'app/entities/enumerations/movement-type.model';
import { RecurringType } from 'app/entities/enumerations/recurring-type.model';
import { IScheduledTransaction, ScheduledTransaction } from '../scheduled-transaction.model';

import { ScheduledTransactionService } from './scheduled-transaction.service';

describe('Service Tests', () => {
  describe('ScheduledTransaction Service', () => {
    let service: ScheduledTransactionService;
    let httpMock: HttpTestingController;
    let elemDefault: IScheduledTransaction;
    let expectedResult: IScheduledTransaction | IScheduledTransaction[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ScheduledTransactionService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        originalAmount: 0,
        movementType: MovementType.INCOME,
        startDate: currentDate,
        endDate: currentDate,
        addToReports: false,
        recurringType: RecurringType.DAILY,
        separationCount: 0,
        maxNumberOfOcurrences: 0,
        dayOfWeek: 0,
        weekOfMonth: 0,
        dayOfMonth: 0,
        monthOfYear: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ScheduledTransaction', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.create(new ScheduledTransaction()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ScheduledTransaction', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            originalAmount: 1,
            movementType: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            addToReports: true,
            recurringType: 'BBBBBB',
            separationCount: 1,
            maxNumberOfOcurrences: 1,
            dayOfWeek: 1,
            weekOfMonth: 1,
            dayOfMonth: 1,
            monthOfYear: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ScheduledTransaction', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
            originalAmount: 1,
            addToReports: true,
            recurringType: 'BBBBBB',
            maxNumberOfOcurrences: 1,
            dayOfWeek: 1,
            dayOfMonth: 1,
            monthOfYear: 1,
          },
          new ScheduledTransaction()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ScheduledTransaction', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            originalAmount: 1,
            movementType: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            addToReports: true,
            recurringType: 'BBBBBB',
            separationCount: 1,
            maxNumberOfOcurrences: 1,
            dayOfWeek: 1,
            weekOfMonth: 1,
            dayOfMonth: 1,
            monthOfYear: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ScheduledTransaction', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addScheduledTransactionToCollectionIfMissing', () => {
        it('should add a ScheduledTransaction to an empty array', () => {
          const scheduledTransaction: IScheduledTransaction = { id: 123 };
          expectedResult = service.addScheduledTransactionToCollectionIfMissing([], scheduledTransaction);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(scheduledTransaction);
        });

        it('should not add a ScheduledTransaction to an array that contains it', () => {
          const scheduledTransaction: IScheduledTransaction = { id: 123 };
          const scheduledTransactionCollection: IScheduledTransaction[] = [
            {
              ...scheduledTransaction,
            },
            { id: 456 },
          ];
          expectedResult = service.addScheduledTransactionToCollectionIfMissing(scheduledTransactionCollection, scheduledTransaction);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ScheduledTransaction to an array that doesn't contain it", () => {
          const scheduledTransaction: IScheduledTransaction = { id: 123 };
          const scheduledTransactionCollection: IScheduledTransaction[] = [{ id: 456 }];
          expectedResult = service.addScheduledTransactionToCollectionIfMissing(scheduledTransactionCollection, scheduledTransaction);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(scheduledTransaction);
        });

        it('should add only unique ScheduledTransaction to an array', () => {
          const scheduledTransactionArray: IScheduledTransaction[] = [{ id: 123 }, { id: 456 }, { id: 95175 }];
          const scheduledTransactionCollection: IScheduledTransaction[] = [{ id: 123 }];
          expectedResult = service.addScheduledTransactionToCollectionIfMissing(
            scheduledTransactionCollection,
            ...scheduledTransactionArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const scheduledTransaction: IScheduledTransaction = { id: 123 };
          const scheduledTransaction2: IScheduledTransaction = { id: 456 };
          expectedResult = service.addScheduledTransactionToCollectionIfMissing([], scheduledTransaction, scheduledTransaction2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(scheduledTransaction);
          expect(expectedResult).toContain(scheduledTransaction2);
        });

        it('should accept null and undefined values', () => {
          const scheduledTransaction: IScheduledTransaction = { id: 123 };
          expectedResult = service.addScheduledTransactionToCollectionIfMissing([], null, scheduledTransaction, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(scheduledTransaction);
        });

        it('should return initial array if no ScheduledTransaction is added', () => {
          const scheduledTransactionCollection: IScheduledTransaction[] = [{ id: 123 }];
          expectedResult = service.addScheduledTransactionToCollectionIfMissing(scheduledTransactionCollection, undefined, null);
          expect(expectedResult).toEqual(scheduledTransactionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
