import React, { Component, Fragment } from 'react';
import { Button, Card } from 'react-bootstrap';
import { PieChart, Pie, Tooltip } from 'recharts';
import { getSession } from '../../util/session';
import { CORE_API_ROOT, ANALYTICS_API_ROOT } from '../../util/api_roots';

class UrlCard extends Component {
  state = { showAnalytics: false, analytics: "Loading..." };

  getChart(title, data) {
    return (
      <div style={{width: 300, float: 'left'}}>
        <b>{title}</b>
        <PieChart width={280} height={200}>
          <Pie dataKey="value" data={data}
            cx={140} cy={100} innerRadius={60} outerRadius={80} paddingAngle={5}
            fill="black" label />
          <Tooltip />
        </PieChart>
        <br/>
      </div>
    );
  }

  getCountIndicator(count) {
    return (
      <div style={{width: 280, height: 200, float: 'left'}}>
        <b>Hit Count</b>
        <div style={{fontSize: '350%', padding: '10px 0'}}>
          {count}
        </div>
        <br/>
      </div>
    );
  }

  extractData(analyticsElement) {
    return Object.keys(analyticsElement).map(key => ({ 
      name: key ? key : "unknown",
      value: analyticsElement[key]
    }));
  }

  getVisuals(analyticsData) {
    if (!analyticsData) {
      return this.getCountIndicator(0);
    }
    const totalCount = this.getCountIndicator(analyticsData.total.total);
    const osChart = this.getChart("OS", this.extractData(analyticsData.os));
    const platformChart = this.getChart("Platform", this.extractData(analyticsData.platform));
    const regionChart = this.getChart("Region", this.extractData(analyticsData.region));
    return (<Fragment> {totalCount} {osChart} {platformChart} {regionChart} </Fragment>);
  }

  async fetchAnalytics() {
    const keys = [this.props.shortUrl.key]
    const start_date = 0;
    const end_date = Number.MAX_SAFE_INTEGER
    const auth_type = getSession().authType;
    const auth_entity = getSession().authEntity;
    const response = await fetch(ANALYTICS_API_ROOT + '/analytics', {
      method: 'POST',
      body: JSON.stringify({ keys, start_date, end_date, [auth_type]: auth_entity }),
      headers: { 'Content-type': 'application/json; charset=UTF-8' }
    });
    if (response.status === 200) {
      const responseJson = await response.json();
      const analyticsData = responseJson.data[this.props.shortUrl.key]
      const analytics = this.getVisuals(analyticsData);
      this.setState({ ...this.state, analytics });
    } else {
      this.setState({ ...this.state, analytics: "Something went wrong." });
    }
  }

  handleAnalyticsClick() {
    const analytics = "Loading..."
    if (!this.state.showAnalytics) {
      this.fetchAnalytics();
    }
    const showAnalytics = !this.state.showAnalytics;
    this.setState({ showAnalytics, analytics });
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
                  <br/>{this.state.analytics}
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
    const apiKeySection = getSession().authType === 'api_key' ? (
      <Fragment>
        <h1>My API Key (<b>{getSession().username}</b>)</h1>
        <p>
          [<font color="#282c34" style={{fontSize: '50%'}}>{getSession().authEntity}</font>]
          <br/>(select to reveal)
        </p>
        <br/><br/>
      </Fragment>
    ) : ('');
    const urlCards = this.state.shortUrls.map((shortUrl, i) => (
      <UrlCard shortUrl={shortUrl} key={i} />
    ));
    return (
      <Fragment>
        {apiKeySection}
        <h1>My URLs (<b>{getSession().username}</b>)</h1>
        <br/>
        {urlCards}
      </Fragment>
    );
  }
}
