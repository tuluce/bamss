function getSession() {
  const username = localStorage.getItem('username');
  const authEntity = localStorage.getItem('authEntity');
  const authType = localStorage.getItem('authType');
  if (username === null || authEntity === null || authType === null) {
    return null;
  }
  return { username, authEntity, authType };
}

function setSession(username, authEntity, authType) {
  localStorage.setItem('username', username);
  localStorage.setItem('authEntity', authEntity);
  localStorage.setItem('authType', authType);
}

function clearSession() {
  localStorage.clear()
}

export { getSession, setSession, clearSession };
