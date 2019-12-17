import { IComment } from 'app/shared/model/comment.model';
import { CorrectionStatus } from 'app/shared/model/enumerations/correction-status.model';

export interface ICorrection {
  id?: number;
  reporterRemark?: string;
  correcterRemark?: string;
  resolverRemark?: string;
  textBeforeCorrection?: string;
  textCorrection?: string;
  textAfterCorrection?: string;
  reporterName?: string;
  pageURL?: string;
  correctionStatus?: CorrectionStatus;
  comments?: IComment[];
  correcterId?: number;
  resolverId?: number;
}

export const defaultValue: Readonly<ICorrection> = {};
