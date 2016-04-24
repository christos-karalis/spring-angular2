var express = require('express');
var serveStatic = require('serve-static');

var app = express();
app.use(serveStatic('build/webapp', {'index': ['index.html', 'index.htm']}));
app.use('/rest', serveStatic('src/main/webapp/test', {'index': ['rest.json']}));
app.listen(3000);