jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CatalogueService } from '../service/catalogue.service';
import { ICatalogue, Catalogue } from '../catalogue.model';

import { CatalogueUpdateComponent } from './catalogue-update.component';

describe('Component Tests', () => {
  describe('Catalogue Management Update Component', () => {
    let comp: CatalogueUpdateComponent;
    let fixture: ComponentFixture<CatalogueUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let catalogueService: CatalogueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CatalogueUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CatalogueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CatalogueUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      catalogueService = TestBed.inject(CatalogueService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const catalogue: ICatalogue = { id: 456 };

        activatedRoute.data = of({ catalogue });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(catalogue));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Catalogue>>();
        const catalogue = { id: 123 };
        jest.spyOn(catalogueService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: catalogue }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(catalogueService.update).toHaveBeenCalledWith(catalogue);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Catalogue>>();
        const catalogue = new Catalogue();
        jest.spyOn(catalogueService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: catalogue }));
        saveSubject.complete();

        // THEN
        expect(catalogueService.create).toHaveBeenCalledWith(catalogue);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Catalogue>>();
        const catalogue = { id: 123 };
        jest.spyOn(catalogueService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(catalogueService.update).toHaveBeenCalledWith(catalogue);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
