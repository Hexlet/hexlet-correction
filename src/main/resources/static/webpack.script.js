const path = require('path');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');

module.exports = {
    mode: 'production',
    entry: './src/script/index.js',
    module: {
        rules: [
            {
                test: /\.jsx?$/,
                exclude: /node_modules/,
                loader: "babel-loader",
                options: {presets: ["@babel/env"]}
            }
        ]
    },
    resolve: {
        extensions: [ '.js' ]
    },
    output: {
        filename: 'correction.js',
        path: path.resolve(__dirname, 'public/js')
    },
    optimization: {
        minimizer: [new UglifyJsPlugin()],
    },
};
