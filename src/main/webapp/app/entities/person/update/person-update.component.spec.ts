jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonService } from '../service/person.service';
import { IPerson, Person } from '../person.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ItemCatalogueService } from 'app/entities/item-catalogue/service/item-catalogue.service';

import { PersonUpdateComponent } from './person-update.component';

describe('Component Tests', () => {
  describe('Person Management Update Component', () => {
    let comp: PersonUpdateComponent;
    let fixture: ComponentFixture<PersonUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personService: PersonService;
    let userService: UserService;
    let itemCatalogueService: ItemCatalogueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personService = TestBed.inject(PersonService);
      userService = TestBed.inject(UserService);
      itemCatalogueService = TestBed.inject(ItemCatalogueService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const person: IPerson = { id: 456 };
        const user: IUser = { id: 57046 };
        person.user = user;

        const userCollection: IUser[] = [{ id: 27076 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ItemCatalogue query and add missing value', () => {
        const person: IPerson = { id: 456 };
        const identificationType: IItemCatalogue = { id: 91187 };
        person.identificationType = identificationType;

        const itemCatalogueCollection: IItemCatalogue[] = [{ id: 70065 }];
        jest.spyOn(itemCatalogueService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCatalogueCollection })));
        const additionalItemCatalogues = [identificationType];
        const expectedCollection: IItemCatalogue[] = [...additionalItemCatalogues, ...itemCatalogueCollection];
        jest.spyOn(itemCatalogueService, 'addItemCatalogueToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(itemCatalogueService.query).toHaveBeenCalled();
        expect(itemCatalogueService.addItemCatalogueToCollectionIfMissing).toHaveBeenCalledWith(
          itemCatalogueCollection,
          ...additionalItemCatalogues
        );
        expect(comp.itemCataloguesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const person: IPerson = { id: 456 };
        const user: IUser = { id: 40784 };
        person.user = user;
        const identificationType: IItemCatalogue = { id: 56607 };
        person.identificationType = identificationType;

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(person));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.itemCataloguesSharedCollection).toContain(identificationType);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Person>>();
        const person = { id: 123 };
        jest.spyOn(personService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: person }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personService.update).toHaveBeenCalledWith(person);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Person>>();
        const person = new Person();
        jest.spyOn(personService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: person }));
        saveSubject.complete();

        // THEN
        expect(personService.create).toHaveBeenCalledWith(person);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Person>>();
        const person = { id: 123 };
        jest.spyOn(personService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personService.update).toHaveBeenCalledWith(person);
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

      describe('trackItemCatalogueById', () => {
        it('Should return tracked ItemCatalogue primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackItemCatalogueById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
