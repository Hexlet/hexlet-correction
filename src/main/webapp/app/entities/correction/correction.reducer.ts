import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICorrection, defaultValue } from 'app/shared/model/correction.model';

export const ACTION_TYPES = {
  FETCH_CORRECTION_LIST: 'correction/FETCH_CORRECTION_LIST',
  FETCH_CORRECTION: 'correction/FETCH_CORRECTION',
  CREATE_CORRECTION: 'correction/CREATE_CORRECTION',
  UPDATE_CORRECTION: 'correction/UPDATE_CORRECTION',
  DELETE_CORRECTION: 'correction/DELETE_CORRECTION',
  RESET: 'correction/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICorrection>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CorrectionState = Readonly<typeof initialState>;

// Reducer

export default (state: CorrectionState = initialState, action): CorrectionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CORRECTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CORRECTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CORRECTION):
    case REQUEST(ACTION_TYPES.UPDATE_CORRECTION):
    case REQUEST(ACTION_TYPES.DELETE_CORRECTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CORRECTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CORRECTION):
    case FAILURE(ACTION_TYPES.CREATE_CORRECTION):
    case FAILURE(ACTION_TYPES.UPDATE_CORRECTION):
    case FAILURE(ACTION_TYPES.DELETE_CORRECTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CORRECTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_CORRECTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CORRECTION):
    case SUCCESS(ACTION_TYPES.UPDATE_CORRECTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CORRECTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/corrections';

// Actions

export const getEntities: ICrudGetAllAction<ICorrection> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CORRECTION_LIST,
    payload: axios.get<ICorrection>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICorrection> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CORRECTION,
    payload: axios.get<ICorrection>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICorrection> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CORRECTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICorrection> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CORRECTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICorrection> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CORRECTION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
