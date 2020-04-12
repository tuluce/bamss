import React, { Fragment } from 'react';
import '../style/App.css';
import HomePage from './pages/HomePage'
import SignupPage from './pages/SignupPage'
import LoginPage from './pages/LoginPage'
import ProfilePage from './pages/ProfilePage'
import AdminPage from './pages/AdminPage'
import PricingPage from './pages/PricingPage'

export default function AppContent(props) {
  return (
    <Fragment>
      {props.page === "home" && <HomePage/>}
      {props.page === "signup" && <SignupPage/>}
      {props.page === "login" && <LoginPage/>}
      {props.page === "profile" && <ProfilePage/>}
      {props.page === "admin" && <AdminPage/>}
      {props.page === "pricing" && <PricingPage/>}
    </Fragment>
  );
}
