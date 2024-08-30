import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { ICategory } from 'app/entities/category/category.model';
import { IReward } from 'app/entities/reward/reward.model';
import { IdeaStatus } from 'app/entities/enumerations/idea-status.model';
import { RewardType } from 'app/entities/enumerations/reward-type.model';

export interface IIdea {
  id: number;
  title?: string | null;
  description?: string | null;
  status?: keyof typeof IdeaStatus | null;
  validation?: boolean | null;
  rewardType?: keyof typeof RewardType | null;
  likes?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  author?: Pick<IUser, 'id'> | null;
  ideaCategory?: ICategory | null;
  assignedReward?: IReward | null;
  category?: ICategory | null;
  reward?: IReward | null;
}

export type NewIdea = Omit<IIdea, 'id'> & { id: null };
