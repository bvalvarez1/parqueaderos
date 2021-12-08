jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IReceipt, Receipt } from '../receipt.model';
import { ReceiptService } from '../service/receipt.service';

import { ReceiptRoutingResolveService } from './receipt-routing-resolve.service';

describe('Receipt routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ReceiptRoutingResolveService;
  let service: ReceiptService;
  let resultReceipt: IReceipt | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ReceiptRoutingResolveService);
    service = TestBed.inject(ReceiptService);
    resultReceipt = undefined;
  });

  describe('resolve', () => {
    it('should return IReceipt returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReceipt = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReceipt).toEqual({ id: 123 });
    });

    it('should return new IReceipt if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReceipt = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultReceipt).toEqual(new Receipt());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Receipt })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReceipt = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReceipt).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
