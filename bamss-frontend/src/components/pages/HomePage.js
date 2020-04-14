import React, { Component, Fragment } from 'react';
import { Button, FormControl, InputGroup } from 'react-bootstrap';
import { getSession } from '../../util/session';
import { CORE_API_ROOT } from '../../util/api_roots';

export default class HomePage extends Component {
  state = { message: '', key: '' };

  getExpireDate() {
    const dateInput = document.getElementById('expire-date').value;
    if (dateInput) {
      return new Date(dateInput).getTime();
    }
    return null;
  }

  getCustomUrl() {
    const value = document.getElementById('custom-key').value;
    if (!value) {
      return null;
    }
    return value;
  }

  handleShorten = async () => {
    const original_url = document.getElementById('original-url').value;
    const custom_url = this.getCustomUrl();
    const expire_date = this.getExpireDate();
    const auth_type = getSession().authType;
    const auth_entity = getSession().authEntity;
    this.setState({ message: 'Please wait...'});
    const response = await fetch(CORE_API_ROOT + '/shorten', {
      method: 'POST',
      body: JSON.stringify({ original_url, custom_url, expire_date, [auth_type]: auth_entity }),
      headers: { 'Content-type': 'application/json; charset=UTF-8' }
    });
    if (response.status === 201) {
      const responseJson = await response.json();
      document.getElementById('original-url').value = '';
      document.getElementById('custom-key').value = '';
      this.setState({ message: 'Short URL created!', key: responseJson.key});
    } else if (response.status === 429) {
      this.setState({ message: 'You are out of your daily quota! Consider upgrading your plan.', key: null });
    } else if (response.status === 406) {
      this.setState({ message: 'This custom URL exists, please provide another one or leave it blank.', key: null });
    } else {
      this.setState({ message: 'Something went wrong.', key: null});
    }
  };

  getAnonContent() {
    return (
      <Fragment>
        Shorten your links.
      </Fragment>
    );
  }
  
  getUserContent() {
    const lastKeyContent = this.state.key ? (
      <a href={CORE_API_ROOT + '/' + this.state.key} target='_blank' rel='noopener noreferrer'>
        {CORE_API_ROOT + '/' + this.state.key}
      </a>
    ) : ('');
    return (
      <Fragment>
        Long URL
        <FormControl id='original-url' aria-describedby='basic-addon3' style={{maxWidth: 350}}/>
        <br/>
        Custom Short URL (optional)
        <InputGroup className='mb-3'  style={{maxWidth: 350}}>
          <InputGroup.Prepend>
            <InputGroup.Text id='basic-addon3'>
              {CORE_API_ROOT}/
            </InputGroup.Text>
          </InputGroup.Prepend>
          <FormControl id='custom-key' aria-describedby='basic-addon3'/>
        </InputGroup>
        Expiration Date (optional)
        <input type='date' id='expire-date'></input>
        <br/>
        <div id='message'>{this.state.message}</div>
        {lastKeyContent}
        <br/>
        <Button variant='primary' onClick={this.handleShorten}>Shorten</Button>
      </Fragment>
    );
  }

  render() {
    const session = getSession();
    const content = (session !== null) ? this.getUserContent() : this.getAnonContent();
    return (
      <Fragment>
        <h1>BAMSS</h1>
        <h1>URL Shortener</h1>
        <br/>
        {content}
      </Fragment>
    );
  }
}
