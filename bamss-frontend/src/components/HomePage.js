import React from 'react';
import logo from '../resources/logo.svg';
import '../style/App.css';
import { Button, FormControl } from 'react-bootstrap';
function HomePage() {
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
        <br/>
      </header>
    </div>
  );
}

export default HomePage;
