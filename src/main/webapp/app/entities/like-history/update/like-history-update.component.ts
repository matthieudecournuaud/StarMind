import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IIdea } from 'app/entities/idea/idea.model';
import { IdeaService } from 'app/entities/idea/service/idea.service';
import { ILikeHistory } from '../like-history.model';
import { LikeHistoryService } from '../service/like-history.service';
import { LikeHistoryFormGroup, LikeHistoryFormService } from './like-history-form.service';

@Component({
  standalone: true,
  selector: 'jhi-like-history-update',
  templateUrl: './like-history-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LikeHistoryUpdateComponent implements OnInit {
  isSaving = false;
  likeHistory: ILikeHistory | null = null;

  ideasSharedCollection: IIdea[] = [];

  protected likeHistoryService = inject(LikeHistoryService);
  protected likeHistoryFormService = inject(LikeHistoryFormService);
  protected ideaService = inject(IdeaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LikeHistoryFormGroup = this.likeHistoryFormService.createLikeHistoryFormGroup();

  compareIdea = (o1: IIdea | null, o2: IIdea | null): boolean => this.ideaService.compareIdea(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ likeHistory }) => {
      this.likeHistory = likeHistory;
      if (likeHistory) {
        this.updateForm(likeHistory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const likeHistory = this.likeHistoryFormService.getLikeHistory(this.editForm);
    if (likeHistory.id !== null) {
      this.subscribeToSaveResponse(this.likeHistoryService.update(likeHistory));
    } else {
      this.subscribeToSaveResponse(this.likeHistoryService.create(likeHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILikeHistory>>): void {
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

  protected updateForm(likeHistory: ILikeHistory): void {
    this.likeHistory = likeHistory;
    this.likeHistoryFormService.resetForm(this.editForm, likeHistory);

    this.ideasSharedCollection = this.ideaService.addIdeaToCollectionIfMissing<IIdea>(this.ideasSharedCollection, likeHistory.idea);
  }

  protected loadRelationshipsOptions(): void {
    this.ideaService
      .query()
      .pipe(map((res: HttpResponse<IIdea[]>) => res.body ?? []))
      .pipe(map((ideas: IIdea[]) => this.ideaService.addIdeaToCollectionIfMissing<IIdea>(ideas, this.likeHistory?.idea)))
      .subscribe((ideas: IIdea[]) => (this.ideasSharedCollection = ideas));
  }
}
