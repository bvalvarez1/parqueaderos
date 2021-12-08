jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITariff, Tariff } from '../tariff.model';
import { TariffService } from '../service/tariff.service';

import { TariffRoutingResolveService } from './tariff-routing-resolve.service';

describe('Tariff routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TariffRoutingResolveService;
  let service: TariffService;
  let resultTariff: ITariff | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TariffRoutingResolveService);
    service = TestBed.inject(TariffService);
    resultTariff = undefined;
  });

  describe('resolve', () => {
    it('should return ITariff returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTariff = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTariff).toEqual({ id: 123 });
    });

    it('should return new ITariff if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTariff = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTariff).toEqual(new Tariff());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Tariff })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTariff = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTariff).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
