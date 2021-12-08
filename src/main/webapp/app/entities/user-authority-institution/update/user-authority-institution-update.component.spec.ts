jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UserAuthorityInstitutionService } from '../service/user-authority-institution.service';
import { IUserAuthorityInstitution, UserAuthorityInstitution } from '../user-authority-institution.model';
import { IInstitution } from 'app/entities/institution/institution.model';
import { InstitutionService } from 'app/entities/institution/service/institution.service';
import { IUserAuthority } from 'app/entities/user-authority/user-authority.model';
import { UserAuthorityService } from 'app/entities/user-authority/service/user-authority.service';

import { UserAuthorityInstitutionUpdateComponent } from './user-authority-institution-update.component';

describe('Component Tests', () => {
  describe('UserAuthorityInstitution Management Update Component', () => {
    let comp: UserAuthorityInstitutionUpdateComponent;
    let fixture: ComponentFixture<UserAuthorityInstitutionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let userAuthorityInstitutionService: UserAuthorityInstitutionService;
    let institutionService: InstitutionService;
    let userAuthorityService: UserAuthorityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UserAuthorityInstitutionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UserAuthorityInstitutionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserAuthorityInstitutionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      userAuthorityInstitutionService = TestBed.inject(UserAuthorityInstitutionService);
      institutionService = TestBed.inject(InstitutionService);
      userAuthorityService = TestBed.inject(UserAuthorityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Institution query and add missing value', () => {
        const userAuthorityInstitution: IUserAuthorityInstitution = { id: 456 };
        const institution: IInstitution = { id: 60633 };
        userAuthorityInstitution.institution = institution;

        const institutionCollection: IInstitution[] = [{ id: 44347 }];
        jest.spyOn(institutionService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCollection })));
        const additionalInstitutions = [institution];
        const expectedCollection: IInstitution[] = [...additionalInstitutions, ...institutionCollection];
        jest.spyOn(institutionService, 'addInstitutionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ userAuthorityInstitution });
        comp.ngOnInit();

        expect(institutionService.query).toHaveBeenCalled();
        expect(institutionService.addInstitutionToCollectionIfMissing).toHaveBeenCalledWith(
          institutionCollection,
          ...additionalInstitutions
        );
        expect(comp.institutionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call UserAuthority query and add missing value', () => {
        const userAuthorityInstitution: IUserAuthorityInstitution = { id: 456 };
        const userAuthority: IUserAuthority = { id: 3077 };
        userAuthorityInstitution.userAuthority = userAuthority;

        const userAuthorityCollection: IUserAuthority[] = [{ id: 99741 }];
        jest.spyOn(userAuthorityService, 'query').mockReturnValue(of(new HttpResponse({ body: userAuthorityCollection })));
        const additionalUserAuthorities = [userAuthority];
        const expectedCollection: IUserAuthority[] = [...additionalUserAuthorities, ...userAuthorityCollection];
        jest.spyOn(userAuthorityService, 'addUserAuthorityToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ userAuthorityInstitution });
        comp.ngOnInit();

        expect(userAuthorityService.query).toHaveBeenCalled();
        expect(userAuthorityService.addUserAuthorityToCollectionIfMissing).toHaveBeenCalledWith(
          userAuthorityCollection,
          ...additionalUserAuthorities
        );
        expect(comp.userAuthoritiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const userAuthorityInstitution: IUserAuthorityInstitution = { id: 456 };
        const institution: IInstitution = { id: 83831 };
        userAuthorityInstitution.institution = institution;
        const userAuthority: IUserAuthority = { id: 12881 };
        userAuthorityInstitution.userAuthority = userAuthority;

        activatedRoute.data = of({ userAuthorityInstitution });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(userAuthorityInstitution));
        expect(comp.institutionsSharedCollection).toContain(institution);
        expect(comp.userAuthoritiesSharedCollection).toContain(userAuthority);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserAuthorityInstitution>>();
        const userAuthorityInstitution = { id: 123 };
        jest.spyOn(userAuthorityInstitutionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userAuthorityInstitution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userAuthorityInstitution }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(userAuthorityInstitutionService.update).toHaveBeenCalledWith(userAuthorityInstitution);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserAuthorityInstitution>>();
        const userAuthorityInstitution = new UserAuthorityInstitution();
        jest.spyOn(userAuthorityInstitutionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userAuthorityInstitution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userAuthorityInstitution }));
        saveSubject.complete();

        // THEN
        expect(userAuthorityInstitutionService.create).toHaveBeenCalledWith(userAuthorityInstitution);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserAuthorityInstitution>>();
        const userAuthorityInstitution = { id: 123 };
        jest.spyOn(userAuthorityInstitutionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userAuthorityInstitution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(userAuthorityInstitutionService.update).toHaveBeenCalledWith(userAuthorityInstitution);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackInstitutionById', () => {
        it('Should return tracked Institution primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackInstitutionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserAuthorityById', () => {
        it('Should return tracked UserAuthority primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserAuthorityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
