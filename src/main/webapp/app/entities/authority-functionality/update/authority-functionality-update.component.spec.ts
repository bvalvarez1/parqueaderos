jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AuthorityFunctionalityService } from '../service/authority-functionality.service';
import { IAuthorityFunctionality, AuthorityFunctionality } from '../authority-functionality.model';
import { IFunctionality } from 'app/entities/functionality/functionality.model';
import { FunctionalityService } from 'app/entities/functionality/service/functionality.service';

import { AuthorityFunctionalityUpdateComponent } from './authority-functionality-update.component';

describe('Component Tests', () => {
  describe('AuthorityFunctionality Management Update Component', () => {
    let comp: AuthorityFunctionalityUpdateComponent;
    let fixture: ComponentFixture<AuthorityFunctionalityUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let authorityFunctionalityService: AuthorityFunctionalityService;
    let functionalityService: FunctionalityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AuthorityFunctionalityUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AuthorityFunctionalityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AuthorityFunctionalityUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      authorityFunctionalityService = TestBed.inject(AuthorityFunctionalityService);
      functionalityService = TestBed.inject(FunctionalityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Functionality query and add missing value', () => {
        const authorityFunctionality: IAuthorityFunctionality = { id: 456 };
        const functionality: IFunctionality = { id: 70072 };
        authorityFunctionality.functionality = functionality;

        const functionalityCollection: IFunctionality[] = [{ id: 68876 }];
        jest.spyOn(functionalityService, 'query').mockReturnValue(of(new HttpResponse({ body: functionalityCollection })));
        const additionalFunctionalities = [functionality];
        const expectedCollection: IFunctionality[] = [...additionalFunctionalities, ...functionalityCollection];
        jest.spyOn(functionalityService, 'addFunctionalityToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ authorityFunctionality });
        comp.ngOnInit();

        expect(functionalityService.query).toHaveBeenCalled();
        expect(functionalityService.addFunctionalityToCollectionIfMissing).toHaveBeenCalledWith(
          functionalityCollection,
          ...additionalFunctionalities
        );
        expect(comp.functionalitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const authorityFunctionality: IAuthorityFunctionality = { id: 456 };
        const functionality: IFunctionality = { id: 12654 };
        authorityFunctionality.functionality = functionality;

        activatedRoute.data = of({ authorityFunctionality });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(authorityFunctionality));
        expect(comp.functionalitiesSharedCollection).toContain(functionality);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AuthorityFunctionality>>();
        const authorityFunctionality = { id: 123 };
        jest.spyOn(authorityFunctionalityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ authorityFunctionality });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: authorityFunctionality }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(authorityFunctionalityService.update).toHaveBeenCalledWith(authorityFunctionality);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AuthorityFunctionality>>();
        const authorityFunctionality = new AuthorityFunctionality();
        jest.spyOn(authorityFunctionalityService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ authorityFunctionality });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: authorityFunctionality }));
        saveSubject.complete();

        // THEN
        expect(authorityFunctionalityService.create).toHaveBeenCalledWith(authorityFunctionality);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AuthorityFunctionality>>();
        const authorityFunctionality = { id: 123 };
        jest.spyOn(authorityFunctionalityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ authorityFunctionality });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(authorityFunctionalityService.update).toHaveBeenCalledWith(authorityFunctionality);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFunctionalityById', () => {
        it('Should return tracked Functionality primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFunctionalityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
