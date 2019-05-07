const path = require('path');
const argv = require('yargs').argv;

export default {
  entry: 'src/index.js',
  extraBabelPlugins: [['import', { libraryName: 'antd', libraryDirectory: 'es', style: true }]],
  define: {
    'process.buildEnv': {
      env: argv.env || 'development',
      apiPrefix: argv.apiPrefix || '/api',
    },
  },
  env: {
    development: {
      extraBabelPlugins: ['dva-hmr'],
      define: {
        'process.env': {
          NODE_ENV: 'development',
        },
      },
    },
    production: {
      devtool: argv.sourceMap ? '#source-map' : '',
      define: {
        'process.env': {
          NODE_ENV: 'production',
        },
      },
    },
  },
  alias: {
    components: path.resolve(__dirname, 'src/components/'),
  },
  ignoreMomentLocale: true,
  theme: './src/theme.js',
  html: {
    template: './src/index.ejs',
  },
  disableDynamicImport: true,
  publicPath: '/',
  hash: true,
  proxy: {
    '/api': {
      target: 'http://127.0.0.1:8188',
      changeOrigin: true,
      pathRewrite: { '^/api': '' },
    },
  },
};
