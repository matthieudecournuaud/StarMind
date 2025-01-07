import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ProspectBoardService } from '../service/prospect-board.service';
import { IProspectBoard } from '../prospect-board.model';
import { ProspectBoardFormGroup, ProspectBoardFormService } from './prospect-board-form.service';

@Component({
  standalone: true,
  selector: 'jhi-prospect-board-update',
  templateUrl: './prospect-board-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProspectBoardUpdateComponent implements OnInit {
  isSaving = false;
  prospectBoard: IProspectBoard | null = null;

  usersSharedCollection: IUser[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected prospectBoardService = inject(ProspectBoardService);
  protected prospectBoardFormService = inject(ProspectBoardFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProspectBoardFormGroup = this.prospectBoardFormService.createProspectBoardFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prospectBoard }) => {
      this.prospectBoard = prospectBoard;
      if (prospectBoard) {
        this.updateForm(prospectBoard);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('starMindApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prospectBoard = this.prospectBoardFormService.getProspectBoard(this.editForm);
    if (prospectBoard.id !== null) {
      this.subscribeToSaveResponse(this.prospectBoardService.update(prospectBoard));
    } else {
      this.subscribeToSaveResponse(this.prospectBoardService.create(prospectBoard));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProspectBoard>>): void {
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

  protected updateForm(prospectBoard: IProspectBoard): void {
    this.prospectBoard = prospectBoard;
    this.prospectBoardFormService.resetForm(this.editForm, prospectBoard);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, prospectBoard.owner);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.prospectBoard?.owner)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
