import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICorrecter } from 'app/shared/model/correcter.model';
import { getEntities as getCorrecters } from 'app/entities/correcter/correcter.reducer';
import { getEntity, updateEntity, createEntity, reset } from './correction.reducer';
import { ICorrection } from 'app/shared/model/correction.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICorrectionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICorrectionUpdateState {
  isNew: boolean;
  correcterId: string;
  resolverId: string;
}

export class CorrectionUpdate extends React.Component<ICorrectionUpdateProps, ICorrectionUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      correcterId: '0',
      resolverId: '0',
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

    this.props.getCorrecters();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { correctionEntity } = this.props;
      const entity = {
        ...correctionEntity,
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
    this.props.history.push('/correction');
  };

  render() {
    const { correctionEntity, correcters, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="hexletCorrectionApp.correction.home.createOrEditLabel">
              <Translate contentKey="hexletCorrectionApp.correction.home.createOrEditLabel">Create or edit a Correction</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : correctionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="correction-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="correction-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="reporterRemarkLabel" for="correction-reporterRemark">
                    <Translate contentKey="hexletCorrectionApp.correction.reporterRemark">Reporter Remark</Translate>
                  </Label>
                  <AvField
                    id="correction-reporterRemark"
                    type="text"
                    name="reporterRemark"
                    validate={{
                      maxLength: { value: 200, errorMessage: translate('entity.validation.maxlength', { max: 200 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="correcterRemarkLabel" for="correction-correcterRemark">
                    <Translate contentKey="hexletCorrectionApp.correction.correcterRemark">Correcter Remark</Translate>
                  </Label>
                  <AvField
                    id="correction-correcterRemark"
                    type="text"
                    name="correcterRemark"
                    validate={{
                      maxLength: { value: 200, errorMessage: translate('entity.validation.maxlength', { max: 200 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="resolverRemarkLabel" for="correction-resolverRemark">
                    <Translate contentKey="hexletCorrectionApp.correction.resolverRemark">Resolver Remark</Translate>
                  </Label>
                  <AvField
                    id="correction-resolverRemark"
                    type="text"
                    name="resolverRemark"
                    validate={{
                      maxLength: { value: 200, errorMessage: translate('entity.validation.maxlength', { max: 200 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="textBeforeCorrectionLabel" for="correction-textBeforeCorrection">
                    <Translate contentKey="hexletCorrectionApp.correction.textBeforeCorrection">Text Before Correction</Translate>
                  </Label>
                  <AvField
                    id="correction-textBeforeCorrection"
                    type="text"
                    name="textBeforeCorrection"
                    validate={{
                      maxLength: { value: 1000, errorMessage: translate('entity.validation.maxlength', { max: 1000 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="textCorrectionLabel" for="correction-textCorrection">
                    <Translate contentKey="hexletCorrectionApp.correction.textCorrection">Text Correction</Translate>
                  </Label>
                  <AvField
                    id="correction-textCorrection"
                    type="text"
                    name="textCorrection"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      minLength: { value: 1, errorMessage: translate('entity.validation.minlength', { min: 1 }) },
                      maxLength: { value: 1000, errorMessage: translate('entity.validation.maxlength', { max: 1000 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="textAfterCorrectionLabel" for="correction-textAfterCorrection">
                    <Translate contentKey="hexletCorrectionApp.correction.textAfterCorrection">Text After Correction</Translate>
                  </Label>
                  <AvField
                    id="correction-textAfterCorrection"
                    type="text"
                    name="textAfterCorrection"
                    validate={{
                      maxLength: { value: 1000, errorMessage: translate('entity.validation.maxlength', { max: 1000 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="reporterNameLabel" for="correction-reporterName">
                    <Translate contentKey="hexletCorrectionApp.correction.reporterName">Reporter Name</Translate>
                  </Label>
                  <AvField
                    id="correction-reporterName"
                    type="text"
                    name="reporterName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      minLength: { value: 1, errorMessage: translate('entity.validation.minlength', { min: 1 }) },
                      maxLength: { value: 50, errorMessage: translate('entity.validation.maxlength', { max: 50 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="pageURLLabel" for="correction-pageURL">
                    <Translate contentKey="hexletCorrectionApp.correction.pageURL">Page URL</Translate>
                  </Label>
                  <AvField
                    id="correction-pageURL"
                    type="text"
                    name="pageURL"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 50, errorMessage: translate('entity.validation.maxlength', { max: 50 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="correctionStatusLabel" for="correction-correctionStatus">
                    <Translate contentKey="hexletCorrectionApp.correction.correctionStatus">Correction Status</Translate>
                  </Label>
                  <AvInput
                    id="correction-correctionStatus"
                    type="select"
                    className="form-control"
                    name="correctionStatus"
                    value={(!isNew && correctionEntity.correctionStatus) || 'REPORTED'}
                  >
                    <option value="REPORTED">{translate('hexletCorrectionApp.CorrectionStatus.REPORTED')}</option>
                    <option value="IN_PROGRESS">{translate('hexletCorrectionApp.CorrectionStatus.IN_PROGRESS')}</option>
                    <option value="RESOLVED">{translate('hexletCorrectionApp.CorrectionStatus.RESOLVED')}</option>
                    <option value="CANCELED">{translate('hexletCorrectionApp.CorrectionStatus.CANCELED')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="correction-correcter">
                    <Translate contentKey="hexletCorrectionApp.correction.correcter">Correcter</Translate>
                  </Label>
                  <AvInput id="correction-correcter" type="select" className="form-control" name="correcterId">
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
                <AvGroup>
                  <Label for="correction-resolver">
                    <Translate contentKey="hexletCorrectionApp.correction.resolver">Resolver</Translate>
                  </Label>
                  <AvInput id="correction-resolver" type="select" className="form-control" name="resolverId">
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
                <Button tag={Link} id="cancel-save" to="/correction" replace color="info">
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
  correcters: storeState.correcter.entities,
  correctionEntity: storeState.correction.entity,
  loading: storeState.correction.loading,
  updating: storeState.correction.updating,
  updateSuccess: storeState.correction.updateSuccess
});

const mapDispatchToProps = {
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
)(CorrectionUpdate);
