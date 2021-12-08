import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPerson, Person } from '../person.model';
import { PersonService } from '../service/person.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ItemCatalogueService } from 'app/entities/item-catalogue/service/item-catalogue.service';

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html',
})
export class PersonUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  itemCataloguesSharedCollection: IItemCatalogue[] = [];

  editForm = this.fb.group({
    id: [],
    fullName: [],
    fistName: [null, [Validators.required]],
    surname: [],
    email: [],
    identificactionNumber: [null, [Validators.required]],
    telephoneNumber: [],
    user: [],
    identificationType: [null, Validators.required],
  });

  constructor(
    protected personService: PersonService,
    protected userService: UserService,
    protected itemCatalogueService: ItemCatalogueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      this.updateForm(person);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const person = this.createFromForm();
    if (person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackItemCatalogueById(index: number, item: IItemCatalogue): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
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

  protected updateForm(person: IPerson): void {
    this.editForm.patchValue({
      id: person.id,
      fullName: person.fullName,
      fistName: person.fistName,
      surname: person.surname,
      email: person.email,
      identificactionNumber: person.identificactionNumber,
      telephoneNumber: person.telephoneNumber,
      user: person.user,
      identificationType: person.identificationType,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, person.user);
    this.itemCataloguesSharedCollection = this.itemCatalogueService.addItemCatalogueToCollectionIfMissing(
      this.itemCataloguesSharedCollection,
      person.identificationType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.itemCatalogueService
      .query()
      .pipe(map((res: HttpResponse<IItemCatalogue[]>) => res.body ?? []))
      .pipe(
        map((itemCatalogues: IItemCatalogue[]) =>
          this.itemCatalogueService.addItemCatalogueToCollectionIfMissing(itemCatalogues, this.editForm.get('identificationType')!.value)
        )
      )
      .subscribe((itemCatalogues: IItemCatalogue[]) => (this.itemCataloguesSharedCollection = itemCatalogues));
  }

  protected createFromForm(): IPerson {
    return {
      ...new Person(),
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      fistName: this.editForm.get(['fistName'])!.value,
      surname: this.editForm.get(['surname'])!.value,
      email: this.editForm.get(['email'])!.value,
      identificactionNumber: this.editForm.get(['identificactionNumber'])!.value,
      telephoneNumber: this.editForm.get(['telephoneNumber'])!.value,
      user: this.editForm.get(['user'])!.value,
      identificationType: this.editForm.get(['identificationType'])!.value,
    };
  }
}
