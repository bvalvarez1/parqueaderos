import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IItemCatalogue, ItemCatalogue } from '../item-catalogue.model';

import { ItemCatalogueService } from './item-catalogue.service';

describe('ItemCatalogue Service', () => {
  let service: ItemCatalogueService;
  let httpMock: HttpTestingController;
  let elemDefault: IItemCatalogue;
  let expectedResult: IItemCatalogue | IItemCatalogue[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ItemCatalogueService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      code: 'AAAAAAA',
      description: 'AAAAAAA',
      catalogCode: 'AAAAAAA',
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

    it('should create a ItemCatalogue', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ItemCatalogue()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ItemCatalogue', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          code: 'BBBBBB',
          description: 'BBBBBB',
          catalogCode: 'BBBBBB',
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

    it('should partial update a ItemCatalogue', () => {
      const patchObject = Object.assign({}, new ItemCatalogue());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ItemCatalogue', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          code: 'BBBBBB',
          description: 'BBBBBB',
          catalogCode: 'BBBBBB',
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

    it('should delete a ItemCatalogue', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addItemCatalogueToCollectionIfMissing', () => {
      it('should add a ItemCatalogue to an empty array', () => {
        const itemCatalogue: IItemCatalogue = { id: 123 };
        expectedResult = service.addItemCatalogueToCollectionIfMissing([], itemCatalogue);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemCatalogue);
      });

      it('should not add a ItemCatalogue to an array that contains it', () => {
        const itemCatalogue: IItemCatalogue = { id: 123 };
        const itemCatalogueCollection: IItemCatalogue[] = [
          {
            ...itemCatalogue,
          },
          { id: 456 },
        ];
        expectedResult = service.addItemCatalogueToCollectionIfMissing(itemCatalogueCollection, itemCatalogue);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ItemCatalogue to an array that doesn't contain it", () => {
        const itemCatalogue: IItemCatalogue = { id: 123 };
        const itemCatalogueCollection: IItemCatalogue[] = [{ id: 456 }];
        expectedResult = service.addItemCatalogueToCollectionIfMissing(itemCatalogueCollection, itemCatalogue);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemCatalogue);
      });

      it('should add only unique ItemCatalogue to an array', () => {
        const itemCatalogueArray: IItemCatalogue[] = [{ id: 123 }, { id: 456 }, { id: 16363 }];
        const itemCatalogueCollection: IItemCatalogue[] = [{ id: 123 }];
        expectedResult = service.addItemCatalogueToCollectionIfMissing(itemCatalogueCollection, ...itemCatalogueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const itemCatalogue: IItemCatalogue = { id: 123 };
        const itemCatalogue2: IItemCatalogue = { id: 456 };
        expectedResult = service.addItemCatalogueToCollectionIfMissing([], itemCatalogue, itemCatalogue2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemCatalogue);
        expect(expectedResult).toContain(itemCatalogue2);
      });

      it('should accept null and undefined values', () => {
        const itemCatalogue: IItemCatalogue = { id: 123 };
        expectedResult = service.addItemCatalogueToCollectionIfMissing([], null, itemCatalogue, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemCatalogue);
      });

      it('should return initial array if no ItemCatalogue is added', () => {
        const itemCatalogueCollection: IItemCatalogue[] = [{ id: 123 }];
        expectedResult = service.addItemCatalogueToCollectionIfMissing(itemCatalogueCollection, undefined, null);
        expect(expectedResult).toEqual(itemCatalogueCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
