import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IProspectBoard } from 'app/entities/prospect-board/prospect-board.model';
import { ProspectStatus } from 'app/entities/enumerations/prospect-status.model';

export interface IProspectEntry {
  id: number;
  status?: keyof typeof ProspectStatus | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  prospect?: Pick<IUser, 'id'> | null;
  prospectBoard?: Pick<IProspectBoard, 'id'> | null;
}

export type NewProspectEntry = Omit<IProspectEntry, 'id'> & { id: null };
