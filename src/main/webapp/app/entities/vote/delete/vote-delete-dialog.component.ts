import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVote } from '../vote.model';
import { VoteService } from '../service/vote.service';

@Component({
  standalone: true,
  templateUrl: './vote-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VoteDeleteDialogComponent {
  vote?: IVote;

  protected voteService = inject(VoteService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.voteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
