import React, { Component } from 'react';
import '../../style/App.css';
import { clearSession } from '../../util/session';

export default class LogoutPage extends Component {
  componentDidMount() {
    clearSession();
    window.location = '/';
  }

  render() {
    return (
      <div className='App'>
        <header className='App-header'>
          Logging out...
        </header>
      </div>
    );
  }
}
