import dayjs from 'dayjs/esm';
import { IIdea } from 'app/entities/idea/idea.model';
import { RewardType } from 'app/entities/enumerations/reward-type.model';

export interface IIdeaHistory {
  id: number;
  action?: string | null;
  actionDate?: dayjs.Dayjs | null;
  description?: string | null;
  rewardType?: keyof typeof RewardType | null;
  likes?: number | null;
  idea?: Pick<IIdea, 'id'> | null;
}

export type NewIdeaHistory = Omit<IIdeaHistory, 'id'> & { id: null };
