import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Correcter from './correcter';
import CorrecterDetail from './correcter-detail';
import CorrecterUpdate from './correcter-update';
import CorrecterDeleteDialog from './correcter-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CorrecterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CorrecterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CorrecterDetail} />
      <ErrorBoundaryRoute path={match.url} component={Correcter} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CorrecterDeleteDialog} />
  </>
);

export default Routes;
