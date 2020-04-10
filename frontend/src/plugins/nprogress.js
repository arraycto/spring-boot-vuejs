import NProgress from 'nprogress';
import 'nprogress/nprogress.css';

NProgress.configure({ showSpinner: false });

NProgress.start();
window.onload = function () {
  NProgress.done();
};
