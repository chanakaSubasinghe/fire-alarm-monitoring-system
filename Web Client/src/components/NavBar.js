// importing libraries
import React from 'react'
import {Link} from 'react-router-dom'

// importing css
import css from '../public/css/navbar.css'

// export class
export default class NavBar extends React.Component {

    // render function
    render(){
        return(
            <nav class="navbar navbar-expand-lg navbar-dark">
                <Link class="navbar-brand" href="/">Fire Alarm Monitoring System</Link>
            </nav>
        )
    }
}