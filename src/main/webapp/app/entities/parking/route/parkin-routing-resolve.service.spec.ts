jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInstitution, Institution } from '../parking.model';
import { ParkingService } from '../service/parking.service';

import { InstitutionRoutingResolveService } from './parking-routing-resolve.service';

describe('Institution routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InstitutionRoutingResolveService;
  let service: ParkingService;
  let resultInstitution: IInstitution | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(InstitutionRoutingResolveService);
    service = TestBed.inject(InstitutionService);
    resultInstitution = undefined;
  });

  describe('resolve', () => {
    it('should return IInstitution returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInstitution = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInstitution).toEqual({ id: 123 });
    });

    it('should return new IInstitution if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInstitution = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInstitution).toEqual(new Institution());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Institution })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInstitution = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInstitution).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
