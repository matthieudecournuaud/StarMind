import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IProspectBoard } from 'app/entities/prospect-board/prospect-board.model';
import { ProspectBoardService } from 'app/entities/prospect-board/service/prospect-board.service';
import { ProspectStatus } from 'app/entities/enumerations/prospect-status.model';
import { ProspectEntryService } from '../service/prospect-entry.service';
import { IProspectEntry } from '../prospect-entry.model';
import { ProspectEntryFormGroup, ProspectEntryFormService } from './prospect-entry-form.service';

@Component({
  standalone: true,
  selector: 'jhi-prospect-entry-update',
  templateUrl: './prospect-entry-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProspectEntryUpdateComponent implements OnInit {
  isSaving = false;
  prospectEntry: IProspectEntry | null = null;
  prospectStatusValues = Object.keys(ProspectStatus);

  usersSharedCollection: IUser[] = [];
  prospectBoardsSharedCollection: IProspectBoard[] = [];

  protected prospectEntryService = inject(ProspectEntryService);
  protected prospectEntryFormService = inject(ProspectEntryFormService);
  protected userService = inject(UserService);
  protected prospectBoardService = inject(ProspectBoardService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProspectEntryFormGroup = this.prospectEntryFormService.createProspectEntryFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareProspectBoard = (o1: IProspectBoard | null, o2: IProspectBoard | null): boolean =>
    this.prospectBoardService.compareProspectBoard(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prospectEntry }) => {
      this.prospectEntry = prospectEntry;
      if (prospectEntry) {
        this.updateForm(prospectEntry);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prospectEntry = this.prospectEntryFormService.getProspectEntry(this.editForm);
    if (prospectEntry.id !== null) {
      this.subscribeToSaveResponse(this.prospectEntryService.update(prospectEntry));
    } else {
      this.subscribeToSaveResponse(this.prospectEntryService.create(prospectEntry));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProspectEntry>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
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

  protected updateForm(prospectEntry: IProspectEntry): void {
    this.prospectEntry = prospectEntry;
    this.prospectEntryFormService.resetForm(this.editForm, prospectEntry);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, prospectEntry.prospect);
    this.prospectBoardsSharedCollection = this.prospectBoardService.addProspectBoardToCollectionIfMissing<IProspectBoard>(
      this.prospectBoardsSharedCollection,
      prospectEntry.prospectBoard,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.prospectEntry?.prospect)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.prospectBoardService
      .query()
      .pipe(map((res: HttpResponse<IProspectBoard[]>) => res.body ?? []))
      .pipe(
        map((prospectBoards: IProspectBoard[]) =>
          this.prospectBoardService.addProspectBoardToCollectionIfMissing<IProspectBoard>(
            prospectBoards,
            this.prospectEntry?.prospectBoard,
          ),
        ),
      )
      .subscribe((prospectBoards: IProspectBoard[]) => (this.prospectBoardsSharedCollection = prospectBoards));
  }
}
