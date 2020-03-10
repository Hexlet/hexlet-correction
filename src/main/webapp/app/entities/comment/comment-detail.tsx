import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {TextFormat, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {IRootState} from 'app/shared/reducers';
import {getEntity} from './comment.reducer';
import {APP_DATE_FORMAT} from 'app/config/constants';

export interface ICommentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {
}

export const CommentDetail = (props: ICommentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const {commentEntity} = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate
            contentKey="hexletCorrectionApp.comment.detail.title">Comment</Translate> [<b>{commentEntity.id}</b>]
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
            <Translate contentKey="hexletCorrectionApp.comment.author">Author</Translate>
          </dt>
          <dd>{commentEntity.authorId ? commentEntity.authorId : ''}</dd>
          <dt>
            <Translate contentKey="hexletCorrectionApp.comment.correction">Correction</Translate>
          </dt>
          <dd>{commentEntity.correctionId ? commentEntity.correctionId : ''}</dd>
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
};

const mapStateToProps = ({comment}: IRootState) => ({
  commentEntity: comment.entity
});

const mapDispatchToProps = {getEntity};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CommentDetail);
