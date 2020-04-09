// 多页面配置
const titles = require('./title.js');
const glob = require('glob');
const pages = {};

glob.sync('./src/pages/**/main.js').forEach(path => {
  const chunk = path.split('./src/pages/')[1].split('/main.js')[0];
  // 多页面配置
  pages[chunk] = {
    // 页面入口
    entry: path,
    // 原始模板
    template: 'public/index.html',
    // 填充原始模板的网页名称（`<title><%= htmlWebpackPlugin.options.title %></title>`）
    title: titles[chunk],
    // 指定要包含在该页面上的块，默认已提取出了 `chunk-vendors` 和 `chunk-common`
    chunks: ['chunk-vendors', 'chunk-common', chunk]
  }
});

module.exports = {
  // 以多页模式构建应用
  pages,

  // Ant Design 定制主题
  css: {
    loaderOptions: {
      less: {
        modifyVars: {
          'primary-color': '#1DA57A',
          'link-color': '#1DA57A',
          'border-radius-base': '2px',
        },
        javascriptEnabled: true,
      },
    },
  },

  // 配置前端开发服务器 webpack dev-server
  // 将前端（如 Axios）所有以 `/api` 为基准路径的请求都代理至后端服务
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8090/app',
        ws: true,
        changeOrigin: true
      }
    }
  },
  // 将前端构建目录配置为 Maven 风格
  outputDir: 'target/dist',
  assetsDir: 'static'
};
