import dayjs from 'dayjs/esm';
import { IIdea } from 'app/entities/idea/idea.model';

export interface ILikeHistory {
  id: number;
  action?: string | null;
  actionDate?: dayjs.Dayjs | null;
  oldLikes?: number | null;
  newLikes?: number | null;
  idea?: Pick<IIdea, 'id'> | null;
}

export type NewLikeHistory = Omit<ILikeHistory, 'id'> & { id: null };
