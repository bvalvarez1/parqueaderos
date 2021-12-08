import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IReceipt, Receipt } from '../receipt.model';

import { ReceiptService } from './receipt.service';

describe('Receipt Service', () => {
  let service: ReceiptService;
  let httpMock: HttpTestingController;
  let elemDefault: IReceipt;
  let expectedResult: IReceipt | IReceipt[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReceiptService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      authorizationNumber: 'AAAAAAA',
      sequential: 'AAAAAAA',
      status: 'AAAAAAA',
      sriaccesskey: 'AAAAAAA',
      sriauthorizationdate: currentDate,
      receiptdate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          sriauthorizationdate: currentDate.format(DATE_FORMAT),
          receiptdate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Receipt', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          sriauthorizationdate: currentDate.format(DATE_FORMAT),
          receiptdate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          sriauthorizationdate: currentDate,
          receiptdate: currentDate,
        },
        returnedFromService
      );

      service.create(new Receipt()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Receipt', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          authorizationNumber: 'BBBBBB',
          sequential: 'BBBBBB',
          status: 'BBBBBB',
          sriaccesskey: 'BBBBBB',
          sriauthorizationdate: currentDate.format(DATE_FORMAT),
          receiptdate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          sriauthorizationdate: currentDate,
          receiptdate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Receipt', () => {
      const patchObject = Object.assign(
        {
          sequential: 'BBBBBB',
          status: 'BBBBBB',
          receiptdate: currentDate.format(DATE_FORMAT),
        },
        new Receipt()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          sriauthorizationdate: currentDate,
          receiptdate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Receipt', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          authorizationNumber: 'BBBBBB',
          sequential: 'BBBBBB',
          status: 'BBBBBB',
          sriaccesskey: 'BBBBBB',
          sriauthorizationdate: currentDate.format(DATE_FORMAT),
          receiptdate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          sriauthorizationdate: currentDate,
          receiptdate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Receipt', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addReceiptToCollectionIfMissing', () => {
      it('should add a Receipt to an empty array', () => {
        const receipt: IReceipt = { id: 123 };
        expectedResult = service.addReceiptToCollectionIfMissing([], receipt);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(receipt);
      });

      it('should not add a Receipt to an array that contains it', () => {
        const receipt: IReceipt = { id: 123 };
        const receiptCollection: IReceipt[] = [
          {
            ...receipt,
          },
          { id: 456 },
        ];
        expectedResult = service.addReceiptToCollectionIfMissing(receiptCollection, receipt);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Receipt to an array that doesn't contain it", () => {
        const receipt: IReceipt = { id: 123 };
        const receiptCollection: IReceipt[] = [{ id: 456 }];
        expectedResult = service.addReceiptToCollectionIfMissing(receiptCollection, receipt);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(receipt);
      });

      it('should add only unique Receipt to an array', () => {
        const receiptArray: IReceipt[] = [{ id: 123 }, { id: 456 }, { id: 76683 }];
        const receiptCollection: IReceipt[] = [{ id: 123 }];
        expectedResult = service.addReceiptToCollectionIfMissing(receiptCollection, ...receiptArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const receipt: IReceipt = { id: 123 };
        const receipt2: IReceipt = { id: 456 };
        expectedResult = service.addReceiptToCollectionIfMissing([], receipt, receipt2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(receipt);
        expect(expectedResult).toContain(receipt2);
      });

      it('should accept null and undefined values', () => {
        const receipt: IReceipt = { id: 123 };
        expectedResult = service.addReceiptToCollectionIfMissing([], null, receipt, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(receipt);
      });

      it('should return initial array if no Receipt is added', () => {
        const receiptCollection: IReceipt[] = [{ id: 123 }];
        expectedResult = service.addReceiptToCollectionIfMissing(receiptCollection, undefined, null);
        expect(expectedResult).toEqual(receiptCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
