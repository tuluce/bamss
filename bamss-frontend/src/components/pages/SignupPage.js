import React from 'react';
import '../../style/App.css';
import { Button, Form } from 'react-bootstrap';

function SignupPage() {
  return (
    <div className='App'>
      <header className='App-header'>
        <Form>
          <Form.Group controlId='formBasicEmail'>
            <Form.Label>Email address</Form.Label>
            <Form.Control type='email' placeholder='Enter email' />
          </Form.Group>
          <Form.Group controlId='formBasicUsername'>
            <Form.Label>Username</Form.Label>
            <Form.Control type='username' placeholder='Enter username' />
            <Form.Text className='text-muted'> </Form.Text>
          </Form.Group>
          <Form.Group controlId='formBasicPassword'>
            <Form.Label>Password</Form.Label>
            <Form.Control type='password' placeholder='Password' />
          </Form.Group>
          <Form.Group controlId='formBasicCheckbox'>
            <Form.Check type='checkbox' label='Business Account' />
          </Form.Group>
          <Button variant='primary' type='submit'>
            Sign Up
          </Button>
        </Form>
      </header>
    </div>
  );
}

export default SignupPage;
