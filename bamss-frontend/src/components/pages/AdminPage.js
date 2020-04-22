import React, { Component, Fragment } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';
import { getSession } from '../../util/session';
import { ANALYTICS_API_ROOT } from '../../util/api_roots';

let start_date;
let end_date;
let resolution;

export default class AdminPage extends Component {
  state = { analytics: "Loading..." };
  
  getChart(data, isSingle) {
    let line1 = "";
    let line2 = "";
    if (isSingle) {
      line1 = <Line type="monotone" dataKey="hits" stroke="#8884d8" strokeWidth={3} />
    } else {
      line1 = <Line type="monotone" dataKey="standard" stroke="#8884d8" strokeWidth={3}/>
      line2 = <Line type="monotone" dataKey="business" stroke="#82ca9d" strokeWidth={3}/>
    }
    return (
      <div>
        <LineChart
          width={window.innerWidth * 0.9}
          height={400}
          data={data}
          margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="time" />
          <YAxis />
          <Tooltip wrapperStyle={{color: "black"}}/>
          <Legend />
          {line1}
          {line2}
        </LineChart>
        <br/>
        <br/>
      </div>
    );
  }

  processSingleData(data) {
    let startIndex = 0;
    let endIndex = data.length;
    for (let i = 0; i < data.length; i++) {
      if (data[i].hits !== 0) {
        startIndex = i;
        break;
      }
    }
    for (let i = data.length - 1; i >= 0; i--) {
      if (data[i].hits !== 0) {
        endIndex = i + 1;
        break;
      }
    }
    return data.slice(startIndex, endIndex);
  }

  processDoubleData(data) {
    let startIndex = 0;
    let endIndex = data.length;
    for (let i = 0; i < data.length; i++) {
      if (data[i].standard !== 0 || data[i].business !== 0) {
        startIndex = i;
        break;
      }
    }
    for (let i = data.length - 1; i >= 0; i--) {
      if (data[i].standard !== 0 || data[i].business !== 0) {
        endIndex = i + 1;
        break;
      }
    }
    return data.slice(startIndex, endIndex);
  }

  formatTs(timestamp) {
    const day = ("" + new Date(timestamp)).substr(8, 2);
    const month = ("" + new Date(timestamp)).substr(4, 3);
    const time = ("" + new Date(timestamp)).substr(16, 5);
    return day + " " + month + " " + time;
  }

  mergeData(data) {
    if (!data || (!data.standard && !data.business)) {
      return [];
    }
    if (!data.standard) {
      data.standard = data.business.map(d => 0);
    }
    if (!data.business) {
      data.business = data.standard.map(d => 0);
    }
    const mergedData = data.standard.map((c, i) => {
      let formattedDate = "";
      if (data.standard[i] || data.business[i]) {
        formattedDate = this.formatTs(start_date + resolution * i)
      }
      return {
        time: formattedDate,
        standard: data.standard[i],
        business: data.business[i]
      }
    });
    return this.processDoubleData(mergedData);
  }

  getVisuals(analyticsData) {
    const rawRedirectData = analyticsData.redirect.total.map((count, i) => {
      let formattedDate = "";
      if (count) {
        formattedDate = this.formatTs(start_date + resolution * i)
      }
      return {
        time: formattedDate,
        hits: count
      }
    });
    const redirectData = this.processSingleData(rawRedirectData);

    const shortenData = this.mergeData(analyticsData.shorten);
    const loginData = this.mergeData(analyticsData.login);
    const signupData = this.mergeData(analyticsData.signup);
    
    return (
      <Fragment>
        Redirect Requests
        {this.getChart(redirectData, true)}

        Shorten Requests
        {this.getChart(shortenData, false)}

        Login Requests
        {this.getChart(loginData, false)}

        Signup Requests
        {this.getChart(signupData, false)}
      </Fragment>
    );
      
  }

  async fetchAnalytics() {
    const THREE_DAYS = 259200000;
    const ONE_HOUR = 3600000;
    end_date = new Date().getTime();
    start_date = end_date - THREE_DAYS;
    resolution = ONE_HOUR;
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
      <Fragment>
        <h1>Admin Analytics</h1>
        <br/>
        {this.state.analytics}
      </Fragment>
    );
  }
}
