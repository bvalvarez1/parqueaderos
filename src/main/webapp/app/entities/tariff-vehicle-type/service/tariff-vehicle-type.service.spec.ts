import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITariffVehicleType, TariffVehicleType } from '../tariff-vehicle-type.model';

import { TariffVehicleTypeService } from './tariff-vehicle-type.service';

describe('TariffVehicleType Service', () => {
  let service: TariffVehicleTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ITariffVehicleType;
  let expectedResult: ITariffVehicleType | ITariffVehicleType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TariffVehicleTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      minValue: 0,
      maxValue: 0,
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

    it('should create a TariffVehicleType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TariffVehicleType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TariffVehicleType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          minValue: 1,
          maxValue: 1,
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

    it('should partial update a TariffVehicleType', () => {
      const patchObject = Object.assign(
        {
          minValue: 1,
          maxValue: 1,
          value: 1,
        },
        new TariffVehicleType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TariffVehicleType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          minValue: 1,
          maxValue: 1,
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

    it('should delete a TariffVehicleType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTariffVehicleTypeToCollectionIfMissing', () => {
      it('should add a TariffVehicleType to an empty array', () => {
        const tariffVehicleType: ITariffVehicleType = { id: 123 };
        expectedResult = service.addTariffVehicleTypeToCollectionIfMissing([], tariffVehicleType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tariffVehicleType);
      });

      it('should not add a TariffVehicleType to an array that contains it', () => {
        const tariffVehicleType: ITariffVehicleType = { id: 123 };
        const tariffVehicleTypeCollection: ITariffVehicleType[] = [
          {
            ...tariffVehicleType,
          },
          { id: 456 },
        ];
        expectedResult = service.addTariffVehicleTypeToCollectionIfMissing(tariffVehicleTypeCollection, tariffVehicleType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TariffVehicleType to an array that doesn't contain it", () => {
        const tariffVehicleType: ITariffVehicleType = { id: 123 };
        const tariffVehicleTypeCollection: ITariffVehicleType[] = [{ id: 456 }];
        expectedResult = service.addTariffVehicleTypeToCollectionIfMissing(tariffVehicleTypeCollection, tariffVehicleType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tariffVehicleType);
      });

      it('should add only unique TariffVehicleType to an array', () => {
        const tariffVehicleTypeArray: ITariffVehicleType[] = [{ id: 123 }, { id: 456 }, { id: 11626 }];
        const tariffVehicleTypeCollection: ITariffVehicleType[] = [{ id: 123 }];
        expectedResult = service.addTariffVehicleTypeToCollectionIfMissing(tariffVehicleTypeCollection, ...tariffVehicleTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tariffVehicleType: ITariffVehicleType = { id: 123 };
        const tariffVehicleType2: ITariffVehicleType = { id: 456 };
        expectedResult = service.addTariffVehicleTypeToCollectionIfMissing([], tariffVehicleType, tariffVehicleType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tariffVehicleType);
        expect(expectedResult).toContain(tariffVehicleType2);
      });

      it('should accept null and undefined values', () => {
        const tariffVehicleType: ITariffVehicleType = { id: 123 };
        expectedResult = service.addTariffVehicleTypeToCollectionIfMissing([], null, tariffVehicleType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tariffVehicleType);
      });

      it('should return initial array if no TariffVehicleType is added', () => {
        const tariffVehicleTypeCollection: ITariffVehicleType[] = [{ id: 123 }];
        expectedResult = service.addTariffVehicleTypeToCollectionIfMissing(tariffVehicleTypeCollection, undefined, null);
        expect(expectedResult).toEqual(tariffVehicleTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
