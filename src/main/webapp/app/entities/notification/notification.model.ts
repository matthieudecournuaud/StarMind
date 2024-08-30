import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface INotification {
  id: number;
  message?: string | null;
  type?: string | null;
  sentDate?: dayjs.Dayjs | null;
  read?: boolean | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };
