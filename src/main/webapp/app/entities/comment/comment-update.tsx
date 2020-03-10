import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Col, Label, Row} from 'reactstrap';
import {AvField, AvForm, AvGroup, AvInput} from 'availity-reactstrap-validation';
import {Translate, translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {IRootState} from 'app/shared/reducers';
import {getEntities as getPreferences} from 'app/entities/preference/preference.reducer';
import {getEntities as getCorrections} from 'app/entities/correction/correction.reducer';
import {createEntity, getEntity, reset, updateEntity} from './comment.reducer';
import {convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime} from 'app/shared/util/date-utils';

export interface ICommentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {
}

export const CommentUpdate = (props: ICommentUpdateProps) => {
  const [authorId, setAuthorId] = useState('0');
  const [correctionId, setCorrectionId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const {commentEntity, preferences, corrections, loading, updating} = props;

  const handleClose = () => {
    props.history.push('/comment' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getPreferences();
    props.getCorrections();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.date = convertDateTimeToServer(values.date);

    if (errors.length === 0) {
      const entity = {
        ...commentEntity,
        ...values
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hexletCorrectionApp.comment.home.createOrEditLabel">
            <Translate contentKey="hexletCorrectionApp.comment.home.createOrEditLabel">Create or edit a
              Comment</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : commentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="comment-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="comment-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="messageLabel" for="comment-message">
                  <Translate contentKey="hexletCorrectionApp.comment.message">Message</Translate>
                </Label>
                <AvField
                  id="comment-message"
                  type="text"
                  name="message"
                  validate={{
                    required: {value: true, errorMessage: translate('entity.validation.required')},
                    maxLength: {value: 200, errorMessage: translate('entity.validation.maxlength', {max: 200})}
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="dateLabel" for="comment-date">
                  <Translate contentKey="hexletCorrectionApp.comment.date">Date</Translate>
                </Label>
                <AvInput
                  id="comment-date"
                  type="datetime-local"
                  className="form-control"
                  name="date"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.commentEntity.date)}
                  validate={{
                    required: {value: true, errorMessage: translate('entity.validation.required')}
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="comment-author">
                  <Translate contentKey="hexletCorrectionApp.comment.author">Author</Translate>
                </Label>
                <AvInput id="comment-author" type="select" className="form-control" name="authorId">
                  <option value="" key="0" />
                  {preferences
                    ? preferences.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="comment-correction">
                  <Translate contentKey="hexletCorrectionApp.comment.correction">Correction</Translate>
                </Label>
                <AvInput id="comment-correction" type="select" className="form-control" name="correctionId">
                  <option value="" key="0" />
                  {corrections
                    ? corrections.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/comment" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  preferences: storeState.preference.entities,
  corrections: storeState.correction.entities,
  commentEntity: storeState.comment.entity,
  loading: storeState.comment.loading,
  updating: storeState.comment.updating,
  updateSuccess: storeState.comment.updateSuccess
});

const mapDispatchToProps = {
  getPreferences,
  getCorrections,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CommentUpdate);
