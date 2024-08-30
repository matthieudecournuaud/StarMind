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
import { IReward } from 'app/entities/reward/reward.model';
import { RewardService } from 'app/entities/reward/service/reward.service';
import { IIdea } from 'app/entities/idea/idea.model';
import { IdeaService } from 'app/entities/idea/service/idea.service';
import { RewardHistoryService } from '../service/reward-history.service';
import { IRewardHistory } from '../reward-history.model';
import { RewardHistoryFormGroup, RewardHistoryFormService } from './reward-history-form.service';

@Component({
  standalone: true,
  selector: 'jhi-reward-history-update',
  templateUrl: './reward-history-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RewardHistoryUpdateComponent implements OnInit {
  isSaving = false;
  rewardHistory: IRewardHistory | null = null;

  rewardsSharedCollection: IReward[] = [];
  ideasSharedCollection: IIdea[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected rewardHistoryService = inject(RewardHistoryService);
  protected rewardHistoryFormService = inject(RewardHistoryFormService);
  protected rewardService = inject(RewardService);
  protected ideaService = inject(IdeaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RewardHistoryFormGroup = this.rewardHistoryFormService.createRewardHistoryFormGroup();

  compareReward = (o1: IReward | null, o2: IReward | null): boolean => this.rewardService.compareReward(o1, o2);

  compareIdea = (o1: IIdea | null, o2: IIdea | null): boolean => this.ideaService.compareIdea(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rewardHistory }) => {
      this.rewardHistory = rewardHistory;
      if (rewardHistory) {
        this.updateForm(rewardHistory);
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
    const rewardHistory = this.rewardHistoryFormService.getRewardHistory(this.editForm);
    if (rewardHistory.id !== null) {
      this.subscribeToSaveResponse(this.rewardHistoryService.update(rewardHistory));
    } else {
      this.subscribeToSaveResponse(this.rewardHistoryService.create(rewardHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRewardHistory>>): void {
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

  protected updateForm(rewardHistory: IRewardHistory): void {
    this.rewardHistory = rewardHistory;
    this.rewardHistoryFormService.resetForm(this.editForm, rewardHistory);

    this.rewardsSharedCollection = this.rewardService.addRewardToCollectionIfMissing<IReward>(
      this.rewardsSharedCollection,
      rewardHistory.reward,
    );
    this.ideasSharedCollection = this.ideaService.addIdeaToCollectionIfMissing<IIdea>(this.ideasSharedCollection, rewardHistory.idea);
  }

  protected loadRelationshipsOptions(): void {
    this.rewardService
      .query()
      .pipe(map((res: HttpResponse<IReward[]>) => res.body ?? []))
      .pipe(map((rewards: IReward[]) => this.rewardService.addRewardToCollectionIfMissing<IReward>(rewards, this.rewardHistory?.reward)))
      .subscribe((rewards: IReward[]) => (this.rewardsSharedCollection = rewards));

    this.ideaService
      .query()
      .pipe(map((res: HttpResponse<IIdea[]>) => res.body ?? []))
      .pipe(map((ideas: IIdea[]) => this.ideaService.addIdeaToCollectionIfMissing<IIdea>(ideas, this.rewardHistory?.idea)))
      .subscribe((ideas: IIdea[]) => (this.ideasSharedCollection = ideas));
  }
}
