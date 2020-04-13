import React, { Component, Fragment } from 'react';
import '../style/App.css';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import { getSession } from '../util/session';

export default class App extends Component {
  state = { expanded: false };

  handlePageClick(page) {
    this.props.setPage(page);
    this.setState({ expanded: false })
  }

  getAnonLinks() {
    return (
      <Fragment>
        <Nav className='mr-auto'>
          <Nav.Link onClick={() => this.handlePageClick('pricing')}>Pricing</Nav.Link>
          <Nav.Link> </Nav.Link>
        </Nav>
        <Nav>
          <Nav.Link onClick={() => this.handlePageClick('signup')}>Register</Nav.Link>
          <Nav.Link onClick={() => this.handlePageClick('login')}>Login</Nav.Link>
        </Nav>
      </Fragment>
    );
  }

  getAdminLinks() {
    return (
      <Fragment>
        <Nav className='mr-auto'>
          <Nav.Link><i>Admin Session</i></Nav.Link>
        </Nav>
        <Nav>
          <Nav.Link onClick={() => this.handlePageClick('logout')}>Log out</Nav.Link>
        </Nav>
      </Fragment>
    );
  }

  getUserLinks(session) {
    if (session.authType === "admin_key") {
      return this.getAdminLinks();
    }
    return (
      <Fragment>
        <Nav className='mr-auto'>
          <Nav.Link onClick={() => this.handlePageClick('pricing')}>Pricing</Nav.Link>
          <Nav.Link> </Nav.Link>
        </Nav>
        <Nav>
          <Nav.Link onClick={() => this.handlePageClick('profile')}>{session.username}</Nav.Link>
          <Nav.Link onClick={() => this.handlePageClick('logout')}>Log out</Nav.Link>
        </Nav>
      </Fragment>
    );
  }

  render() {
    const session = getSession();
    const links = (session !== null) ? this.getUserLinks(session) : this.getAnonLinks();
    return (
      <Navbar expanded={this.state.expanded} collapseOnSelect expand='lg' bg='dark' variant='dark'>
        <Navbar.Brand href='#' onClick={() => this.handlePageClick('home')}>BAMSS</Navbar.Brand>
        <Navbar.Toggle 
          onClick={() => this.setState({ expanded: !this.state.expanded })} aria-controls='responsive-navbar-nav' 
        />
        <Navbar.Collapse id='responsive-navbar-nav'>
          {links}
        </Navbar.Collapse>
      </Navbar>
    );
  }
}
