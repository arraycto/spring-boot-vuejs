const getUserName = () => localStorage.getItem('user_name');

const isLoggedIn = () => {
  const accessToken = localStorage.getItem('access_token');
  const expiresIn = localStorage.getItem('expires_in');
  return accessToken && (expiresIn - new Date().getTime() > 0);
};

export { getUserName, isLoggedIn };
