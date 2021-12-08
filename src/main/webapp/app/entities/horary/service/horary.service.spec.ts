import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHorary, Horary } from '../horary.model';

import { HoraryService } from './horary.service';

describe('Horary Service', () => {
  let service: HoraryService;
  let httpMock: HttpTestingController;
  let elemDefault: IHorary;
  let expectedResult: IHorary | IHorary[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HoraryService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      startTime: currentDate,
      finalHour: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          startTime: currentDate.format(DATE_TIME_FORMAT),
          finalHour: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Horary', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          startTime: currentDate.format(DATE_TIME_FORMAT),
          finalHour: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startTime: currentDate,
          finalHour: currentDate,
        },
        returnedFromService
      );

      service.create(new Horary()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Horary', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          startTime: currentDate.format(DATE_TIME_FORMAT),
          finalHour: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startTime: currentDate,
          finalHour: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Horary', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new Horary()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          startTime: currentDate,
          finalHour: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Horary', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          startTime: currentDate.format(DATE_TIME_FORMAT),
          finalHour: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startTime: currentDate,
          finalHour: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Horary', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHoraryToCollectionIfMissing', () => {
      it('should add a Horary to an empty array', () => {
        const horary: IHorary = { id: 123 };
        expectedResult = service.addHoraryToCollectionIfMissing([], horary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(horary);
      });

      it('should not add a Horary to an array that contains it', () => {
        const horary: IHorary = { id: 123 };
        const horaryCollection: IHorary[] = [
          {
            ...horary,
          },
          { id: 456 },
        ];
        expectedResult = service.addHoraryToCollectionIfMissing(horaryCollection, horary);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Horary to an array that doesn't contain it", () => {
        const horary: IHorary = { id: 123 };
        const horaryCollection: IHorary[] = [{ id: 456 }];
        expectedResult = service.addHoraryToCollectionIfMissing(horaryCollection, horary);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(horary);
      });

      it('should add only unique Horary to an array', () => {
        const horaryArray: IHorary[] = [{ id: 123 }, { id: 456 }, { id: 18652 }];
        const horaryCollection: IHorary[] = [{ id: 123 }];
        expectedResult = service.addHoraryToCollectionIfMissing(horaryCollection, ...horaryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const horary: IHorary = { id: 123 };
        const horary2: IHorary = { id: 456 };
        expectedResult = service.addHoraryToCollectionIfMissing([], horary, horary2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(horary);
        expect(expectedResult).toContain(horary2);
      });

      it('should accept null and undefined values', () => {
        const horary: IHorary = { id: 123 };
        expectedResult = service.addHoraryToCollectionIfMissing([], null, horary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(horary);
      });

      it('should return initial array if no Horary is added', () => {
        const horaryCollection: IHorary[] = [{ id: 123 }];
        expectedResult = service.addHoraryToCollectionIfMissing(horaryCollection, undefined, null);
        expect(expectedResult).toEqual(horaryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
