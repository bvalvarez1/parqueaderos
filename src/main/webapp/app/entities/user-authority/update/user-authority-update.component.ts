import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUserAuthority, UserAuthority } from '../user-authority.model';
import { UserAuthorityService } from '../service/user-authority.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-user-authority-update',
  templateUrl: './user-authority-update.component.html',
})
export class UserAuthorityUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    authority: [],
    active: [],
    user: [],
  });

  constructor(
    protected userAuthorityService: UserAuthorityService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAuthority }) => {
      this.updateForm(userAuthority);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userAuthority = this.createFromForm();
    if (userAuthority.id !== undefined) {
      this.subscribeToSaveResponse(this.userAuthorityService.update(userAuthority));
    } else {
      this.subscribeToSaveResponse(this.userAuthorityService.create(userAuthority));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserAuthority>>): void {
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

  protected updateForm(userAuthority: IUserAuthority): void {
    this.editForm.patchValue({
      id: userAuthority.id,
      authority: userAuthority.authority,
      active: userAuthority.active,
      user: userAuthority.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, userAuthority.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IUserAuthority {
    return {
      ...new UserAuthority(),
      id: this.editForm.get(['id'])!.value,
      authority: this.editForm.get(['authority'])!.value,
      active: this.editForm.get(['active'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
