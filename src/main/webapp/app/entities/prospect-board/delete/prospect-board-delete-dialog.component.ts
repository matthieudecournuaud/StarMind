import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProspectBoard } from '../prospect-board.model';
import { ProspectBoardService } from '../service/prospect-board.service';

@Component({
  standalone: true,
  templateUrl: './prospect-board-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProspectBoardDeleteDialogComponent {
  prospectBoard?: IProspectBoard;

  protected prospectBoardService = inject(ProspectBoardService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prospectBoardService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
