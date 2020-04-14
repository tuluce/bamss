import React, { Component, Fragment } from 'react';
import { Button, Form } from 'react-bootstrap';
import { AUTH_API_ROOT } from '../../util/api_roots';

export default class SignupPage extends Component {
  state = { success: false, message: '' };

  getAccountType() {
    if (document.getElementById('standard').checked) {
      return 'standard';
    }
    if (document.getElementById('business').checked) {
      return 'business';
    }
  }

  handleSignup = async () => {
    const email = document.getElementById('email').value;
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const account_type = this.getAccountType();
    this.setState({ message: 'Registering...'});
    const response = await fetch(AUTH_API_ROOT + '/user', {
      method: 'PUT',
      body: JSON.stringify({ email, username, password, account_type }),
      headers: { 'Content-type': 'application/json; charset=UTF-8' }
    });
    if (response.status === 201) {
      this.setState({ success: true, message: 'Register succeed! Now you can login.'});
    } else if (response.status === 400 || response.status === 409) {
      const responseJson = await response.json();
      this.setState({ message: responseJson.error });
    } else {
      this.setState({ message: 'Something went wrong.'});
    }
  };

  render() {
    const form = !this.state.success ? (
      <Form>
        <Form.Group>
          <Form.Label>Email</Form.Label>
          <Form.Control id='email' type='email' placeholder='Enter email' />
        </Form.Group>
        <Form.Group>
          <Form.Label>Username</Form.Label>
          <Form.Control id='username' type='username' placeholder='Enter username' />
        </Form.Group>
        <Form.Group>
          <Form.Label>Password</Form.Label>
          <Form.Control id='password' type='password' placeholder='Enter password' />
        </Form.Group>
        <Form.Group>
          <Form.Label>Account Type</Form.Label>
          <br/>
          <Form.Check inline label='standard' type='radio' name='account_type' id='standard' checked/>
          <Form.Check inline label='Business' type='radio' name='account_type' id='business'/>
        </Form.Group>
        <div id='message'>{this.state.message}</div>
        <br/>
        <Button variant='primary' onClick={this.handleSignup}>
          Submit
        </Button>
      </Form>
    ) : (
      <div id='message'>{this.state.message}</div>
    );
    return (
      <Fragment>
        <h1>Register</h1>
        <br/>
        {form}
      </Fragment>
    );
  }
}
