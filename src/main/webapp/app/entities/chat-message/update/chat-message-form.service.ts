import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IChatMessage, NewChatMessage } from '../chat-message.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IChatMessage for edit and NewChatMessageFormGroupInput for create.
 */
type ChatMessageFormGroupInput = IChatMessage | PartialWithRequiredKeyOf<NewChatMessage>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IChatMessage | NewChatMessage> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type ChatMessageFormRawValue = FormValueOf<IChatMessage>;

type NewChatMessageFormRawValue = FormValueOf<NewChatMessage>;

type ChatMessageFormDefaults = Pick<NewChatMessage, 'id' | 'createdDate'>;

type ChatMessageFormGroupContent = {
  id: FormControl<ChatMessageFormRawValue['id'] | NewChatMessage['id']>;
  message: FormControl<ChatMessageFormRawValue['message']>;
  createdDate: FormControl<ChatMessageFormRawValue['createdDate']>;
  likeCount: FormControl<ChatMessageFormRawValue['likeCount']>;
  author: FormControl<ChatMessageFormRawValue['author']>;
  globalChat: FormControl<ChatMessageFormRawValue['globalChat']>;
  ideaChat: FormControl<ChatMessageFormRawValue['ideaChat']>;
};

export type ChatMessageFormGroup = FormGroup<ChatMessageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChatMessageFormService {
  createChatMessageFormGroup(chatMessage: ChatMessageFormGroupInput = { id: null }): ChatMessageFormGroup {
    const chatMessageRawValue = this.convertChatMessageToChatMessageRawValue({
      ...this.getFormDefaults(),
      ...chatMessage,
    });
    return new FormGroup<ChatMessageFormGroupContent>({
      id: new FormControl(
        { value: chatMessageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      message: new FormControl(chatMessageRawValue.message, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(chatMessageRawValue.createdDate, {
        validators: [Validators.required],
      }),
      likeCount: new FormControl(chatMessageRawValue.likeCount),
      author: new FormControl(chatMessageRawValue.author),
      globalChat: new FormControl(chatMessageRawValue.globalChat),
      ideaChat: new FormControl(chatMessageRawValue.ideaChat),
    });
  }

  getChatMessage(form: ChatMessageFormGroup): IChatMessage | NewChatMessage {
    return this.convertChatMessageRawValueToChatMessage(form.getRawValue() as ChatMessageFormRawValue | NewChatMessageFormRawValue);
  }

  resetForm(form: ChatMessageFormGroup, chatMessage: ChatMessageFormGroupInput): void {
    const chatMessageRawValue = this.convertChatMessageToChatMessageRawValue({ ...this.getFormDefaults(), ...chatMessage });
    form.reset(
      {
        ...chatMessageRawValue,
        id: { value: chatMessageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ChatMessageFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertChatMessageRawValueToChatMessage(
    rawChatMessage: ChatMessageFormRawValue | NewChatMessageFormRawValue,
  ): IChatMessage | NewChatMessage {
    return {
      ...rawChatMessage,
      createdDate: dayjs(rawChatMessage.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertChatMessageToChatMessageRawValue(
    chatMessage: IChatMessage | (Partial<NewChatMessage> & ChatMessageFormDefaults),
  ): ChatMessageFormRawValue | PartialWithRequiredKeyOf<NewChatMessageFormRawValue> {
    return {
      ...chatMessage,
      createdDate: chatMessage.createdDate ? chatMessage.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
