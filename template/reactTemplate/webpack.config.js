var HTMLWebpackPlugin=require('html-webpack-plugin');
var HTMLWebpackPluginConfig= new HTMLWebpackPlugin({
  template:__dirname+'/app/index.html',
  fileName:'index.html',
  inject:'body'
});

module.exports={
  entry:__dirname+'/app/index.jsx',
  module:{
    loaders:[{
      test:/\.jsx$/,
      exclude:/node_modules/,
      loader:'babel-loader'
    }]
  },
  resolve: {
    extensions: ['.js', '.jsx']
  },
  output:{
    filename:'transformed.js',
    path:__dirname+'/build'
  },
  plugins:[HTMLWebpackPluginConfig]
};