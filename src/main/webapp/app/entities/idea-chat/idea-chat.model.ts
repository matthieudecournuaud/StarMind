import { IUser } from 'app/entities/user/user.model';

export interface IIdeaChat {
  id: number;
  name?: string | null;
  users?: Pick<IUser, 'id'>[] | null;
}

export type NewIdeaChat = Omit<IIdeaChat, 'id'> & { id: null };
