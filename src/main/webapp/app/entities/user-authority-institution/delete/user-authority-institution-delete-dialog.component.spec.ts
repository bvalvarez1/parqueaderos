jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { UserAuthorityInstitutionService } from '../service/user-authority-institution.service';

import { UserAuthorityInstitutionDeleteDialogComponent } from './user-authority-institution-delete-dialog.component';

describe('Component Tests', () => {
  describe('UserAuthorityInstitution Management Delete Component', () => {
    let comp: UserAuthorityInstitutionDeleteDialogComponent;
    let fixture: ComponentFixture<UserAuthorityInstitutionDeleteDialogComponent>;
    let service: UserAuthorityInstitutionService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UserAuthorityInstitutionDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(UserAuthorityInstitutionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserAuthorityInstitutionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(UserAuthorityInstitutionService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
