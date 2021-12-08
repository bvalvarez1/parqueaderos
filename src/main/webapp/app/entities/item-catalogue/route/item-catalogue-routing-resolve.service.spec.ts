jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IItemCatalogue, ItemCatalogue } from '../item-catalogue.model';
import { ItemCatalogueService } from '../service/item-catalogue.service';

import { ItemCatalogueRoutingResolveService } from './item-catalogue-routing-resolve.service';

describe('ItemCatalogue routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ItemCatalogueRoutingResolveService;
  let service: ItemCatalogueService;
  let resultItemCatalogue: IItemCatalogue | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ItemCatalogueRoutingResolveService);
    service = TestBed.inject(ItemCatalogueService);
    resultItemCatalogue = undefined;
  });

  describe('resolve', () => {
    it('should return IItemCatalogue returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultItemCatalogue = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultItemCatalogue).toEqual({ id: 123 });
    });

    it('should return new IItemCatalogue if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultItemCatalogue = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultItemCatalogue).toEqual(new ItemCatalogue());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ItemCatalogue })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultItemCatalogue = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultItemCatalogue).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
