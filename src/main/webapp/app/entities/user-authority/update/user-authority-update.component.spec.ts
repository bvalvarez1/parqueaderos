jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UserAuthorityService } from '../service/user-authority.service';
import { IUserAuthority, UserAuthority } from '../user-authority.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { UserAuthorityUpdateComponent } from './user-authority-update.component';

describe('Component Tests', () => {
  describe('UserAuthority Management Update Component', () => {
    let comp: UserAuthorityUpdateComponent;
    let fixture: ComponentFixture<UserAuthorityUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let userAuthorityService: UserAuthorityService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UserAuthorityUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UserAuthorityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserAuthorityUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      userAuthorityService = TestBed.inject(UserAuthorityService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const userAuthority: IUserAuthority = { id: 456 };
        const user: IUser = { id: 61565 };
        userAuthority.user = user;

        const userCollection: IUser[] = [{ id: 8701 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ userAuthority });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const userAuthority: IUserAuthority = { id: 456 };
        const user: IUser = { id: 37760 };
        userAuthority.user = user;

        activatedRoute.data = of({ userAuthority });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(userAuthority));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserAuthority>>();
        const userAuthority = { id: 123 };
        jest.spyOn(userAuthorityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userAuthority });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userAuthority }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(userAuthorityService.update).toHaveBeenCalledWith(userAuthority);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserAuthority>>();
        const userAuthority = new UserAuthority();
        jest.spyOn(userAuthorityService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userAuthority });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userAuthority }));
        saveSubject.complete();

        // THEN
        expect(userAuthorityService.create).toHaveBeenCalledWith(userAuthority);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserAuthority>>();
        const userAuthority = { id: 123 };
        jest.spyOn(userAuthorityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userAuthority });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(userAuthorityService.update).toHaveBeenCalledWith(userAuthority);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
