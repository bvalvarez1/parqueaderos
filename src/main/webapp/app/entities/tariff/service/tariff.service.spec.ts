import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITariff, Tariff } from '../tariff.model';

import { TariffService } from './tariff.service';

describe('Tariff Service', () => {
  let service: TariffService;
  let httpMock: HttpTestingController;
  let elemDefault: ITariff;
  let expectedResult: ITariff | ITariff[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TariffService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      initialDate: currentDate,
      finalDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          initialDate: currentDate.format(DATE_FORMAT),
          finalDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Tariff', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          initialDate: currentDate.format(DATE_FORMAT),
          finalDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          initialDate: currentDate,
          finalDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Tariff()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tariff', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          initialDate: currentDate.format(DATE_FORMAT),
          finalDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          initialDate: currentDate,
          finalDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tariff', () => {
      const patchObject = Object.assign({}, new Tariff());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          initialDate: currentDate,
          finalDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tariff', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          initialDate: currentDate.format(DATE_FORMAT),
          finalDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          initialDate: currentDate,
          finalDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Tariff', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTariffToCollectionIfMissing', () => {
      it('should add a Tariff to an empty array', () => {
        const tariff: ITariff = { id: 123 };
        expectedResult = service.addTariffToCollectionIfMissing([], tariff);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tariff);
      });

      it('should not add a Tariff to an array that contains it', () => {
        const tariff: ITariff = { id: 123 };
        const tariffCollection: ITariff[] = [
          {
            ...tariff,
          },
          { id: 456 },
        ];
        expectedResult = service.addTariffToCollectionIfMissing(tariffCollection, tariff);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tariff to an array that doesn't contain it", () => {
        const tariff: ITariff = { id: 123 };
        const tariffCollection: ITariff[] = [{ id: 456 }];
        expectedResult = service.addTariffToCollectionIfMissing(tariffCollection, tariff);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tariff);
      });

      it('should add only unique Tariff to an array', () => {
        const tariffArray: ITariff[] = [{ id: 123 }, { id: 456 }, { id: 44334 }];
        const tariffCollection: ITariff[] = [{ id: 123 }];
        expectedResult = service.addTariffToCollectionIfMissing(tariffCollection, ...tariffArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tariff: ITariff = { id: 123 };
        const tariff2: ITariff = { id: 456 };
        expectedResult = service.addTariffToCollectionIfMissing([], tariff, tariff2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tariff);
        expect(expectedResult).toContain(tariff2);
      });

      it('should accept null and undefined values', () => {
        const tariff: ITariff = { id: 123 };
        expectedResult = service.addTariffToCollectionIfMissing([], null, tariff, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tariff);
      });

      it('should return initial array if no Tariff is added', () => {
        const tariffCollection: ITariff[] = [{ id: 123 }];
        expectedResult = service.addTariffToCollectionIfMissing(tariffCollection, undefined, null);
        expect(expectedResult).toEqual(tariffCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
