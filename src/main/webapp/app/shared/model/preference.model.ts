import {ICorrection} from 'app/shared/model/correction.model';
import {IComment} from 'app/shared/model/comment.model';

export interface IPreference {
  id?: number;
  avatarContentType?: string;
  avatar?: any;
  userId?: number;
  correctionsInProgresses?: ICorrection[];
  resolvedCorrections?: ICorrection[];
  comments?: IComment[];
}

export const defaultValue: Readonly<IPreference> = {};
