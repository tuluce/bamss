const CORE_API_ROOT = 'https://bamss.herokuapp.com';
const AUTH_API_ROOT = 'https://bamss-auth.herokuapp.com';
const ANALYTICS_API_ROOT = 'https://bamss-analytics.herokuapp.com';
const KEYGEN_API_ROOT = 'https://bamss-keygen.herokuapp.com';

function apiHealthCheck() {
  fetch(CORE_API_ROOT + '/health');
  fetch(AUTH_API_ROOT + '/health');
  fetch(ANALYTICS_API_ROOT + '/health');
  fetch(KEYGEN_API_ROOT + '/health');
}

export { apiHealthCheck, CORE_API_ROOT, AUTH_API_ROOT, ANALYTICS_API_ROOT, KEYGEN_API_ROOT };
