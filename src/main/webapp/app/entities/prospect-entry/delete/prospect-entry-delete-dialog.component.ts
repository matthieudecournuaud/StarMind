import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProspectEntry } from '../prospect-entry.model';
import { ProspectEntryService } from '../service/prospect-entry.service';

@Component({
  standalone: true,
  templateUrl: './prospect-entry-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProspectEntryDeleteDialogComponent {
  prospectEntry?: IProspectEntry;

  protected prospectEntryService = inject(ProspectEntryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prospectEntryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
