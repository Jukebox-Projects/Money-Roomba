import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISystemSetting, SystemSetting } from '../system-setting.model';

import { SystemSettingService } from './system-setting.service';

describe('Service Tests', () => {
  describe('SystemSetting Service', () => {
    let service: SystemSettingService;
    let httpMock: HttpTestingController;
    let elemDefault: ISystemSetting;
    let expectedResult: ISystemSetting | ISystemSetting[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SystemSettingService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        key: 'AAAAAAA',
        value: 0,
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

      it('should create a SystemSetting', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new SystemSetting()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SystemSetting', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
            value: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SystemSetting', () => {
        const patchObject = Object.assign(
          {
            key: 'BBBBBB',
          },
          new SystemSetting()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SystemSetting', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
            value: 1,
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

      it('should delete a SystemSetting', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSystemSettingToCollectionIfMissing', () => {
        it('should add a SystemSetting to an empty array', () => {
          const systemSetting: ISystemSetting = { id: 123 };
          expectedResult = service.addSystemSettingToCollectionIfMissing([], systemSetting);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(systemSetting);
        });

        it('should not add a SystemSetting to an array that contains it', () => {
          const systemSetting: ISystemSetting = { id: 123 };
          const systemSettingCollection: ISystemSetting[] = [
            {
              ...systemSetting,
            },
            { id: 456 },
          ];
          expectedResult = service.addSystemSettingToCollectionIfMissing(systemSettingCollection, systemSetting);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SystemSetting to an array that doesn't contain it", () => {
          const systemSetting: ISystemSetting = { id: 123 };
          const systemSettingCollection: ISystemSetting[] = [{ id: 456 }];
          expectedResult = service.addSystemSettingToCollectionIfMissing(systemSettingCollection, systemSetting);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(systemSetting);
        });

        it('should add only unique SystemSetting to an array', () => {
          const systemSettingArray: ISystemSetting[] = [{ id: 123 }, { id: 456 }, { id: 40891 }];
          const systemSettingCollection: ISystemSetting[] = [{ id: 123 }];
          expectedResult = service.addSystemSettingToCollectionIfMissing(systemSettingCollection, ...systemSettingArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const systemSetting: ISystemSetting = { id: 123 };
          const systemSetting2: ISystemSetting = { id: 456 };
          expectedResult = service.addSystemSettingToCollectionIfMissing([], systemSetting, systemSetting2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(systemSetting);
          expect(expectedResult).toContain(systemSetting2);
        });

        it('should accept null and undefined values', () => {
          const systemSetting: ISystemSetting = { id: 123 };
          expectedResult = service.addSystemSettingToCollectionIfMissing([], null, systemSetting, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(systemSetting);
        });

        it('should return initial array if no SystemSetting is added', () => {
          const systemSettingCollection: ISystemSetting[] = [{ id: 123 }];
          expectedResult = service.addSystemSettingToCollectionIfMissing(systemSettingCollection, undefined, null);
          expect(expectedResult).toEqual(systemSettingCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
