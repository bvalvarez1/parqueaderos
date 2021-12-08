jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUserAuthorityInstitution, UserAuthorityInstitution } from '../user-authority-institution.model';
import { UserAuthorityInstitutionService } from '../service/user-authority-institution.service';

import { UserAuthorityInstitutionRoutingResolveService } from './user-authority-institution-routing-resolve.service';

describe('UserAuthorityInstitution routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: UserAuthorityInstitutionRoutingResolveService;
  let service: UserAuthorityInstitutionService;
  let resultUserAuthorityInstitution: IUserAuthorityInstitution | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(UserAuthorityInstitutionRoutingResolveService);
    service = TestBed.inject(UserAuthorityInstitutionService);
    resultUserAuthorityInstitution = undefined;
  });

  describe('resolve', () => {
    it('should return IUserAuthorityInstitution returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUserAuthorityInstitution = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUserAuthorityInstitution).toEqual({ id: 123 });
    });

    it('should return new IUserAuthorityInstitution if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUserAuthorityInstitution = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultUserAuthorityInstitution).toEqual(new UserAuthorityInstitution());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as UserAuthorityInstitution })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUserAuthorityInstitution = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUserAuthorityInstitution).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
