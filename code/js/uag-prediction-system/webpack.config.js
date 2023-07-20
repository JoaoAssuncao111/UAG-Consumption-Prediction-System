module.exports = {
    mode: "development",
    resolve: {
        extensions: [".js", ".ts", ".tsx"]
    },
    devServer: {
		hot: false,
        historyApiFallback: true,
        allowedHosts:"all"
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/
            },
			{
                test: /\.css$/,
                use: ['style-loader', 'css-loader'],
            },
        ]
    }
}