import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IGlobalChat, NewGlobalChat } from '../global-chat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGlobalChat for edit and NewGlobalChatFormGroupInput for create.
 */
type GlobalChatFormGroupInput = IGlobalChat | PartialWithRequiredKeyOf<NewGlobalChat>;

type GlobalChatFormDefaults = Pick<NewGlobalChat, 'id' | 'users'>;

type GlobalChatFormGroupContent = {
  id: FormControl<IGlobalChat['id'] | NewGlobalChat['id']>;
  name: FormControl<IGlobalChat['name']>;
  users: FormControl<IGlobalChat['users']>;
};

export type GlobalChatFormGroup = FormGroup<GlobalChatFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GlobalChatFormService {
  createGlobalChatFormGroup(globalChat: GlobalChatFormGroupInput = { id: null }): GlobalChatFormGroup {
    const globalChatRawValue = {
      ...this.getFormDefaults(),
      ...globalChat,
    };
    return new FormGroup<GlobalChatFormGroupContent>({
      id: new FormControl(
        { value: globalChatRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(globalChatRawValue.name, {
        validators: [Validators.required],
      }),
      users: new FormControl(globalChatRawValue.users ?? []),
    });
  }

  getGlobalChat(form: GlobalChatFormGroup): IGlobalChat | NewGlobalChat {
    return form.getRawValue() as IGlobalChat | NewGlobalChat;
  }

  resetForm(form: GlobalChatFormGroup, globalChat: GlobalChatFormGroupInput): void {
    const globalChatRawValue = { ...this.getFormDefaults(), ...globalChat };
    form.reset(
      {
        ...globalChatRawValue,
        id: { value: globalChatRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): GlobalChatFormDefaults {
    return {
      id: null,
      users: [],
    };
  }
}
