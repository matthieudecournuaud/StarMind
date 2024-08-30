import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRewardHistory } from '../reward-history.model';
import { RewardHistoryService } from '../service/reward-history.service';

@Component({
  standalone: true,
  templateUrl: './reward-history-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RewardHistoryDeleteDialogComponent {
  rewardHistory?: IRewardHistory;

  protected rewardHistoryService = inject(RewardHistoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rewardHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
