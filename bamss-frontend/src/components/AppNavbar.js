import React, { Component } from 'react';
import '../style/App.css';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';

export default class App extends Component {
  state = { expanded: false };

  handlePageClick(page) {
    this.props.setPage(page);
    this.setState({ expanded: false })
  }

  render() {
    return (
      <Navbar expanded={this.state.expanded} collapseOnSelect expand="lg" bg="dark" variant="dark">
        <Navbar.Brand href="#" onClick={() => this.handlePageClick("home")}>BAMSS</Navbar.Brand>
        <Navbar.Toggle 
          onClick={() => this.setState({ expanded: !this.state.expanded })} aria-controls="responsive-navbar-nav" 
        />
        <Navbar.Collapse id="responsive-navbar-nav">
          <Nav className="mr-auto">
            <Nav.Link onClick={() => this.handlePageClick("pricing")}>Pricing</Nav.Link>
          </Nav>
          <Nav>
            <Nav.Link onClick={() => this.handlePageClick("signup")}>Signup</Nav.Link>
            <Nav.Link onClick={() => this.handlePageClick("login")}>Login</Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
    );
  }
}
