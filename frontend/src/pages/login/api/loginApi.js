import instance from '../../../plugins/axios';
import qs from 'qs';

export default {
  login(userName, password) {
    return instance.post('/token', qs.stringify({ user_name: userName, password }));
  }
};
