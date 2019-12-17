import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Comment from './comment';
import CommentDetail from './comment-detail';
import CommentUpdate from './comment-update';
import CommentDeleteDialog from './comment-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CommentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CommentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CommentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Comment} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CommentDeleteDialog} />
  </>
);

export default Routes;
