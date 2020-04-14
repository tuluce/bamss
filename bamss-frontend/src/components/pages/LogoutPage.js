import React, { Component, Fragment } from 'react';
import { clearSession } from '../../util/session';

export default class LogoutPage extends Component {
  componentDidMount() {
    clearSession();
    window.location = '/';
  }

  render() {
    return (<Fragment>Logging out...</Fragment>);
  }
}
