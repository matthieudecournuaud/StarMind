import dayjs from 'dayjs/esm';
import { IReward } from 'app/entities/reward/reward.model';
import { IIdea } from 'app/entities/idea/idea.model';

export interface IRewardHistory {
  id: number;
  action?: string | null;
  actionDate?: dayjs.Dayjs | null;
  description?: string | null;
  reward?: IReward | null;
  idea?: IIdea | null;
}

export type NewRewardHistory = Omit<IRewardHistory, 'id'> & { id: null };
