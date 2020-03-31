import loginApi from '../api/loginApi';
import jwt_decode from 'jwt-decode';

const isLoggedIn = () => {
  const accessToken = localStorage.getItem('access_token');
  const expiresIn = localStorage.getItem('expires_in');
  const currentTimestamp = new Date().getTime() / 1000; // 精确到秒
  return accessToken && (expiresIn - currentTimestamp > 0);
};

const refreshToken = () => {
  loginApi.refreshToken()
    .then(({ data }) => {
      // 请求成功
      const { access_token: accessToken } = data.result;

      // 解析 JWT
      const decoded = jwt_decode(accessToken);
      const { user_name: userName, roles, exp: expiresIn } = decoded;

      localStorage.setItem('user_name', userName);
      localStorage.setItem('roles', roles);
      // 前端 token 的刷新机制：
      // 当 `expires_in` 减去当前 UTC 时间的毫秒值小于 10 分钟时刷新 token
      localStorage.setItem('access_token', accessToken);
      // 过期时间于，相对 UTC 时间的毫秒值
      localStorage.setItem('expires_in', expiresIn);
    })
    .catch(error => {
      // 请求失败
      // 取消前端登录缓存
      localStorage.removeItem('access_token');
    });
};

const autoRefreshToken = (url) => {
  const expiresIn = localStorage.getItem('expires_in');
  const currentSeconds = new Date().getTime() / 1000; // 精确到秒
  // 当 access token 有效期小于 1 天时刷新，
  const leastValidSeconds = 24 * 60 * 60;
  if (expiresIn && (expiresIn - currentSeconds > 0) && (expiresIn - currentSeconds < leastValidSeconds) && (url !== '/token/refresh')) {
    refreshToken(); // 若刷新失败，则会自动取消前端登录缓存
  }
};

const getRoleArray = () => {
  const roles = localStorage.getItem('roles');
  return roles.split(',');
};

export { isLoggedIn, autoRefreshToken, getRoleArray };
