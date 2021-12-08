import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRecordTicket, RecordTicket } from '../record-ticket.model';

import { RecordTicketService } from './record-ticket.service';

describe('RecordTicket Service', () => {
  let service: RecordTicketService;
  let httpMock: HttpTestingController;
  let elemDefault: IRecordTicket;
  let expectedResult: IRecordTicket | IRecordTicket[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RecordTicketService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      initDate: currentDate,
      endDate: currentDate,
      plate: 'AAAAAAA',
      parkingTime: currentDate,
      taxableTotal: 0,
      taxes: 0,
      total: 0,
      observation: 'AAAAAAA',
      sequential: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          initDate: currentDate.format(DATE_TIME_FORMAT),
          endDate: currentDate.format(DATE_TIME_FORMAT),
          parkingTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a RecordTicket', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          initDate: currentDate.format(DATE_TIME_FORMAT),
          endDate: currentDate.format(DATE_TIME_FORMAT),
          parkingTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          initDate: currentDate,
          endDate: currentDate,
          parkingTime: currentDate,
        },
        returnedFromService
      );

      service.create(new RecordTicket()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RecordTicket', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          initDate: currentDate.format(DATE_TIME_FORMAT),
          endDate: currentDate.format(DATE_TIME_FORMAT),
          plate: 'BBBBBB',
          parkingTime: currentDate.format(DATE_TIME_FORMAT),
          taxableTotal: 1,
          taxes: 1,
          total: 1,
          observation: 'BBBBBB',
          sequential: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          initDate: currentDate,
          endDate: currentDate,
          parkingTime: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RecordTicket', () => {
      const patchObject = Object.assign(
        {
          initDate: currentDate.format(DATE_TIME_FORMAT),
          plate: 'BBBBBB',
          parkingTime: currentDate.format(DATE_TIME_FORMAT),
          observation: 'BBBBBB',
        },
        new RecordTicket()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          initDate: currentDate,
          endDate: currentDate,
          parkingTime: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RecordTicket', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          initDate: currentDate.format(DATE_TIME_FORMAT),
          endDate: currentDate.format(DATE_TIME_FORMAT),
          plate: 'BBBBBB',
          parkingTime: currentDate.format(DATE_TIME_FORMAT),
          taxableTotal: 1,
          taxes: 1,
          total: 1,
          observation: 'BBBBBB',
          sequential: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          initDate: currentDate,
          endDate: currentDate,
          parkingTime: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a RecordTicket', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRecordTicketToCollectionIfMissing', () => {
      it('should add a RecordTicket to an empty array', () => {
        const recordTicket: IRecordTicket = { id: 123 };
        expectedResult = service.addRecordTicketToCollectionIfMissing([], recordTicket);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recordTicket);
      });

      it('should not add a RecordTicket to an array that contains it', () => {
        const recordTicket: IRecordTicket = { id: 123 };
        const recordTicketCollection: IRecordTicket[] = [
          {
            ...recordTicket,
          },
          { id: 456 },
        ];
        expectedResult = service.addRecordTicketToCollectionIfMissing(recordTicketCollection, recordTicket);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RecordTicket to an array that doesn't contain it", () => {
        const recordTicket: IRecordTicket = { id: 123 };
        const recordTicketCollection: IRecordTicket[] = [{ id: 456 }];
        expectedResult = service.addRecordTicketToCollectionIfMissing(recordTicketCollection, recordTicket);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recordTicket);
      });

      it('should add only unique RecordTicket to an array', () => {
        const recordTicketArray: IRecordTicket[] = [{ id: 123 }, { id: 456 }, { id: 69181 }];
        const recordTicketCollection: IRecordTicket[] = [{ id: 123 }];
        expectedResult = service.addRecordTicketToCollectionIfMissing(recordTicketCollection, ...recordTicketArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const recordTicket: IRecordTicket = { id: 123 };
        const recordTicket2: IRecordTicket = { id: 456 };
        expectedResult = service.addRecordTicketToCollectionIfMissing([], recordTicket, recordTicket2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recordTicket);
        expect(expectedResult).toContain(recordTicket2);
      });

      it('should accept null and undefined values', () => {
        const recordTicket: IRecordTicket = { id: 123 };
        expectedResult = service.addRecordTicketToCollectionIfMissing([], null, recordTicket, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recordTicket);
      });

      it('should return initial array if no RecordTicket is added', () => {
        const recordTicketCollection: IRecordTicket[] = [{ id: 123 }];
        expectedResult = service.addRecordTicketToCollectionIfMissing(recordTicketCollection, undefined, null);
        expect(expectedResult).toEqual(recordTicketCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
