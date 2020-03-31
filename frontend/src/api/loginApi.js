import instance from '../plugins/axios';
import qs from 'qs';

export default {
  getToken(userName, password) {
    return instance.post('/token', qs.stringify({ user_name: userName, password }));
  },
  refreshToken() {
    return instance.get('/token/refresh');
  }
};
