import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { RecurringType } from 'app/entities/enumerations/recurring-type.model';
import { ISchedulePattern, SchedulePattern } from '../schedule-pattern.model';

import { SchedulePatternService } from './schedule-pattern.service';

describe('Service Tests', () => {
  describe('SchedulePattern Service', () => {
    let service: SchedulePatternService;
    let httpMock: HttpTestingController;
    let elemDefault: ISchedulePattern;
    let expectedResult: ISchedulePattern | ISchedulePattern[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SchedulePatternService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
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
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SchedulePattern', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new SchedulePattern()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SchedulePattern', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
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

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SchedulePattern', () => {
        const patchObject = Object.assign(
          {
            maxNumberOfOcurrences: 1,
            dayOfWeek: 1,
            dayOfMonth: 1,
            monthOfYear: 1,
          },
          new SchedulePattern()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SchedulePattern', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
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

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SchedulePattern', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSchedulePatternToCollectionIfMissing', () => {
        it('should add a SchedulePattern to an empty array', () => {
          const schedulePattern: ISchedulePattern = { id: 123 };
          expectedResult = service.addSchedulePatternToCollectionIfMissing([], schedulePattern);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schedulePattern);
        });

        it('should not add a SchedulePattern to an array that contains it', () => {
          const schedulePattern: ISchedulePattern = { id: 123 };
          const schedulePatternCollection: ISchedulePattern[] = [
            {
              ...schedulePattern,
            },
            { id: 456 },
          ];
          expectedResult = service.addSchedulePatternToCollectionIfMissing(schedulePatternCollection, schedulePattern);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SchedulePattern to an array that doesn't contain it", () => {
          const schedulePattern: ISchedulePattern = { id: 123 };
          const schedulePatternCollection: ISchedulePattern[] = [{ id: 456 }];
          expectedResult = service.addSchedulePatternToCollectionIfMissing(schedulePatternCollection, schedulePattern);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schedulePattern);
        });

        it('should add only unique SchedulePattern to an array', () => {
          const schedulePatternArray: ISchedulePattern[] = [{ id: 123 }, { id: 456 }, { id: 55403 }];
          const schedulePatternCollection: ISchedulePattern[] = [{ id: 123 }];
          expectedResult = service.addSchedulePatternToCollectionIfMissing(schedulePatternCollection, ...schedulePatternArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const schedulePattern: ISchedulePattern = { id: 123 };
          const schedulePattern2: ISchedulePattern = { id: 456 };
          expectedResult = service.addSchedulePatternToCollectionIfMissing([], schedulePattern, schedulePattern2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schedulePattern);
          expect(expectedResult).toContain(schedulePattern2);
        });

        it('should accept null and undefined values', () => {
          const schedulePattern: ISchedulePattern = { id: 123 };
          expectedResult = service.addSchedulePatternToCollectionIfMissing([], null, schedulePattern, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schedulePattern);
        });

        it('should return initial array if no SchedulePattern is added', () => {
          const schedulePatternCollection: ISchedulePattern[] = [{ id: 123 }];
          expectedResult = service.addSchedulePatternToCollectionIfMissing(schedulePatternCollection, undefined, null);
          expect(expectedResult).toEqual(schedulePatternCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
