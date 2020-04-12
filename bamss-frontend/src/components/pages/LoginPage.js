import React from 'react';
import '../../style/App.css';
import { Button, Form } from 'react-bootstrap';

function LoginPage() {
  return (
    <div className="App">
      <header className="App-header">
        <Form>
          <Form.Group controlId="formBasicEmail">
            <Form.Label>Username</Form.Label>
            <Form.Control type="username" placeholder="Enter username" />
          </Form.Group>
          <Form.Group controlId="formBasicPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control type="password" placeholder="Password" />
          </Form.Group>

          <Button variant="primary" type="submit">
             Login
          </Button>
        </Form>
      </header>
    </div>
  );
}

export default LoginPage;
