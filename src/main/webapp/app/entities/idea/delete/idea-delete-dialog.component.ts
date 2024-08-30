import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IIdea } from '../idea.model';
import { IdeaService } from '../service/idea.service';

@Component({
  standalone: true,
  templateUrl: './idea-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class IdeaDeleteDialogComponent {
  idea?: IIdea;

  protected ideaService = inject(IdeaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ideaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
