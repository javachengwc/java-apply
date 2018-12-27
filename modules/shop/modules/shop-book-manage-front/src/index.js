import '@babel/polyfill';
import 'url-polyfill';
import dva from 'dva';

import createHistory from 'history/createBrowserHistory';
// user BrowserHistory
// import createHistory from 'history/createBrowserHistory';
import createLoading from 'dva-loading';
import 'moment/locale/zh-cn';
import './rollbar';

import './index.less';
// 1. Initialize
const app = dva({
  history: createHistory(),
});

//alert("me index is 1");

// 2. Plugins
app.use(createLoading());

// 3. Register global model,加载global全局数据
app.model(require('./models/global').default);

// 4. Router,加载router路由
app.router(require('./router').default);

// 5. Start
app.start('#root');

export default app._store; // eslint-disable-line
