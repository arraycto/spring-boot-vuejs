import instance from '../plugins/axios';

export default {
  hello() {
    return instance.get('/hello');
  }
};
