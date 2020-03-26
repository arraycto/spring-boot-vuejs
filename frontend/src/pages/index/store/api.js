import instance from '../../../plugins/axios';

export default { // TODO: 解决没有使用
  hello() {
    return instance.get('/hello');
  }
};
