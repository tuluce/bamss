import React, { Component, Fragment } from 'react';
import '../../style/App.css';
import { PieChart, Pie, Tooltip } from 'recharts';
import { getSession } from '../../util/session';
import { ANALYTICS_API_ROOT } from '../../util/api_roots';

export default class AdminPage extends Component {
  state = { analytics: "Loading..." };

  getVisuals(analyticsData) {
    return (JSON.stringify(analyticsData));
  }

  async fetchAnalytics() {
    const SEVEN_DAYS = 604800000;
    const ONE_HOUR = 3600000;
    const end_date = new Date().getTime();
    const start_date = end_date - SEVEN_DAYS;
    const resolution = ONE_HOUR;
    const auth_type = getSession().authType;
    const auth_entity = getSession().authEntity;
    const response = await fetch(ANALYTICS_API_ROOT + '/admin', {
      method: 'POST',
      body: JSON.stringify({ start_date, end_date, resolution, [auth_type]: auth_entity }),
      headers: { 'Content-type': 'application/json; charset=UTF-8' }
    });
    if (response.status === 200) {
      const responseJson = await response.json();
      const analyticsData = responseJson.data;
      const analytics = this.getVisuals(analyticsData);
      this.setState({ analytics });
    } else {
      this.setState({ analytics: "Something went wrong." });
    }
  }

  componentDidMount() {
    this.fetchAnalytics();
  }

  render() {
    return (
      <div className='App'>
        <header className='App-header'>
          <h1>Admin Analytics</h1>
          <br/>
          {this.state.analytics}
        </header>
      </div>
    );
  }
}
