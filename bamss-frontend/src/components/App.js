import React, { Component, Fragment } from 'react';
import '../style/App.css';
import AppNavbar from './AppNavbar';
import AppContent from './AppContent';

export default class App extends Component {  
  state = { page: 'home' };

  setPage = (page) => {
    this.setState({ ...this.state, page });
  }

  render() {
    return (
      <Fragment>
        <AppNavbar setPage={this.setPage}/>
        <AppContent page={this.state.page}/>
      </Fragment>
    );
  }
}
