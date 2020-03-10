import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import {Translate, translate} from 'react-jhipster';
import {NavDropdown} from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    style={{maxHeight: '80vh', overflow: 'auto'}}
  >
    <MenuItem icon="asterisk" to="/preference">
      <Translate contentKey="global.menu.entities.preference" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/correction">
      <Translate contentKey="global.menu.entities.correction" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/comment">
      <Translate contentKey="global.menu.entities.comment" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
