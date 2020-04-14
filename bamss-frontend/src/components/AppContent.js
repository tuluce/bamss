import React, { Fragment } from 'react';
import '../style/App.css';
import HomePage from './pages/HomePage'
import SignupPage from './pages/SignupPage'
import LoginPage from './pages/LoginPage'
import ProfilePage from './pages/ProfilePage'
import AdminPage from './pages/AdminPage'
import PricingPage from './pages/PricingPage'
import LogoutPage from './pages/LogoutPage'
import { getSession } from '../util/session';

export default function AppContent(props) {
  const session = getSession();
  if (session !== null && session.authType === 'admin_key' && props.page !== 'logout') {
    return <AdminPage />
  }
  return (
    <Fragment>
      {props.page === 'home' && <HomePage />}
      {props.page === 'signup' && <SignupPage />}
      {props.page === 'login' && <LoginPage />}
      {props.page === 'profile' && <ProfilePage />}
      {props.page === 'pricing' && <PricingPage />}
      {props.page === 'logout' && <LogoutPage />}
    </Fragment>
  );
}
