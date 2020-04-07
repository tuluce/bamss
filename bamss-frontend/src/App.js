import React from 'react';
import logo from './logo.svg';
import './App.css';
import { Button, FormControl } from 'react-bootstrap';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        Bamss URL Shortener
        <br/>
        <br/>
        <FormControl id="basic-url" aria-describedby="basic-addon3" style={{maxWidth: 300}}/>
        <br/>
        <Button variant="primary">Shorten</Button>
      </header>
    </div>
  );
}

export default App;
