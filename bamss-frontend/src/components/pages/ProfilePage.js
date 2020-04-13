import React, { Component, Fragment } from 'react';
import '../../style/App.css';
import { Button, Card } from 'react-bootstrap';
import { getSession } from '../../util/session';
import { CORE_API_ROOT } from '../../util/api_roots';

class UrlCard extends Component {
  state = { showAnalytics: false };

  fetchAnalytics() {
    return "Here are the analytics";
  }

  handleAnalyticsClick() {
    this.setState({ showAnalytics: !this.state.showAnalytics });
  }

  render() {
    const rawDate = ("" + new Date(Number(this.props.shortUrl.expireDate))).substr(4, 11);
    const day = rawDate.substr(4, 2);
    const month = rawDate.substr(0, 3);
    const year = rawDate.substr(7, 4);
    const dateString = day + ' ' + month + ' ' + year;
    const buttonText = this.state.showAnalytics ? "Hide Analytics" : "Show Analytics"
    const shortLinkHref = 'https://bamss.herokuapp.com/' + this.props.shortUrl.key;
    const shortLinkDisplay = 'bamss.herokuapp.com/' + this.props.shortUrl.key;
    return (
      <Fragment>
        <Card style={{width: "90%"}}>
          <Card.Header style={{backgroundColor: "grey"}}>
            <a style={{color: "white"}} href={shortLinkHref}  target='_blank' rel='noopener noreferrer'>
              {shortLinkDisplay}
            </a>
          </Card.Header>
          <Card.Body style={{backgroundColor: "white", color: "black"}}>
            <b>Expiration date:</b> {dateString}<br/><br/>
            <Button variant="dark" onClick={() => this.handleAnalyticsClick()}>{buttonText}</Button>
            {
              this.state.showAnalytics && (
                <div>
                  <br/>{this.fetchAnalytics()}
                </div>
              )
            }
          </Card.Body>
        </Card>
        <br/>
      </Fragment>
    );
  }
}

export default class ProfilePage extends Component {
  state = { shortUrls: [] }

  async componentDidMount() {
    const auth_type = getSession().authType;
    const auth_entity = getSession().authEntity;
    const response = await fetch(CORE_API_ROOT + '/urls', {
      method: 'POST',
      body: JSON.stringify({ [auth_type]: auth_entity }),
      headers: { 'Content-type': 'application/json; charset=UTF-8' }
    });
    if (response.status === 200) {
      const responseJson = await response.json();
      this.setState({ shortUrls: responseJson });
    }
  }

  getUrlCard(shortUrl, i) {
    const rawDate = ("" + new Date(Number(shortUrl.expireDate))).substr(4, 11);
    const day = rawDate.substr(4, 2);
    const month = rawDate.substr(0, 3);
    const year = rawDate.substr(7, 4);
    const dateString = day + ' ' + month + ' ' + year;
    return (
      <Fragment key={i}>
        <Card style={{width: "90%"}}>
          <Card.Header style={{backgroundColor: "grey"}}>
            https://bamss.herokuapp.com/{shortUrl.key}
          </Card.Header>
          <Card.Body style={{backgroundColor: "white", color: "black"}}>
            <b>Expiration date:</b> {dateString}<br/><br/>
            <Button variant="dark">Show Analytics</Button>

          </Card.Body>
        </Card>
        <br/>
      </Fragment>
    );
  }

  render() {
    const urlCards = this.state.shortUrls.map((shortUrl, i) => (
      <UrlCard shortUrl={shortUrl} key={i} />
    ));
    return (
      <div className='App'>
        <header className='App-header'>
          <h1>My URLs (<b>{getSession().username}</b>)</h1>
          <br/>
          {urlCards}
        </header>
      </div>
    );
  }
}
