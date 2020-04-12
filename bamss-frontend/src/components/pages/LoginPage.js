import React from 'react';
import '../../style/App.css';
import { Button, Form } from 'react-bootstrap';

function LoginPage() {
  function handleLogin(event) {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const requestBody = { username, password };
    fetch("https://bamss-auth.herokuapp.com/user", {
      method: "POST",
      body: requestBody,
      headers: { "Content-type": "application/json; charset=UTF-8" }
    })
      .then(response => response.json())
      .then(
        (responseJson) => {
          console.log("JSON HERE");
          console.log(responseJson);
        },
        (error) => {
          console.log("ERROR HERE");
          console.log(error);
        }
      );
  };

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
          <br/>
          <Button variant="primary" type="submit" onClick={handleLogin}>
             Login
          </Button>
        </Form>
      </header>
    </div>
  );
}

export default LoginPage;
