function getSession() {
  const username = localStorage.getItem('username');
  const authEntity = localStorage.getItem('authEntity');
  const authEntityType = localStorage.getItem('authEntityType');
  if (username === null || authEntity === null || authEntityType === null) {
    return null;
  }
  return { username, authEntity, authEntityType };
}

function setSession(username, authEntity, authEntityType) {
  localStorage.setItem('username', username);
  localStorage.setItem('authEntity', authEntity);
  localStorage.setItem('authEntityType', authEntityType);
}

function clearSession() {
  localStorage.clear()
}

export { getSession, setSession, clearSession };
