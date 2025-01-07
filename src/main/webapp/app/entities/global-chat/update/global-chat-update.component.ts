import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IGlobalChat } from '../global-chat.model';
import { GlobalChatService } from '../service/global-chat.service';
import { GlobalChatFormGroup, GlobalChatFormService } from './global-chat-form.service';

@Component({
  standalone: true,
  selector: 'jhi-global-chat-update',
  templateUrl: './global-chat-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GlobalChatUpdateComponent implements OnInit {
  isSaving = false;
  globalChat: IGlobalChat | null = null;

  usersSharedCollection: IUser[] = [];

  protected globalChatService = inject(GlobalChatService);
  protected globalChatFormService = inject(GlobalChatFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GlobalChatFormGroup = this.globalChatFormService.createGlobalChatFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ globalChat }) => {
      this.globalChat = globalChat;
      if (globalChat) {
        this.updateForm(globalChat);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const globalChat = this.globalChatFormService.getGlobalChat(this.editForm);
    if (globalChat.id !== null) {
      this.subscribeToSaveResponse(this.globalChatService.update(globalChat));
    } else {
      this.subscribeToSaveResponse(this.globalChatService.create(globalChat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGlobalChat>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(globalChat: IGlobalChat): void {
    this.globalChat = globalChat;
    this.globalChatFormService.resetForm(this.editForm, globalChat);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      ...(globalChat.users ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, ...(this.globalChat?.users ?? []))))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
