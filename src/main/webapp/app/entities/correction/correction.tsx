import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './correction.reducer';
import { ICorrection } from 'app/shared/model/correction.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface ICorrectionProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type ICorrectionState = IPaginationBaseState;

export class Correction extends React.Component<ICorrectionProps, ICorrectionState> {
  state: ICorrectionState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { correctionList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="correction-heading">
          <Translate contentKey="hexletCorrectionApp.correction.home.title">Corrections</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="hexletCorrectionApp.correction.home.createLabel">Create a new Correction</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {correctionList && correctionList.length > 0 ? (
            <Table responsive aria-describedby="correction-heading">
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('id')}>
                    <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('reporterRemark')}>
                    <Translate contentKey="hexletCorrectionApp.correction.reporterRemark">Reporter Remark</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('correcterRemark')}>
                    <Translate contentKey="hexletCorrectionApp.correction.correcterRemark">Correcter Remark</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('resolverRemark')}>
                    <Translate contentKey="hexletCorrectionApp.correction.resolverRemark">Resolver Remark</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('textBeforeCorrection')}>
                    <Translate contentKey="hexletCorrectionApp.correction.textBeforeCorrection">Text Before Correction</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('textCorrection')}>
                    <Translate contentKey="hexletCorrectionApp.correction.textCorrection">Text Correction</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('textAfterCorrection')}>
                    <Translate contentKey="hexletCorrectionApp.correction.textAfterCorrection">Text After Correction</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('reporterName')}>
                    <Translate contentKey="hexletCorrectionApp.correction.reporterName">Reporter Name</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('pageURL')}>
                    <Translate contentKey="hexletCorrectionApp.correction.pageURL">Page URL</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('correctionStatus')}>
                    <Translate contentKey="hexletCorrectionApp.correction.correctionStatus">Correction Status</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="hexletCorrectionApp.correction.correcter">Correcter</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="hexletCorrectionApp.correction.resolver">Resolver</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {correctionList.map((correction, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${correction.id}`} color="link" size="sm">
                        {correction.id}
                      </Button>
                    </td>
                    <td>{correction.reporterRemark}</td>
                    <td>{correction.correcterRemark}</td>
                    <td>{correction.resolverRemark}</td>
                    <td>{correction.textBeforeCorrection}</td>
                    <td>{correction.textCorrection}</td>
                    <td>{correction.textAfterCorrection}</td>
                    <td>{correction.reporterName}</td>
                    <td>{correction.pageURL}</td>
                    <td>
                      <Translate contentKey={`hexletCorrectionApp.CorrectionStatus.${correction.correctionStatus}`} />
                    </td>
                    <td>
                      {correction.correcterId ? <Link to={`correcter/${correction.correcterId}`}>{correction.correcterId}</Link> : ''}
                    </td>
                    <td>{correction.resolverId ? <Link to={`correcter/${correction.resolverId}`}>{correction.resolverId}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${correction.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${correction.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${correction.id}/delete`} color="danger" size="sm">
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
            <div className="alert alert-warning">
              <Translate contentKey="hexletCorrectionApp.correction.home.notFound">No Corrections found</Translate>
            </div>
          )}
        </div>
        <div className={correctionList && correctionList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={this.state.activePage} total={totalItems} itemsPerPage={this.state.itemsPerPage} i18nEnabled />
          </Row>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={this.state.activePage}
              onSelect={this.handlePagination}
              maxButtons={5}
              itemsPerPage={this.state.itemsPerPage}
              totalItems={this.props.totalItems}
            />
          </Row>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ correction }: IRootState) => ({
  correctionList: correction.entities,
  totalItems: correction.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Correction);
