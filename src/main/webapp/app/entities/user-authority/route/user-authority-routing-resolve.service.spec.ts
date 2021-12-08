jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUserAuthority, UserAuthority } from '../user-authority.model';
import { UserAuthorityService } from '../service/user-authority.service';

import { UserAuthorityRoutingResolveService } from './user-authority-routing-resolve.service';

describe('UserAuthority routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: UserAuthorityRoutingResolveService;
  let service: UserAuthorityService;
  let resultUserAuthority: IUserAuthority | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(UserAuthorityRoutingResolveService);
    service = TestBed.inject(UserAuthorityService);
    resultUserAuthority = undefined;
  });

  describe('resolve', () => {
    it('should return IUserAuthority returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUserAuthority = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUserAuthority).toEqual({ id: 123 });
    });

    it('should return new IUserAuthority if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUserAuthority = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultUserAuthority).toEqual(new UserAuthority());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as UserAuthority })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUserAuthority = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUserAuthority).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
