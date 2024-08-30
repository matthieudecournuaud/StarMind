import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IIdeaHistory } from '../idea-history.model';
import { IdeaHistoryService } from '../service/idea-history.service';

@Component({
  standalone: true,
  templateUrl: './idea-history-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class IdeaHistoryDeleteDialogComponent {
  ideaHistory?: IIdeaHistory;

  protected ideaHistoryService = inject(IdeaHistoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ideaHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
