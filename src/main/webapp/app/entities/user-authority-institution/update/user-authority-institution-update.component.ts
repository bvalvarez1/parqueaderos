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
import { IUserAuthority } from 'app/entities/user-authority/user-authority.model';
import { UserAuthorityService } from 'app/entities/user-authority/service/user-authority.service';

@Component({
  selector: 'jhi-user-authority-institution-update',
  templateUrl: './user-authority-institution-update.component.html',
})
export class UserAuthorityInstitutionUpdateComponent implements OnInit {
  isSaving = false;

  institutionsSharedCollection: IInstitution[] = [];
  userAuthoritiesSharedCollection: IUserAuthority[] = [];

  editForm = this.fb.group({
    id: [],
    active: [],
    institution: [],
    userAuthority: [],
  });

  constructor(
    protected userAuthorityInstitutionService: UserAuthorityInstitutionService,
    protected institutionService: InstitutionService,
    protected userAuthorityService: UserAuthorityService,
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
      this.subscribeToSaveResponse(this.userAuthorityInstitutionService.update(userAuthorityInstitution));
    } else {
      this.subscribeToSaveResponse(this.userAuthorityInstitutionService.create(userAuthorityInstitution));
    }
  }

  trackInstitutionById(index: number, item: IInstitution): number {
    return item.id!;
  }

  trackUserAuthorityById(index: number, item: IUserAuthority): number {
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

    this.userAuthorityService
      .query()
      .pipe(map((res: HttpResponse<IUserAuthority[]>) => res.body ?? []))
      .pipe(
        map((userAuthorities: IUserAuthority[]) =>
          this.userAuthorityService.addUserAuthorityToCollectionIfMissing(userAuthorities, this.editForm.get('userAuthority')!.value)
        )
      )
      .subscribe((userAuthorities: IUserAuthority[]) => (this.userAuthoritiesSharedCollection = userAuthorities));
  }

  protected createFromForm(): IUserAuthorityInstitution {
    return {
      ...new UserAuthorityInstitution(),
      id: this.editForm.get(['id'])!.value,
      active: this.editForm.get(['active'])!.value,
      institution: this.editForm.get(['institution'])!.value,
      userAuthority: this.editForm.get(['userAuthority'])!.value,
    };
  }
}
