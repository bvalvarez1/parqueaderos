import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserAuthority, UserAuthority } from '../user-authority.model';

import { UserAuthorityService } from './user-authority.service';

describe('UserAuthority Service', () => {
  let service: UserAuthorityService;
  let httpMock: HttpTestingController;
  let elemDefault: IUserAuthority;
  let expectedResult: IUserAuthority | IUserAuthority[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserAuthorityService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      authority: 'AAAAAAA',
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

    it('should create a UserAuthority', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new UserAuthority()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserAuthority', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          authority: 'BBBBBB',
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

    it('should partial update a UserAuthority', () => {
      const patchObject = Object.assign(
        {
          authority: 'BBBBBB',
          active: true,
        },
        new UserAuthority()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserAuthority', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          authority: 'BBBBBB',
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

    it('should delete a UserAuthority', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUserAuthorityToCollectionIfMissing', () => {
      it('should add a UserAuthority to an empty array', () => {
        const userAuthority: IUserAuthority = { id: 123 };
        expectedResult = service.addUserAuthorityToCollectionIfMissing([], userAuthority);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAuthority);
      });

      it('should not add a UserAuthority to an array that contains it', () => {
        const userAuthority: IUserAuthority = { id: 123 };
        const userAuthorityCollection: IUserAuthority[] = [
          {
            ...userAuthority,
          },
          { id: 456 },
        ];
        expectedResult = service.addUserAuthorityToCollectionIfMissing(userAuthorityCollection, userAuthority);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserAuthority to an array that doesn't contain it", () => {
        const userAuthority: IUserAuthority = { id: 123 };
        const userAuthorityCollection: IUserAuthority[] = [{ id: 456 }];
        expectedResult = service.addUserAuthorityToCollectionIfMissing(userAuthorityCollection, userAuthority);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAuthority);
      });

      it('should add only unique UserAuthority to an array', () => {
        const userAuthorityArray: IUserAuthority[] = [{ id: 123 }, { id: 456 }, { id: 43439 }];
        const userAuthorityCollection: IUserAuthority[] = [{ id: 123 }];
        expectedResult = service.addUserAuthorityToCollectionIfMissing(userAuthorityCollection, ...userAuthorityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userAuthority: IUserAuthority = { id: 123 };
        const userAuthority2: IUserAuthority = { id: 456 };
        expectedResult = service.addUserAuthorityToCollectionIfMissing([], userAuthority, userAuthority2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAuthority);
        expect(expectedResult).toContain(userAuthority2);
      });

      it('should accept null and undefined values', () => {
        const userAuthority: IUserAuthority = { id: 123 };
        expectedResult = service.addUserAuthorityToCollectionIfMissing([], null, userAuthority, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAuthority);
      });

      it('should return initial array if no UserAuthority is added', () => {
        const userAuthorityCollection: IUserAuthority[] = [{ id: 123 }];
        expectedResult = service.addUserAuthorityToCollectionIfMissing(userAuthorityCollection, undefined, null);
        expect(expectedResult).toEqual(userAuthorityCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
