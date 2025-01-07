import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IProspectBoard {
  id: number;
  name?: string | null;
  description?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  owner?: Pick<IUser, 'id'> | null;
}

export type NewProspectBoard = Omit<IProspectBoard, 'id'> & { id: null };
