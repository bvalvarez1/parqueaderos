import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISystemParameterInstitution, SystemParameterInstitution } from '../system-parameter-institution.model';

import { SystemParameterInstitutionService } from './system-parameter-institution.service';

describe('SystemParameterInstitution Service', () => {
  let service: SystemParameterInstitutionService;
  let httpMock: HttpTestingController;
  let elemDefault: ISystemParameterInstitution;
  let expectedResult: ISystemParameterInstitution | ISystemParameterInstitution[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SystemParameterInstitutionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      value: 'AAAAAAA',
      active: false,
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

    it('should create a SystemParameterInstitution', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SystemParameterInstitution()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SystemParameterInstitution', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          value: 'BBBBBB',
          active: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SystemParameterInstitution', () => {
      const patchObject = Object.assign(
        {
          value: 'BBBBBB',
        },
        new SystemParameterInstitution()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SystemParameterInstitution', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          value: 'BBBBBB',
          active: true,
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

    it('should delete a SystemParameterInstitution', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSystemParameterInstitutionToCollectionIfMissing', () => {
      it('should add a SystemParameterInstitution to an empty array', () => {
        const systemParameterInstitution: ISystemParameterInstitution = { id: 123 };
        expectedResult = service.addSystemParameterInstitutionToCollectionIfMissing([], systemParameterInstitution);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemParameterInstitution);
      });

      it('should not add a SystemParameterInstitution to an array that contains it', () => {
        const systemParameterInstitution: ISystemParameterInstitution = { id: 123 };
        const systemParameterInstitutionCollection: ISystemParameterInstitution[] = [
          {
            ...systemParameterInstitution,
          },
          { id: 456 },
        ];
        expectedResult = service.addSystemParameterInstitutionToCollectionIfMissing(
          systemParameterInstitutionCollection,
          systemParameterInstitution
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SystemParameterInstitution to an array that doesn't contain it", () => {
        const systemParameterInstitution: ISystemParameterInstitution = { id: 123 };
        const systemParameterInstitutionCollection: ISystemParameterInstitution[] = [{ id: 456 }];
        expectedResult = service.addSystemParameterInstitutionToCollectionIfMissing(
          systemParameterInstitutionCollection,
          systemParameterInstitution
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemParameterInstitution);
      });

      it('should add only unique SystemParameterInstitution to an array', () => {
        const systemParameterInstitutionArray: ISystemParameterInstitution[] = [{ id: 123 }, { id: 456 }, { id: 95100 }];
        const systemParameterInstitutionCollection: ISystemParameterInstitution[] = [{ id: 123 }];
        expectedResult = service.addSystemParameterInstitutionToCollectionIfMissing(
          systemParameterInstitutionCollection,
          ...systemParameterInstitutionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const systemParameterInstitution: ISystemParameterInstitution = { id: 123 };
        const systemParameterInstitution2: ISystemParameterInstitution = { id: 456 };
        expectedResult = service.addSystemParameterInstitutionToCollectionIfMissing(
          [],
          systemParameterInstitution,
          systemParameterInstitution2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemParameterInstitution);
        expect(expectedResult).toContain(systemParameterInstitution2);
      });

      it('should accept null and undefined values', () => {
        const systemParameterInstitution: ISystemParameterInstitution = { id: 123 };
        expectedResult = service.addSystemParameterInstitutionToCollectionIfMissing([], null, systemParameterInstitution, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemParameterInstitution);
      });

      it('should return initial array if no SystemParameterInstitution is added', () => {
        const systemParameterInstitutionCollection: ISystemParameterInstitution[] = [{ id: 123 }];
        expectedResult = service.addSystemParameterInstitutionToCollectionIfMissing(systemParameterInstitutionCollection, undefined, null);
        expect(expectedResult).toEqual(systemParameterInstitutionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
