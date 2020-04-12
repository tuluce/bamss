import React, { Component, Fragment } from 'react';
import '../../style/App.css';
import { Button, FormControl, InputGroup } from 'react-bootstrap';
import { getSession } from '../../util/session';
import { CORE_API_ROOT } from '../../util/api_roots';

export default class HomePage extends Component {
  getAnonContent() {
    return (
      <Fragment>
        Shorten your links.
      </Fragment>
    );
    // style={{maxWidth: 300}}
  }
  
  getUserContent() {
    return (
      <Fragment>
        Long URL
        <FormControl id='basic-url' aria-describedby='basic-addon3' style={{maxWidth: 350}}/>
        <br/>
        Custom Short URL (optional)
        <InputGroup className="mb-3"  style={{maxWidth: 350}}>
          <InputGroup.Prepend>
            <InputGroup.Text id="basic-addon3">
              {CORE_API_ROOT}/
            </InputGroup.Text>
          </InputGroup.Prepend>
          <FormControl id="basic-url" aria-describedby="basic-addon3"/>
        </InputGroup>
        <br/>
        <Button variant='primary'>Shorten</Button>
      </Fragment>
    );
  }

  render() {
    const session = getSession();
    const content = (session !== null) ? this.getUserContent() : this.getAnonContent();
    return (
      <div className='App'>
        <header className='App-header'>
          <h1>BAMSS</h1>
          <h1>URL Shortener</h1>
          <br/><br/>
          {content}
        </header>
      </div>
    );
  }
}
