import React, { Component, Fragment } from 'react';
import { Button, Form } from 'react-bootstrap';
import { setSession } from '../../util/session';
import { AUTH_API_ROOT } from '../../util/api_roots';

export default class LoginPage extends Component {
  state = { message: '' };

  handleLogin = async () => {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    this.setState({ message: 'Logging in...'});
    const response = await fetch(AUTH_API_ROOT + '/user', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
      headers: { 'Content-type': 'application/json; charset=UTF-8' }
    });
    if (response.status === 200) {
      this.setState({ message: 'Logged in! Redirecting...'});
      const responseJson = await response.json();
      if (responseJson.authType === 'standart') {
        setSession(username, responseJson.token, 'token');
      } else if (responseJson.authType === 'business') {
        setSession(username, responseJson.apiKey, 'api_key');
      } else if (responseJson.authType === 'admin') {
        setSession(username, responseJson.adminKey, 'admin_key');
      }
      window.location = '/';
    } else if (response.status === 401 || response.status === 404) {
      this.setState({ message: 'Username and password do not match.'});
    } else {
      this.setState({ message: 'Something went wrong.'});
    }
  };

  componentDidMount() {
    document.getElementById("password")
      .addEventListener("keyup", function(event) {
      event.preventDefault();
      if (event.keyCode === 13) {
        document.getElementById("submit_button").click();
      }
    });
  }

  render() {
    return (
      <Fragment>
        <h1>Login</h1>
        <br/>
        <Form>
          <Form.Group>
            <Form.Label>Username</Form.Label>
            <Form.Control id='username' type='username' placeholder='Enter username' />
          </Form.Group>
          <Form.Group>
            <Form.Label>Password</Form.Label>
            <Form.Control id='password' type='password' placeholder='Enter password' />
          </Form.Group>
          <div id='message'>{this.state.message}</div>
          <br/>
          <Button id='submit_button' variant='primary' onClick={this.handleLogin}>
            Submit
          </Button>
        </Form>
      </Fragment>
    );
  }
}
