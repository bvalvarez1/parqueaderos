jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISystemParameters, SystemParameters } from '../system-parameters.model';
import { SystemParametersService } from '../service/system-parameters.service';

import { SystemParametersRoutingResolveService } from './system-parameters-routing-resolve.service';

describe('SystemParameters routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SystemParametersRoutingResolveService;
  let service: SystemParametersService;
  let resultSystemParameters: ISystemParameters | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SystemParametersRoutingResolveService);
    service = TestBed.inject(SystemParametersService);
    resultSystemParameters = undefined;
  });

  describe('resolve', () => {
    it('should return ISystemParameters returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSystemParameters = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSystemParameters).toEqual({ id: 123 });
    });

    it('should return new ISystemParameters if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSystemParameters = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSystemParameters).toEqual(new SystemParameters());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SystemParameters })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSystemParameters = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSystemParameters).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
