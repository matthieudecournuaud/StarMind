import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IReward } from 'app/entities/reward/reward.model';
import { ICategory } from 'app/entities/category/category.model';
import { IdeaStatus } from 'app/entities/enumerations/idea-status.model';
import { RewardType } from 'app/entities/enumerations/reward-type.model';

export interface IIdea {
  id: number;
  title?: string | null;
  description?: string | null;
  status?: keyof typeof IdeaStatus | null;
  isConfidential?: boolean | null;
  validation?: boolean | null;
  rewardType?: keyof typeof RewardType | null;
  likes?: number | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  isPublic?: boolean | null;
  impact?: string | null;
  author?: Pick<IUser, 'id'> | null;
  assignedReward?: Pick<IReward, 'id'> | null;
  manager?: Pick<IUser, 'id'> | null;
  category?: Pick<ICategory, 'id'> | null;
}

export type NewIdea = Omit<IIdea, 'id'> & { id: null };
