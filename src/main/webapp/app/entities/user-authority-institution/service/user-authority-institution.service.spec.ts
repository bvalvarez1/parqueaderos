import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserAuthorityInstitution, UserAuthorityInstitution } from '../user-authority-institution.model';

import { UserAuthorityInstitutionService } from './user-authority-institution.service';

describe('UserAuthorityInstitution Service', () => {
  let service: UserAuthorityInstitutionService;
  let httpMock: HttpTestingController;
  let elemDefault: IUserAuthorityInstitution;
  let expectedResult: IUserAuthorityInstitution | IUserAuthorityInstitution[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserAuthorityInstitutionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a UserAuthorityInstitution', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new UserAuthorityInstitution()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserAuthorityInstitution', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should partial update a UserAuthorityInstitution', () => {
      const patchObject = Object.assign(
        {
          active: true,
        },
        new UserAuthorityInstitution()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserAuthorityInstitution', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a UserAuthorityInstitution', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUserAuthorityInstitutionToCollectionIfMissing', () => {
      it('should add a UserAuthorityInstitution to an empty array', () => {
        const userAuthorityInstitution: IUserAuthorityInstitution = { id: 123 };
        expectedResult = service.addUserAuthorityInstitutionToCollectionIfMissing([], userAuthorityInstitution);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAuthorityInstitution);
      });

      it('should not add a UserAuthorityInstitution to an array that contains it', () => {
        const userAuthorityInstitution: IUserAuthorityInstitution = { id: 123 };
        const userAuthorityInstitutionCollection: IUserAuthorityInstitution[] = [
          {
            ...userAuthorityInstitution,
          },
          { id: 456 },
        ];
        expectedResult = service.addUserAuthorityInstitutionToCollectionIfMissing(
          userAuthorityInstitutionCollection,
          userAuthorityInstitution
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserAuthorityInstitution to an array that doesn't contain it", () => {
        const userAuthorityInstitution: IUserAuthorityInstitution = { id: 123 };
        const userAuthorityInstitutionCollection: IUserAuthorityInstitution[] = [{ id: 456 }];
        expectedResult = service.addUserAuthorityInstitutionToCollectionIfMissing(
          userAuthorityInstitutionCollection,
          userAuthorityInstitution
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAuthorityInstitution);
      });

      it('should add only unique UserAuthorityInstitution to an array', () => {
        const userAuthorityInstitutionArray: IUserAuthorityInstitution[] = [{ id: 123 }, { id: 456 }, { id: 14949 }];
        const userAuthorityInstitutionCollection: IUserAuthorityInstitution[] = [{ id: 123 }];
        expectedResult = service.addUserAuthorityInstitutionToCollectionIfMissing(
          userAuthorityInstitutionCollection,
          ...userAuthorityInstitutionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userAuthorityInstitution: IUserAuthorityInstitution = { id: 123 };
        const userAuthorityInstitution2: IUserAuthorityInstitution = { id: 456 };
        expectedResult = service.addUserAuthorityInstitutionToCollectionIfMissing([], userAuthorityInstitution, userAuthorityInstitution2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAuthorityInstitution);
        expect(expectedResult).toContain(userAuthorityInstitution2);
      });

      it('should accept null and undefined values', () => {
        const userAuthorityInstitution: IUserAuthorityInstitution = { id: 123 };
        expectedResult = service.addUserAuthorityInstitutionToCollectionIfMissing([], null, userAuthorityInstitution, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAuthorityInstitution);
      });

      it('should return initial array if no UserAuthorityInstitution is added', () => {
        const userAuthorityInstitutionCollection: IUserAuthorityInstitution[] = [{ id: 123 }];
        expectedResult = service.addUserAuthorityInstitutionToCollectionIfMissing(userAuthorityInstitutionCollection, undefined, null);
        expect(expectedResult).toEqual(userAuthorityInstitutionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
