import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUserAuthorityInstitution, UserAuthorityInstitution } from '../user-authority-institution.model';
import { UserAuthorityInstitutionService } from '../service/user-authority-institution.service';
import { IInstitution } from 'app/entities/institution/institution.model';
import { InstitutionService } from 'app/entities/institution/service/institution.service';
import { IUserAuthority, UserAuthority } from 'app/entities/user-authority/user-authority.model';
import { UserAuthorityService } from 'app/entities/user-authority/service/user-authority.service';
import { UserService } from 'app/entities/user/user.service';
import { IUser } from 'app/admin/user-management/user-management.model';

@Component({
  selector: 'jhi-user-authority-institution-update',
  templateUrl: './user-authority-institution-update.component.html',
})
export class UserAuthorityInstitutionUpdateComponent implements OnInit {
  isSaving = false;

  institutionsSharedCollection: IInstitution[] = [];
  userAuthoritiesSharedCollection: IUserAuthority[] = [];
  userSharedCollection: IUser[] = [];
  userAuthority: IUserAuthority | any;

  editForm = this.fb.group({
    id: [],
    active: [],
    institution: [],
    user: [],
  });

  constructor(
    protected userAuthorityInstitutionService: UserAuthorityInstitutionService,
    protected institutionService: InstitutionService,
    protected userAuthorityService: UserAuthorityService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAuthorityInstitution }) => {
      this.updateForm(userAuthorityInstitution);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userAuthorityInstitution = this.createFromForm();

    if (userAuthorityInstitution.id !== undefined) {
      //  this.subscribeToSaveResponse(this.userAuthorityInstitutionService.update(userAuthorityInstitution));
    } else {
      //crear el user authority
      this.userAuthorityService.findByUserid(userAuthorityInstitution.user!.id!).subscribe(
        (res: HttpResponse<IUserAuthority>) => {
          this.userAuthority = res.body;
          // eslint-disable-next-line no-console
          console.log(res.body);
          const userAuthInst = new UserAuthorityInstitution();
          userAuthInst.active = userAuthorityInstitution.active;
          userAuthInst.institution = userAuthorityInstitution.institution;
          userAuthInst.userAuthority = this.userAuthority;
          this.subscribeToSaveResponse(this.userAuthorityInstitutionService.create(userAuthInst));
        },
        () => {
          // eslint-disable-next-line no-console
          console.log('error');
        }
      );

      // eslint-disable-next-line no-console
      console.log('//');

      // eslint-disable-next-line no-console
      console.log(this.userAuthority);
      /*
      this.userAuthorityService.create(userAuthority);
 
      id: this.editForm.get(['id'])!.value,
      authority: this.editForm.get(['authority'])!.value,
      active: this.editForm.get(['active'])!.value,p[]p]    */

      // this.subscribeToSaveResponse(this.userAuthorityInstitutionService.create(userAuthorityInstitution));
    }
  }

  trackInstitutionById(index: number, item: IInstitution): number {
    return item.id!;
  }

  trackUserAuthorityById(index: number, item: IUserAuthority): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserAuthorityInstitution>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userAuthorityInstitution: IUserAuthorityInstitution): void {
    this.editForm.patchValue({
      id: userAuthorityInstitution.id,
      active: userAuthorityInstitution.active,
      institution: userAuthorityInstitution.institution,
      userAuthority: userAuthorityInstitution.userAuthority,
    });

    this.institutionsSharedCollection = this.institutionService.addInstitutionToCollectionIfMissing(
      this.institutionsSharedCollection,
      userAuthorityInstitution.institution
    );
    this.userAuthoritiesSharedCollection = this.userAuthorityService.addUserAuthorityToCollectionIfMissing(
      this.userAuthoritiesSharedCollection,
      userAuthorityInstitution.userAuthority
    );

    this.userSharedCollection = this.userService.addUserToCollectionIfMissing(this.userSharedCollection, userAuthorityInstitution.user);
  }

  protected loadRelationshipsOptions(): void {
    this.institutionService
      .query()
      .pipe(map((res: HttpResponse<IInstitution[]>) => res.body ?? []))
      .pipe(
        map((institutions: IInstitution[]) =>
          this.institutionService.addInstitutionToCollectionIfMissing(institutions, this.editForm.get('institution')!.value)
        )
      )
      .subscribe((institutions: IInstitution[]) => (this.institutionsSharedCollection = institutions));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.userSharedCollection = users));
  }

  protected createFromForm(): IUserAuthorityInstitution {
    return {
      ...new UserAuthorityInstitution(),
      id: this.editForm.get(['id'])!.value,
      active: this.editForm.get(['active'])!.value,
      institution: this.editForm.get(['institution'])!.value,
      //userAuthority: this.editForm.get(['userAuthority'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
