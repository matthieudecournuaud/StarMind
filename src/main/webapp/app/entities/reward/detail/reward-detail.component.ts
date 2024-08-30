import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IReward } from '../reward.model';

@Component({
  standalone: true,
  selector: 'jhi-reward-detail',
  templateUrl: './reward-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RewardDetailComponent {
  reward = input<IReward | null>(null);

  previousState(): void {
    window.history.back();
  }
}
