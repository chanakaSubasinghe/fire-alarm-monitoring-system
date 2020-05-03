// condition to require env file for developer purpose
if (process.env.NODE_ENV !== 'production') {
    require('dotenv').config();
}

// importing dependencies
const  express = require ('express')
const  mongoose  = require('mongoose')

// create app
const app = express();

// declaring port - default port = 5000
const PORT = process.env.PORT || 5000;

// using json 
app.use(express.json());

// assigning database url from .env file
const databaseURL = process.env.MONGODB_URI;

// mongoose connection
mongoose.connect(databaseURL, {
    useNewUrlParser: true,
    useCreateIndex: true,
    useUnifiedTopology: true,
    useFindAndModify: false
});

// create connection
const connection = mongoose.connection;

// once connected to database, then console logging simple message
connection.once('open', () => {
    console.log('database connected!')
});

// requiring routes
const sensorRoute = require('./routes/sensor');
const userRoute = require('./routes/user')

// using routes
app.use(sensorRoute)
app.use(userRoute)

// port listener
app.listen(PORT,() => console.log(`Server running on PORT ${PORT}`))


