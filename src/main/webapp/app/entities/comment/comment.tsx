import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Row, Table} from 'reactstrap';
import {getSortState, JhiItemCount, JhiPagination, TextFormat, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {IRootState} from 'app/shared/reducers';
import {getEntities} from './comment.reducer';
import {APP_DATE_FORMAT} from 'app/config/constants';
import {ITEMS_PER_PAGE} from 'app/shared/util/pagination.constants';

export interface ICommentProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {
}

export const Comment = (props: ICommentProps) => {
  const [paginationState, setPaginationState] = useState(getSortState(props.location, ITEMS_PER_PAGE));

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
  };

  const sortEntities = () => {
    getAllEntities();
    props.history.push(
      `${props.location.pathname}?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`
    );
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage
    });

  const {commentList, match, loading, totalItems} = props;
  return (
    <div>
      <h2 id="comment-heading">
        <Translate contentKey="hexletCorrectionApp.comment.home.title">Comments</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="hexletCorrectionApp.comment.home.createLabel">Create new Comment</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {commentList && commentList.length > 0 ? (
          <Table responsive>
            <thead>
            <tr>
              <th className="hand" onClick={sort('id')}>
                <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={sort('message')}>
                <Translate contentKey="hexletCorrectionApp.comment.message">Message</Translate> <FontAwesomeIcon
                icon="sort" />
              </th>
              <th className="hand" onClick={sort('date')}>
                <Translate contentKey="hexletCorrectionApp.comment.date">Date</Translate> <FontAwesomeIcon
                icon="sort" />
              </th>
              <th>
                <Translate contentKey="hexletCorrectionApp.comment.author">Author</Translate> <FontAwesomeIcon
                icon="sort" />
              </th>
              <th>
                <Translate contentKey="hexletCorrectionApp.comment.correction">Correction</Translate> <FontAwesomeIcon
                icon="sort" />
              </th>
              <th />
            </tr>
            </thead>
            <tbody>
            {commentList.map((comment, i) => (
              <tr key={`entity-${i}`}>
                <td>
                  <Button tag={Link} to={`${match.url}/${comment.id}`} color="link" size="sm">
                    {comment.id}
                  </Button>
                </td>
                <td>{comment.message}</td>
                <td>
                  <TextFormat type="date" value={comment.date} format={APP_DATE_FORMAT} />
                </td>
                <td>{comment.authorId ? <Link to={`preference/${comment.authorId}`}>{comment.authorId}</Link> : ''}</td>
                <td>{comment.correctionId ?
                  <Link to={`correction/${comment.correctionId}`}>{comment.correctionId}</Link> : ''}</td>
                <td className="text-right">
                  <div className="btn-group flex-btn-group-container">
                    <Button tag={Link} to={`${match.url}/${comment.id}`} color="info" size="sm">
                      <FontAwesomeIcon icon="eye" />{' '}
                      <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                    </Button>
                    <Button
                      tag={Link}
                      to={`${match.url}/${comment.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                      color="primary"
                      size="sm"
                    >
                      <FontAwesomeIcon icon="pencil-alt" />{' '}
                      <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                    </Button>
                    <Button
                      tag={Link}
                      to={`${match.url}/${comment.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                      color="danger"
                      size="sm"
                    >
                      <FontAwesomeIcon icon="trash" />{' '}
                      <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                    </Button>
                  </div>
                </td>
              </tr>
            ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="hexletCorrectionApp.comment.home.notFound">No Comments found</Translate>
            </div>
          )
        )}
      </div>
      <div className={commentList && commentList.length > 0 ? '' : 'd-none'}>
        <Row className="justify-content-center">
          <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage}
                        i18nEnabled />
        </Row>
        <Row className="justify-content-center">
          <JhiPagination
            activePage={paginationState.activePage}
            onSelect={handlePagination}
            maxButtons={5}
            itemsPerPage={paginationState.itemsPerPage}
            totalItems={props.totalItems}
          />
        </Row>
      </div>
    </div>
  );
};

const mapStateToProps = ({comment}: IRootState) => ({
  commentList: comment.entities,
  loading: comment.loading,
  totalItems: comment.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Comment);
