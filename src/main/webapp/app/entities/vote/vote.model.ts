import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IIdea } from 'app/entities/idea/idea.model';
import { VoteOption } from 'app/entities/enumerations/vote-option.model';

export interface IVote {
  id: number;
  voteOption?: keyof typeof VoteOption | null;
  comment?: string | null;
  createdDate?: dayjs.Dayjs | null;
  voter?: Pick<IUser, 'id'> | null;
  idea?: Pick<IIdea, 'id'> | null;
}

export type NewVote = Omit<IVote, 'id'> & { id: null };
