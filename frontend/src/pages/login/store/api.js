import instance from '../../../plugins/axios';
import qs from 'qs';

export default {
  login(userName, password) {
    return instance.post('/access_token', qs.stringify({ userName, password }));
  }
};
