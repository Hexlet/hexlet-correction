import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './comment.reducer';
import { IComment } from 'app/shared/model/comment.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICommentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CommentDetail extends React.Component<ICommentDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { commentEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="hexletCorrectionApp.comment.detail.title">Comment</Translate> [<b>{commentEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="message">
                <Translate contentKey="hexletCorrectionApp.comment.message">Message</Translate>
              </span>
            </dt>
            <dd>{commentEntity.message}</dd>
            <dt>
              <span id="date">
                <Translate contentKey="hexletCorrectionApp.comment.date">Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={commentEntity.date} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="hexletCorrectionApp.comment.correction">Correction</Translate>
            </dt>
            <dd>{commentEntity.correctionId ? commentEntity.correctionId : ''}</dd>
            <dt>
              <Translate contentKey="hexletCorrectionApp.comment.correcter">Correcter</Translate>
            </dt>
            <dd>{commentEntity.correcterId ? commentEntity.correcterId : ''}</dd>
          </dl>
          <Button tag={Link} to="/comment" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/comment/${commentEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ comment }: IRootState) => ({
  commentEntity: comment.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CommentDetail);
