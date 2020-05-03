// importing dependencies
import React from 'react';
import {BrowserRouter as Router} from 'react-router-dom'
import axios from 'axios'

// importing components
import NavBar from './components/NavBar'
import IndexBody from './components/indexBody'

// class definition
class App extends React.Component{

  // things should execute before load the components
  componentDidMount(){

    // SENSOR APPLICATIONS

    // set interval function using asynchronous - every 10 seconds
    setInterval(async () => {
      
      // get response with API using await 
      const response = await axios.get('/api/sensors');

      // assigning data using destructuring ES6 feature
      const {data} = response

      // for loop 
      for (let i = 0; i < data.length; i++) {

        // create random values for carbon dioxide and smoke levels
        const randomCarbonDioxideLevel = Math.floor(Math.random() * 10) + 1
        const randomSmokeLevel = Math.floor(Math.random() * 10) + 1
  
        // create new object using spread operation
        const updatedSensor = {...data[i]}

        // update properties 
        updatedSensor['carbonDioxideLevel'] = randomCarbonDioxideLevel;
        updatedSensor['smokeLevel'] = randomSmokeLevel;

        // send request to server to update every sensor 
        axios.put(`/api/sensors/${data[i]._id}`, updatedSensor)
      }
    },10000) // this will fire after every 10 seconds
  }

    // render function
    render() {
      return(
        <div>
          <Router>
            <NavBar/>
            <IndexBody />
          </Router>
        </div>
      )
    }
}

// export App
export default App;
