import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAuthorityFunctionality, AuthorityFunctionality } from '../authority-functionality.model';

import { AuthorityFunctionalityService } from './authority-functionality.service';

describe('AuthorityFunctionality Service', () => {
  let service: AuthorityFunctionalityService;
  let httpMock: HttpTestingController;
  let elemDefault: IAuthorityFunctionality;
  let expectedResult: IAuthorityFunctionality | IAuthorityFunctionality[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AuthorityFunctionalityService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      authority: 'AAAAAAA',
      priority: 0,
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

    it('should create a AuthorityFunctionality', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AuthorityFunctionality()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AuthorityFunctionality', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          authority: 'BBBBBB',
          priority: 1,
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

    it('should partial update a AuthorityFunctionality', () => {
      const patchObject = Object.assign(
        {
          authority: 'BBBBBB',
          active: true,
        },
        new AuthorityFunctionality()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AuthorityFunctionality', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          authority: 'BBBBBB',
          priority: 1,
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

    it('should delete a AuthorityFunctionality', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAuthorityFunctionalityToCollectionIfMissing', () => {
      it('should add a AuthorityFunctionality to an empty array', () => {
        const authorityFunctionality: IAuthorityFunctionality = { id: 123 };
        expectedResult = service.addAuthorityFunctionalityToCollectionIfMissing([], authorityFunctionality);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(authorityFunctionality);
      });

      it('should not add a AuthorityFunctionality to an array that contains it', () => {
        const authorityFunctionality: IAuthorityFunctionality = { id: 123 };
        const authorityFunctionalityCollection: IAuthorityFunctionality[] = [
          {
            ...authorityFunctionality,
          },
          { id: 456 },
        ];
        expectedResult = service.addAuthorityFunctionalityToCollectionIfMissing(authorityFunctionalityCollection, authorityFunctionality);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AuthorityFunctionality to an array that doesn't contain it", () => {
        const authorityFunctionality: IAuthorityFunctionality = { id: 123 };
        const authorityFunctionalityCollection: IAuthorityFunctionality[] = [{ id: 456 }];
        expectedResult = service.addAuthorityFunctionalityToCollectionIfMissing(authorityFunctionalityCollection, authorityFunctionality);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(authorityFunctionality);
      });

      it('should add only unique AuthorityFunctionality to an array', () => {
        const authorityFunctionalityArray: IAuthorityFunctionality[] = [{ id: 123 }, { id: 456 }, { id: 92361 }];
        const authorityFunctionalityCollection: IAuthorityFunctionality[] = [{ id: 123 }];
        expectedResult = service.addAuthorityFunctionalityToCollectionIfMissing(
          authorityFunctionalityCollection,
          ...authorityFunctionalityArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const authorityFunctionality: IAuthorityFunctionality = { id: 123 };
        const authorityFunctionality2: IAuthorityFunctionality = { id: 456 };
        expectedResult = service.addAuthorityFunctionalityToCollectionIfMissing([], authorityFunctionality, authorityFunctionality2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(authorityFunctionality);
        expect(expectedResult).toContain(authorityFunctionality2);
      });

      it('should accept null and undefined values', () => {
        const authorityFunctionality: IAuthorityFunctionality = { id: 123 };
        expectedResult = service.addAuthorityFunctionalityToCollectionIfMissing([], null, authorityFunctionality, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(authorityFunctionality);
      });

      it('should return initial array if no AuthorityFunctionality is added', () => {
        const authorityFunctionalityCollection: IAuthorityFunctionality[] = [{ id: 123 }];
        expectedResult = service.addAuthorityFunctionalityToCollectionIfMissing(authorityFunctionalityCollection, undefined, null);
        expect(expectedResult).toEqual(authorityFunctionalityCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
