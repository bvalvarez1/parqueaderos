import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFunctionality, Functionality } from '../functionality.model';

import { FunctionalityService } from './functionality.service';

describe('Functionality Service', () => {
  let service: FunctionalityService;
  let httpMock: HttpTestingController;
  let elemDefault: IFunctionality;
  let expectedResult: IFunctionality | IFunctionality[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FunctionalityService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
      icon: 'AAAAAAA',
      url: 'AAAAAAA',
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

    it('should create a Functionality', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Functionality()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Functionality', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          icon: 'BBBBBB',
          url: 'BBBBBB',
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

    it('should partial update a Functionality', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
          url: 'BBBBBB',
        },
        new Functionality()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Functionality', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          icon: 'BBBBBB',
          url: 'BBBBBB',
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

    it('should delete a Functionality', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFunctionalityToCollectionIfMissing', () => {
      it('should add a Functionality to an empty array', () => {
        const functionality: IFunctionality = { id: 123 };
        expectedResult = service.addFunctionalityToCollectionIfMissing([], functionality);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(functionality);
      });

      it('should not add a Functionality to an array that contains it', () => {
        const functionality: IFunctionality = { id: 123 };
        const functionalityCollection: IFunctionality[] = [
          {
            ...functionality,
          },
          { id: 456 },
        ];
        expectedResult = service.addFunctionalityToCollectionIfMissing(functionalityCollection, functionality);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Functionality to an array that doesn't contain it", () => {
        const functionality: IFunctionality = { id: 123 };
        const functionalityCollection: IFunctionality[] = [{ id: 456 }];
        expectedResult = service.addFunctionalityToCollectionIfMissing(functionalityCollection, functionality);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(functionality);
      });

      it('should add only unique Functionality to an array', () => {
        const functionalityArray: IFunctionality[] = [{ id: 123 }, { id: 456 }, { id: 70030 }];
        const functionalityCollection: IFunctionality[] = [{ id: 123 }];
        expectedResult = service.addFunctionalityToCollectionIfMissing(functionalityCollection, ...functionalityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const functionality: IFunctionality = { id: 123 };
        const functionality2: IFunctionality = { id: 456 };
        expectedResult = service.addFunctionalityToCollectionIfMissing([], functionality, functionality2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(functionality);
        expect(expectedResult).toContain(functionality2);
      });

      it('should accept null and undefined values', () => {
        const functionality: IFunctionality = { id: 123 };
        expectedResult = service.addFunctionalityToCollectionIfMissing([], null, functionality, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(functionality);
      });

      it('should return initial array if no Functionality is added', () => {
        const functionalityCollection: IFunctionality[] = [{ id: 123 }];
        expectedResult = service.addFunctionalityToCollectionIfMissing(functionalityCollection, undefined, null);
        expect(expectedResult).toEqual(functionalityCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
