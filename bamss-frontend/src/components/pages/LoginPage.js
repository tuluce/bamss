import React, { Component } from 'react';
import '../../style/App.css';
import { Button, Form } from 'react-bootstrap';
import { setSession } from '../../util/session';

export default class LoginPage extends Component {
  constructor(props) {
    super(props);
    this.state = { message: '' };
  }

  handleLogin = async () => {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    this.setState({ message: 'Logging in...'});
    const response = await fetch("https://bamss-auth.herokuapp.com/user", {
      method: "POST",
      body: JSON.stringify({ username, password }),
      headers: { "Content-type": "application/json; charset=UTF-8" }
    })
    if (response.status === 200) {
      this.setState({ message: 'Logged in! Redirecting...'});
      const responseJson = await response.json();
      if (responseJson.authType === "standart") {
        setSession(username, responseJson.token, "token");
      } else if (responseJson.authType === "business") {
        setSession(username, responseJson.apiKey, "apiKey");
      }
      window.location = "/";
    } else if (response.status === 401) {
      this.setState({ message: 'Username and password do not match.'});
    } else {
      this.setState({ message: 'Something went wrong.'});
    }
  };

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <Form>
            <Form.Group>
              <Form.Label>Username</Form.Label>
              <Form.Control id="username" type="username" placeholder="Enter username" />
            </Form.Group>
            <br/>
            <Form.Group>
              <Form.Label>Password</Form.Label>
              <Form.Control id="password" type="password" placeholder="Enter password" />
            </Form.Group>
            <div id="message">{this.state.message}</div>
            <br/>
            <Button variant="primary" type="submit" onClick={this.handleLogin}>
              Login
            </Button>
          </Form>
        </header>
      </div>
    );
  }
}
