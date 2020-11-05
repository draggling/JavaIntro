"use strict"
import React from 'react';
import {Switch, Route} from 'react-router-dom';
//import Logout from './Logout.js';
import Header from './Header.js';
import Home from './Home.js';
import AppointmentMakeContainer from './Appointment/AppointmentMakeContainer';
import AppointmentContainer from './Appointment/AppointmentContainer';
import FeeContainer from './Fee/FeeContainer';
import InformationContainer from './Information/InformationContainer';
import InsuranceContainer from './Insurance/InsuranceContainer';
import LoginPage from './User/Login.js';
import ProfilePage from './User/Profile.js';
import PaypalContainer from './Paypal/PaypalContainer.js';
import VehicleDetailsContainer from './Vehicle/VehicleDetailsContainer.js';
import InfoDMVContainer from './InfoDMV/InfoDMVContainer.js';


export class App extends React.Component{
    render() {
        return(
            <div>
                <Header />
                <Switch>
                    <Route exact path='/' component={Home}/>
                    <Route path='/books' component={VehicleDetailsContainer}/>
                    <Route path='/authors' component={AppointmentContainer}/>
                    <Route path='/publishers' component={AppointmentMakeContainer}/>
                    <Route path='/genres' component={FeeContainer}/>
                    <Route path='/branches' component={InformationContainer}/>
                    <Route path='/borrowers' component={InsuranceContainer}/>
                    <Route path='/administratorhome' component={InsuranceContainer}/>
                    <Route path='/librarianhome' component={InsuranceContainer}/>
                    <Route path='/borrowerhome' component={InsuranceContainer}/>

                </Switch>
            </div>
        );
    }
}
