import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IGlobalChat } from '../global-chat.model';
import { GlobalChatService } from '../service/global-chat.service';

@Component({
  standalone: true,
  templateUrl: './global-chat-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class GlobalChatDeleteDialogComponent {
  globalChat?: IGlobalChat;

  protected globalChatService = inject(GlobalChatService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.globalChatService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
