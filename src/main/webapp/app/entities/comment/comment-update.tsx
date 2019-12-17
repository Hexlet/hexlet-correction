import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICorrection } from 'app/shared/model/correction.model';
import { getEntities as getCorrections } from 'app/entities/correction/correction.reducer';
import { ICorrecter } from 'app/shared/model/correcter.model';
import { getEntities as getCorrecters } from 'app/entities/correcter/correcter.reducer';
import { getEntity, updateEntity, createEntity, reset } from './comment.reducer';
import { IComment } from 'app/shared/model/comment.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICommentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICommentUpdateState {
  isNew: boolean;
  correctionId: string;
  correcterId: string;
}

export class CommentUpdate extends React.Component<ICommentUpdateProps, ICommentUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      correctionId: '0',
      correcterId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getCorrections();
    this.props.getCorrecters();
  }

  saveEntity = (event, errors, values) => {
    values.date = convertDateTimeToServer(values.date);

    if (errors.length === 0) {
      const { commentEntity } = this.props;
      const entity = {
        ...commentEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/comment');
  };

  render() {
    const { commentEntity, corrections, correcters, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="hexletCorrectionApp.comment.home.createOrEditLabel">
              <Translate contentKey="hexletCorrectionApp.comment.home.createOrEditLabel">Create or edit a Comment</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : commentEntity} onSubmit={this.saveEntity}>
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
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      minLength: { value: 1, errorMessage: translate('entity.validation.minlength', { min: 1 }) },
                      maxLength: { value: 200, errorMessage: translate('entity.validation.maxlength', { max: 200 }) }
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
                    value={isNew ? null : convertDateTimeFromServer(this.props.commentEntity.date)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
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
                <AvGroup>
                  <Label for="comment-correcter">
                    <Translate contentKey="hexletCorrectionApp.comment.correcter">Correcter</Translate>
                  </Label>
                  <AvInput id="comment-correcter" type="select" className="form-control" name="correcterId">
                    <option value="" key="0" />
                    {correcters
                      ? correcters.map(otherEntity => (
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
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  corrections: storeState.correction.entities,
  correcters: storeState.correcter.entities,
  commentEntity: storeState.comment.entity,
  loading: storeState.comment.loading,
  updating: storeState.comment.updating,
  updateSuccess: storeState.comment.updateSuccess
});

const mapDispatchToProps = {
  getCorrections,
  getCorrecters,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CommentUpdate);
