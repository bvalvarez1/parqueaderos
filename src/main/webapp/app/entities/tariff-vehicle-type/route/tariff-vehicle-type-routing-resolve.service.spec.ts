jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITariffVehicleType, TariffVehicleType } from '../tariff-vehicle-type.model';
import { TariffVehicleTypeService } from '../service/tariff-vehicle-type.service';

import { TariffVehicleTypeRoutingResolveService } from './tariff-vehicle-type-routing-resolve.service';

describe('TariffVehicleType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TariffVehicleTypeRoutingResolveService;
  let service: TariffVehicleTypeService;
  let resultTariffVehicleType: ITariffVehicleType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TariffVehicleTypeRoutingResolveService);
    service = TestBed.inject(TariffVehicleTypeService);
    resultTariffVehicleType = undefined;
  });

  describe('resolve', () => {
    it('should return ITariffVehicleType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTariffVehicleType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTariffVehicleType).toEqual({ id: 123 });
    });

    it('should return new ITariffVehicleType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTariffVehicleType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTariffVehicleType).toEqual(new TariffVehicleType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TariffVehicleType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTariffVehicleType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTariffVehicleType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
