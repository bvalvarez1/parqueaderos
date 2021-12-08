jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { UserAuthorityService } from '../service/user-authority.service';

import { UserAuthorityDeleteDialogComponent } from './user-authority-delete-dialog.component';

describe('Component Tests', () => {
  describe('UserAuthority Management Delete Component', () => {
    let comp: UserAuthorityDeleteDialogComponent;
    let fixture: ComponentFixture<UserAuthorityDeleteDialogComponent>;
    let service: UserAuthorityService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UserAuthorityDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(UserAuthorityDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserAuthorityDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(UserAuthorityService);
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
