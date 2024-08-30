import dayjs from 'dayjs/esm';
import { IIdea } from 'app/entities/idea/idea.model';

export interface ILikeHistory {
  id: number;
  action?: string | null;
  actionDate?: dayjs.Dayjs | null;
  oldLikes?: string | null;
  newLikes?: string | null;
  idea?: IIdea | null;
}

export type NewLikeHistory = Omit<ILikeHistory, 'id'> & { id: null };
