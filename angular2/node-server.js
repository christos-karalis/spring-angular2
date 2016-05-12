var express = require('express');

var app = express();
app.use(express.static('build/webapp', {'index': ['index.html', 'index.htm']}));
// app.use('/src/main/webapp/app', express.static('src/main/webapp/app', {'index': ['index.html', 'index.htm']}));
app.use('/rest', [express.static('src/main/webapp/test', {'index': ['rest.json'], 'extensions': ['json']})]);
app.post('/authenticate', function(req, res, next) {
  if (req.query['username']==='user'&&req.query['password']==='password') {
      res.sendStatus(200);
    } else {
      res.sendStatus(403);
    }
})
app.post('/logout', function(req, res, next) {
  res.sendStatus(200);
});
app.listen(3000);
