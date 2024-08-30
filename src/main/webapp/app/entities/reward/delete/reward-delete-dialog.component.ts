import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReward } from '../reward.model';
import { RewardService } from '../service/reward.service';

@Component({
  standalone: true,
  templateUrl: './reward-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RewardDeleteDialogComponent {
  reward?: IReward;

  protected rewardService = inject(RewardService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rewardService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
