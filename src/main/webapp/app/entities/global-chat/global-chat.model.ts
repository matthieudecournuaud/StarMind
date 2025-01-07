import { IUser } from 'app/entities/user/user.model';

export interface IGlobalChat {
  id: number;
  name?: string | null;
  users?: Pick<IUser, 'id'>[] | null;
}

export type NewGlobalChat = Omit<IGlobalChat, 'id'> & { id: null };
