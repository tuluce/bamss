function getSession() {
  const token = localStorage.getItem("token");
  const username = localStorage.getItem("username");
  if (token === null || username === null) {
    return null;
  }
  return {
    token,
    username
  }
}

export default getSession;
