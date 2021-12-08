import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISystemParameters, SystemParameters } from '../system-parameters.model';

import { SystemParametersService } from './system-parameters.service';

describe('SystemParameters Service', () => {
  let service: SystemParametersService;
  let httpMock: HttpTestingController;
  let elemDefault: ISystemParameters;
  let expectedResult: ISystemParameters | ISystemParameters[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SystemParametersService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      code: 'AAAAAAA',
      clase: 'AAAAAAA',
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

    it('should create a SystemParameters', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SystemParameters()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SystemParameters', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          code: 'BBBBBB',
          clase: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SystemParameters', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          code: 'BBBBBB',
        },
        new SystemParameters()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SystemParameters', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          code: 'BBBBBB',
          clase: 'BBBBBB',
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

    it('should delete a SystemParameters', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSystemParametersToCollectionIfMissing', () => {
      it('should add a SystemParameters to an empty array', () => {
        const systemParameters: ISystemParameters = { id: 123 };
        expectedResult = service.addSystemParametersToCollectionIfMissing([], systemParameters);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemParameters);
      });

      it('should not add a SystemParameters to an array that contains it', () => {
        const systemParameters: ISystemParameters = { id: 123 };
        const systemParametersCollection: ISystemParameters[] = [
          {
            ...systemParameters,
          },
          { id: 456 },
        ];
        expectedResult = service.addSystemParametersToCollectionIfMissing(systemParametersCollection, systemParameters);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SystemParameters to an array that doesn't contain it", () => {
        const systemParameters: ISystemParameters = { id: 123 };
        const systemParametersCollection: ISystemParameters[] = [{ id: 456 }];
        expectedResult = service.addSystemParametersToCollectionIfMissing(systemParametersCollection, systemParameters);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemParameters);
      });

      it('should add only unique SystemParameters to an array', () => {
        const systemParametersArray: ISystemParameters[] = [{ id: 123 }, { id: 456 }, { id: 9972 }];
        const systemParametersCollection: ISystemParameters[] = [{ id: 123 }];
        expectedResult = service.addSystemParametersToCollectionIfMissing(systemParametersCollection, ...systemParametersArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const systemParameters: ISystemParameters = { id: 123 };
        const systemParameters2: ISystemParameters = { id: 456 };
        expectedResult = service.addSystemParametersToCollectionIfMissing([], systemParameters, systemParameters2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemParameters);
        expect(expectedResult).toContain(systemParameters2);
      });

      it('should accept null and undefined values', () => {
        const systemParameters: ISystemParameters = { id: 123 };
        expectedResult = service.addSystemParametersToCollectionIfMissing([], null, systemParameters, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemParameters);
      });

      it('should return initial array if no SystemParameters is added', () => {
        const systemParametersCollection: ISystemParameters[] = [{ id: 123 }];
        expectedResult = service.addSystemParametersToCollectionIfMissing(systemParametersCollection, undefined, null);
        expect(expectedResult).toEqual(systemParametersCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
