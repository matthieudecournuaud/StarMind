import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILikeHistory } from '../like-history.model';
import { LikeHistoryService } from '../service/like-history.service';

@Component({
  standalone: true,
  templateUrl: './like-history-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LikeHistoryDeleteDialogComponent {
  likeHistory?: ILikeHistory;

  protected likeHistoryService = inject(LikeHistoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.likeHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
