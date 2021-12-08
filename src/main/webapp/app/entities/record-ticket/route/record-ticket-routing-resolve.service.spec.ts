jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRecordTicket, RecordTicket } from '../record-ticket.model';
import { RecordTicketService } from '../service/record-ticket.service';

import { RecordTicketRoutingResolveService } from './record-ticket-routing-resolve.service';

describe('RecordTicket routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RecordTicketRoutingResolveService;
  let service: RecordTicketService;
  let resultRecordTicket: IRecordTicket | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RecordTicketRoutingResolveService);
    service = TestBed.inject(RecordTicketService);
    resultRecordTicket = undefined;
  });

  describe('resolve', () => {
    it('should return IRecordTicket returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRecordTicket = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRecordTicket).toEqual({ id: 123 });
    });

    it('should return new IRecordTicket if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRecordTicket = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRecordTicket).toEqual(new RecordTicket());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RecordTicket })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRecordTicket = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRecordTicket).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
