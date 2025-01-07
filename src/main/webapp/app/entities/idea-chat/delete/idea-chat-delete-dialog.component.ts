import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IIdeaChat } from '../idea-chat.model';
import { IdeaChatService } from '../service/idea-chat.service';

@Component({
  standalone: true,
  templateUrl: './idea-chat-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class IdeaChatDeleteDialogComponent {
  ideaChat?: IIdeaChat;

  protected ideaChatService = inject(IdeaChatService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ideaChatService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
