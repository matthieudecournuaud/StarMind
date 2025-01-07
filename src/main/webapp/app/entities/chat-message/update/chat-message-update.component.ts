import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IGlobalChat } from 'app/entities/global-chat/global-chat.model';
import { GlobalChatService } from 'app/entities/global-chat/service/global-chat.service';
import { IIdeaChat } from 'app/entities/idea-chat/idea-chat.model';
import { IdeaChatService } from 'app/entities/idea-chat/service/idea-chat.service';
import { ChatMessageService } from '../service/chat-message.service';
import { IChatMessage } from '../chat-message.model';
import { ChatMessageFormGroup, ChatMessageFormService } from './chat-message-form.service';

@Component({
  standalone: true,
  selector: 'jhi-chat-message-update',
  templateUrl: './chat-message-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ChatMessageUpdateComponent implements OnInit {
  isSaving = false;
  chatMessage: IChatMessage | null = null;

  usersSharedCollection: IUser[] = [];
  globalChatsSharedCollection: IGlobalChat[] = [];
  ideaChatsSharedCollection: IIdeaChat[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected chatMessageService = inject(ChatMessageService);
  protected chatMessageFormService = inject(ChatMessageFormService);
  protected userService = inject(UserService);
  protected globalChatService = inject(GlobalChatService);
  protected ideaChatService = inject(IdeaChatService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ChatMessageFormGroup = this.chatMessageFormService.createChatMessageFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareGlobalChat = (o1: IGlobalChat | null, o2: IGlobalChat | null): boolean => this.globalChatService.compareGlobalChat(o1, o2);

  compareIdeaChat = (o1: IIdeaChat | null, o2: IIdeaChat | null): boolean => this.ideaChatService.compareIdeaChat(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chatMessage }) => {
      this.chatMessage = chatMessage;
      if (chatMessage) {
        this.updateForm(chatMessage);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('starMindApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chatMessage = this.chatMessageFormService.getChatMessage(this.editForm);
    if (chatMessage.id !== null) {
      this.subscribeToSaveResponse(this.chatMessageService.update(chatMessage));
    } else {
      this.subscribeToSaveResponse(this.chatMessageService.create(chatMessage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChatMessage>>): void {
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

  protected updateForm(chatMessage: IChatMessage): void {
    this.chatMessage = chatMessage;
    this.chatMessageFormService.resetForm(this.editForm, chatMessage);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, chatMessage.author);
    this.globalChatsSharedCollection = this.globalChatService.addGlobalChatToCollectionIfMissing<IGlobalChat>(
      this.globalChatsSharedCollection,
      chatMessage.globalChat,
    );
    this.ideaChatsSharedCollection = this.ideaChatService.addIdeaChatToCollectionIfMissing<IIdeaChat>(
      this.ideaChatsSharedCollection,
      chatMessage.ideaChat,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.chatMessage?.author)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.globalChatService
      .query()
      .pipe(map((res: HttpResponse<IGlobalChat[]>) => res.body ?? []))
      .pipe(
        map((globalChats: IGlobalChat[]) =>
          this.globalChatService.addGlobalChatToCollectionIfMissing<IGlobalChat>(globalChats, this.chatMessage?.globalChat),
        ),
      )
      .subscribe((globalChats: IGlobalChat[]) => (this.globalChatsSharedCollection = globalChats));

    this.ideaChatService
      .query()
      .pipe(map((res: HttpResponse<IIdeaChat[]>) => res.body ?? []))
      .pipe(
        map((ideaChats: IIdeaChat[]) =>
          this.ideaChatService.addIdeaChatToCollectionIfMissing<IIdeaChat>(ideaChats, this.chatMessage?.ideaChat),
        ),
      )
      .subscribe((ideaChats: IIdeaChat[]) => (this.ideaChatsSharedCollection = ideaChats));
  }
}
