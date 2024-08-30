import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ILikeHistory } from '../like-history.model';

@Component({
  standalone: true,
  selector: 'jhi-like-history-detail',
  templateUrl: './like-history-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class LikeHistoryDetailComponent {
  likeHistory = input<ILikeHistory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
