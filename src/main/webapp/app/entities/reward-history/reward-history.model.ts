import dayjs from 'dayjs/esm';
import { IIdea } from 'app/entities/idea/idea.model';
import { IReward } from 'app/entities/reward/reward.model';

export interface IRewardHistory {
  id: number;
  action?: string | null;
  actionDate?: dayjs.Dayjs | null;
  description?: string | null;
  idea?: Pick<IIdea, 'id'> | null;
  reward?: Pick<IReward, 'id'> | null;
}

export type NewRewardHistory = Omit<IRewardHistory, 'id'> & { id: null };
