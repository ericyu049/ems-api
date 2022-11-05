const express = require('express');
const auth = require('./src/controller/auth');
const employee = require('./src/controller/employee');
const review = require('./src/controller/review');
const feedback = require('./src/controller/feedback');
const story = require('./src/controller/story');
const sprint = require('./src/controller/sprint');
const project = require('./src/controller/project');

const app = express();
const port = 8080;

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.use('/auth', auth);
app.use('/employee', employee);
app.use('/review', review);
app.use('/feedback', feedback);
app.use('/story', story);
app.use('/sprint', sprint);
app.use('/project', project);

app.get('/health', (response) => {
    response.send('Application is healthy.');
})
app.listen(port, ()=> {
    console.log("Application listening on port", port);
})