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
import { IIdea } from 'app/entities/idea/idea.model';
import { IdeaService } from 'app/entities/idea/service/idea.service';
import { RewardType } from 'app/entities/enumerations/reward-type.model';
import { IdeaHistoryService } from '../service/idea-history.service';
import { IIdeaHistory } from '../idea-history.model';
import { IdeaHistoryFormGroup, IdeaHistoryFormService } from './idea-history-form.service';

@Component({
  standalone: true,
  selector: 'jhi-idea-history-update',
  templateUrl: './idea-history-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class IdeaHistoryUpdateComponent implements OnInit {
  isSaving = false;
  ideaHistory: IIdeaHistory | null = null;
  rewardTypeValues = Object.keys(RewardType);

  ideasSharedCollection: IIdea[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected ideaHistoryService = inject(IdeaHistoryService);
  protected ideaHistoryFormService = inject(IdeaHistoryFormService);
  protected ideaService = inject(IdeaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: IdeaHistoryFormGroup = this.ideaHistoryFormService.createIdeaHistoryFormGroup();

  compareIdea = (o1: IIdea | null, o2: IIdea | null): boolean => this.ideaService.compareIdea(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ideaHistory }) => {
      this.ideaHistory = ideaHistory;
      if (ideaHistory) {
        this.updateForm(ideaHistory);
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
    const ideaHistory = this.ideaHistoryFormService.getIdeaHistory(this.editForm);
    if (ideaHistory.id !== null) {
      this.subscribeToSaveResponse(this.ideaHistoryService.update(ideaHistory));
    } else {
      this.subscribeToSaveResponse(this.ideaHistoryService.create(ideaHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIdeaHistory>>): void {
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

  protected updateForm(ideaHistory: IIdeaHistory): void {
    this.ideaHistory = ideaHistory;
    this.ideaHistoryFormService.resetForm(this.editForm, ideaHistory);

    this.ideasSharedCollection = this.ideaService.addIdeaToCollectionIfMissing<IIdea>(this.ideasSharedCollection, ideaHistory.idea);
  }

  protected loadRelationshipsOptions(): void {
    this.ideaService
      .query()
      .pipe(map((res: HttpResponse<IIdea[]>) => res.body ?? []))
      .pipe(map((ideas: IIdea[]) => this.ideaService.addIdeaToCollectionIfMissing<IIdea>(ideas, this.ideaHistory?.idea)))
      .subscribe((ideas: IIdea[]) => (this.ideasSharedCollection = ideas));
  }
}
