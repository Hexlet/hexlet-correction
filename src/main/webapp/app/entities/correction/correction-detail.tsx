import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './correction.reducer';
import { ICorrection } from 'app/shared/model/correction.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICorrectionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CorrectionDetail extends React.Component<ICorrectionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { correctionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="hexletCorrectionApp.correction.detail.title">Correction</Translate> [<b>{correctionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="reporterRemark">
                <Translate contentKey="hexletCorrectionApp.correction.reporterRemark">Reporter Remark</Translate>
              </span>
            </dt>
            <dd>{correctionEntity.reporterRemark}</dd>
            <dt>
              <span id="correcterRemark">
                <Translate contentKey="hexletCorrectionApp.correction.correcterRemark">Correcter Remark</Translate>
              </span>
            </dt>
            <dd>{correctionEntity.correcterRemark}</dd>
            <dt>
              <span id="resolverRemark">
                <Translate contentKey="hexletCorrectionApp.correction.resolverRemark">Resolver Remark</Translate>
              </span>
            </dt>
            <dd>{correctionEntity.resolverRemark}</dd>
            <dt>
              <span id="textBeforeCorrection">
                <Translate contentKey="hexletCorrectionApp.correction.textBeforeCorrection">Text Before Correction</Translate>
              </span>
            </dt>
            <dd>{correctionEntity.textBeforeCorrection}</dd>
            <dt>
              <span id="textCorrection">
                <Translate contentKey="hexletCorrectionApp.correction.textCorrection">Text Correction</Translate>
              </span>
            </dt>
            <dd>{correctionEntity.textCorrection}</dd>
            <dt>
              <span id="textAfterCorrection">
                <Translate contentKey="hexletCorrectionApp.correction.textAfterCorrection">Text After Correction</Translate>
              </span>
            </dt>
            <dd>{correctionEntity.textAfterCorrection}</dd>
            <dt>
              <span id="reporterName">
                <Translate contentKey="hexletCorrectionApp.correction.reporterName">Reporter Name</Translate>
              </span>
            </dt>
            <dd>{correctionEntity.reporterName}</dd>
            <dt>
              <span id="pageURL">
                <Translate contentKey="hexletCorrectionApp.correction.pageURL">Page URL</Translate>
              </span>
            </dt>
            <dd>{correctionEntity.pageURL}</dd>
            <dt>
              <span id="correctionStatus">
                <Translate contentKey="hexletCorrectionApp.correction.correctionStatus">Correction Status</Translate>
              </span>
            </dt>
            <dd>{correctionEntity.correctionStatus}</dd>
            <dt>
              <Translate contentKey="hexletCorrectionApp.correction.correcter">Correcter</Translate>
            </dt>
            <dd>{correctionEntity.correcterId ? correctionEntity.correcterId : ''}</dd>
            <dt>
              <Translate contentKey="hexletCorrectionApp.correction.resolver">Resolver</Translate>
            </dt>
            <dd>{correctionEntity.resolverId ? correctionEntity.resolverId : ''}</dd>
          </dl>
          <Button tag={Link} to="/correction" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/correction/${correctionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ correction }: IRootState) => ({
  correctionEntity: correction.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CorrectionDetail);
