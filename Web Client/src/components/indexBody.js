// importing libraries
import React from 'react'
import axios from 'axios';

// importing css
import css from '../public/css/index.css'

// importing images
import helper from '../public/images/helper.jpg'


// functional components to fetch table rows
const Sensor = props => (
    <tr>
        <td>{props.item.name}</td>
        <td>{props.item.floorNumber}</td>
        <td>{props.item.roomNumber}</td>
        <td>{props.item.carbonDioxideLevel}</td>        
        <td>{props.item.smokeLevel}</td>              
        <td>
            <button className=
                {props.item.carbonDioxideLevel > 5 || props.item.smokeLevel > 5  
                ? "btn btn-danger" 
                : "btn btn-success"}
                >
                    {props.item.carbonDioxideLevel > 5 || props.item.smokeLevel > 5 
                    ? "Activated" 
                    : "Normal"}
            </button>
        </td>         
    </tr>
)

// class definition
export default class NavBar extends React.Component {

    // constructor
    constructor(props){
        super(props)

        // declaring this state
        this.state = {
            // all sensor objects goes here
            sensors: []
        }
    }

    // things should fire before render
    componentDidMount (){

        // asynchronous function to fetch sensor details
        const getSensorInfo = async () => {
            // assigning response 
            const response = await axios.get('/api/sensors')

            // get data using destructuring
            const {data} = response;

            // update this state
            this.setState({sensors: data})

            //getting boolean value using every callback function
            const isAny = data.every((sensor) => {
                return sensor.carbonDioxideLevel < 6 && sensor.smokeLevel < 6
            })

            if (isAny) {
                // assigning fire alert message
                const message = document.querySelector('#fireMessage')
                // its hidden property set to false then user can see the alert
            
                // set value to empty value
                const h4 = message.querySelector('h4');
                h4.innerText = '';

                // set value to empty value
                const p = message.querySelector('p');
                p.innerText = '';
            }else {
                // invoke the fireAlarmActivated 
                this.fireAlarmActivated();
            }
        }

        // when page loaded first time this is going to execute
        getSensorInfo()

        // set interval for update the application
        setInterval(() => {
            // message 
            console.log('this will execute after every 40 seconds!')
            // call the getSensorInfo function in every 40 seconds
            getSensorInfo();
        }, 40000);        
    }

    // a function for rotate client image when emergency situation
    fireAlarmActivated() {
        // assigning fire alert message
        const message = document.querySelector('#fireMessage')
        // its hidden property set to false then user can see the alert
       
        const h4 = message.querySelector('h4');
        h4.innerText = 'The fire alert has been activated!!';

        const p = message.querySelector('p');
        p.innerText = 'please leave the building by the nearest exit and go to the nearest assembly point immediately!!';

        // assigning image using javascript Document Object Model
        const image = document.querySelector('img');

        // rotate 180 degrees left
        image.style.transform = 'scale(-1,1)'

        // declare a setTimeOut function for rotate image again to 180 degrees right
        setTimeout(() => {
            // rotate 180 degrees right
            image.style.transform = 'scale(1,1)'
        },20000)
    }

    // fetch table rows from the Sensor functional component
    SensorsList(){
        return this.state.sensors.map(currentItem => {
            return <Sensor item={currentItem} key={currentItem._id}/>
        })
    }

    // render function
    render(){
        return(
            <div className="main">
                {/* alert message */}
                <div id="fireMessage" className="fireActivated">
                    <h4 className="text-center"></h4>
                    <br />
                    <p className="text-center"></p>
                </div>
                <div className="container my-5">
                        {/* client image */}
                        <img src={helper} class="img-fluid rounded mx-auto d-block" alt="Responsive image"></img>

                        {/* all sensor details goes here */}
                        <table class="table table-striped main">
                            <thead>
                                <tr>
                                <th scope="col">Sensor Name</th>
                                <th scope="col">Floor Number</th>
                                <th scope="col">Room Number</th>
                                <th scope="col">Carbon Dioxide Level</th>
                                <th scope="col">Smoke Level</th>
                                <th scope="col">Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                {/* invoke sensorList function to fetch data */}
                                {this.SensorsList()}                             
                            </tbody>
                        </table>
                </div>
            </div>
        )
    }
}