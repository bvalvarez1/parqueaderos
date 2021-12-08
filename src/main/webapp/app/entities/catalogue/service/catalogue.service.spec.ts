import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICatalogue, Catalogue } from '../catalogue.model';

import { CatalogueService } from './catalogue.service';

describe('Catalogue Service', () => {
  let service: CatalogueService;
  let httpMock: HttpTestingController;
  let elemDefault: ICatalogue;
  let expectedResult: ICatalogue | ICatalogue[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CatalogueService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      code: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a Catalogue', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Catalogue()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Catalogue', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          code: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should partial update a Catalogue', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          code: 'BBBBBB',
          description: 'BBBBBB',
        },
        new Catalogue()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Catalogue', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          code: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a Catalogue', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCatalogueToCollectionIfMissing', () => {
      it('should add a Catalogue to an empty array', () => {
        const catalogue: ICatalogue = { id: 123 };
        expectedResult = service.addCatalogueToCollectionIfMissing([], catalogue);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(catalogue);
      });

      it('should not add a Catalogue to an array that contains it', () => {
        const catalogue: ICatalogue = { id: 123 };
        const catalogueCollection: ICatalogue[] = [
          {
            ...catalogue,
          },
          { id: 456 },
        ];
        expectedResult = service.addCatalogueToCollectionIfMissing(catalogueCollection, catalogue);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Catalogue to an array that doesn't contain it", () => {
        const catalogue: ICatalogue = { id: 123 };
        const catalogueCollection: ICatalogue[] = [{ id: 456 }];
        expectedResult = service.addCatalogueToCollectionIfMissing(catalogueCollection, catalogue);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalogue);
      });

      it('should add only unique Catalogue to an array', () => {
        const catalogueArray: ICatalogue[] = [{ id: 123 }, { id: 456 }, { id: 32348 }];
        const catalogueCollection: ICatalogue[] = [{ id: 123 }];
        expectedResult = service.addCatalogueToCollectionIfMissing(catalogueCollection, ...catalogueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const catalogue: ICatalogue = { id: 123 };
        const catalogue2: ICatalogue = { id: 456 };
        expectedResult = service.addCatalogueToCollectionIfMissing([], catalogue, catalogue2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalogue);
        expect(expectedResult).toContain(catalogue2);
      });

      it('should accept null and undefined values', () => {
        const catalogue: ICatalogue = { id: 123 };
        expectedResult = service.addCatalogueToCollectionIfMissing([], null, catalogue, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(catalogue);
      });

      it('should return initial array if no Catalogue is added', () => {
        const catalogueCollection: ICatalogue[] = [{ id: 123 }];
        expectedResult = service.addCatalogueToCollectionIfMissing(catalogueCollection, undefined, null);
        expect(expectedResult).toEqual(catalogueCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
