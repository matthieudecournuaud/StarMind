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
import { IIdea } from 'app/entities/idea/idea.model';
import { IdeaService } from 'app/entities/idea/service/idea.service';
import { VoteOption } from 'app/entities/enumerations/vote-option.model';
import { VoteService } from '../service/vote.service';
import { IVote } from '../vote.model';
import { VoteFormGroup, VoteFormService } from './vote-form.service';

@Component({
  standalone: true,
  selector: 'jhi-vote-update',
  templateUrl: './vote-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VoteUpdateComponent implements OnInit {
  isSaving = false;
  vote: IVote | null = null;
  voteOptionValues = Object.keys(VoteOption);

  usersSharedCollection: IUser[] = [];
  ideasSharedCollection: IIdea[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected voteService = inject(VoteService);
  protected voteFormService = inject(VoteFormService);
  protected userService = inject(UserService);
  protected ideaService = inject(IdeaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VoteFormGroup = this.voteFormService.createVoteFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareIdea = (o1: IIdea | null, o2: IIdea | null): boolean => this.ideaService.compareIdea(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vote }) => {
      this.vote = vote;
      if (vote) {
        this.updateForm(vote);
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
    const vote = this.voteFormService.getVote(this.editForm);
    if (vote.id !== null) {
      this.subscribeToSaveResponse(this.voteService.update(vote));
    } else {
      this.subscribeToSaveResponse(this.voteService.create(vote));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVote>>): void {
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

  protected updateForm(vote: IVote): void {
    this.vote = vote;
    this.voteFormService.resetForm(this.editForm, vote);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, vote.voter);
    this.ideasSharedCollection = this.ideaService.addIdeaToCollectionIfMissing<IIdea>(this.ideasSharedCollection, vote.idea);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.vote?.voter)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.ideaService
      .query()
      .pipe(map((res: HttpResponse<IIdea[]>) => res.body ?? []))
      .pipe(map((ideas: IIdea[]) => this.ideaService.addIdeaToCollectionIfMissing<IIdea>(ideas, this.vote?.idea)))
      .subscribe((ideas: IIdea[]) => (this.ideasSharedCollection = ideas));
  }
}
