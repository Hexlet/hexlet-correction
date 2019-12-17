import { ICorrection } from 'app/shared/model/correction.model';
import { IComment } from 'app/shared/model/comment.model';
import { CorrecterStatus } from 'app/shared/model/enumerations/correcter-status.model';

export interface ICorrecter {
  id?: number;
  firstName?: string;
  lastName?: string;
  status?: CorrecterStatus;
  email?: string;
  password?: string;
  phone?: string;
  avatarContentType?: string;
  avatar?: any;
  userLogin?: string;
  userId?: number;
  correctionsInProgresses?: ICorrection[];
  correctionsResolveds?: ICorrection[];
  comments?: IComment[];
}

export const defaultValue: Readonly<ICorrecter> = {};
