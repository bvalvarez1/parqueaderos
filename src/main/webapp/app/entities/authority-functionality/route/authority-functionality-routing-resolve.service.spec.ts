jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAuthorityFunctionality, AuthorityFunctionality } from '../authority-functionality.model';
import { AuthorityFunctionalityService } from '../service/authority-functionality.service';

import { AuthorityFunctionalityRoutingResolveService } from './authority-functionality-routing-resolve.service';

describe('AuthorityFunctionality routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AuthorityFunctionalityRoutingResolveService;
  let service: AuthorityFunctionalityService;
  let resultAuthorityFunctionality: IAuthorityFunctionality | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AuthorityFunctionalityRoutingResolveService);
    service = TestBed.inject(AuthorityFunctionalityService);
    resultAuthorityFunctionality = undefined;
  });

  describe('resolve', () => {
    it('should return IAuthorityFunctionality returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAuthorityFunctionality = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAuthorityFunctionality).toEqual({ id: 123 });
    });

    it('should return new IAuthorityFunctionality if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAuthorityFunctionality = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAuthorityFunctionality).toEqual(new AuthorityFunctionality());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AuthorityFunctionality })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAuthorityFunctionality = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAuthorityFunctionality).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
