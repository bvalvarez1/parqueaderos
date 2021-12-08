jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFunctionality, Functionality } from '../functionality.model';
import { FunctionalityService } from '../service/functionality.service';

import { FunctionalityRoutingResolveService } from './functionality-routing-resolve.service';

describe('Functionality routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FunctionalityRoutingResolveService;
  let service: FunctionalityService;
  let resultFunctionality: IFunctionality | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FunctionalityRoutingResolveService);
    service = TestBed.inject(FunctionalityService);
    resultFunctionality = undefined;
  });

  describe('resolve', () => {
    it('should return IFunctionality returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFunctionality = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFunctionality).toEqual({ id: 123 });
    });

    it('should return new IFunctionality if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFunctionality = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFunctionality).toEqual(new Functionality());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Functionality })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFunctionality = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFunctionality).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
