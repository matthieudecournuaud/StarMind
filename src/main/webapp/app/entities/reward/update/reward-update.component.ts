import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReward } from '../reward.model';
import { RewardService } from '../service/reward.service';
import { RewardFormGroup, RewardFormService } from './reward-form.service';

@Component({
  standalone: true,
  selector: 'jhi-reward-update',
  templateUrl: './reward-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RewardUpdateComponent implements OnInit {
  isSaving = false;
  reward: IReward | null = null;

  protected rewardService = inject(RewardService);
  protected rewardFormService = inject(RewardFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RewardFormGroup = this.rewardFormService.createRewardFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reward }) => {
      this.reward = reward;
      if (reward) {
        this.updateForm(reward);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reward = this.rewardFormService.getReward(this.editForm);
    if (reward.id !== null) {
      this.subscribeToSaveResponse(this.rewardService.update(reward));
    } else {
      this.subscribeToSaveResponse(this.rewardService.create(reward));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReward>>): void {
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

  protected updateForm(reward: IReward): void {
    this.reward = reward;
    this.rewardFormService.resetForm(this.editForm, reward);
  }
}
