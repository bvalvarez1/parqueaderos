jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IHorary, Horary } from '../horary.model';
import { HoraryService } from '../service/horary.service';

import { HoraryRoutingResolveService } from './horary-routing-resolve.service';

describe('Horary routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: HoraryRoutingResolveService;
  let service: HoraryService;
  let resultHorary: IHorary | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(HoraryRoutingResolveService);
    service = TestBed.inject(HoraryService);
    resultHorary = undefined;
  });

  describe('resolve', () => {
    it('should return IHorary returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHorary = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultHorary).toEqual({ id: 123 });
    });

    it('should return new IHorary if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHorary = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultHorary).toEqual(new Horary());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Horary })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHorary = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultHorary).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
