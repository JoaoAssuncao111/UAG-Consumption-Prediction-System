import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { Home } from '../components/Home';
import { Uags } from '../components/Uags';
import {Temperature} from '../components/Temperature'
import { ReadingChoice } from '../components/ReadingChoice';
import { Levels } from '../components/Levels';
import { Humidity } from '../components/Humidity';


const App = () => {

    return (
        <Router>
            <Switch>
                <Route path="/humidity" component={Humidity}></Route>
                <Route path="/readings" component={ReadingChoice}></Route>
                <Route path="/levels" component={Levels}></Route>
                <Route path="/temperature" component={Temperature} />
                <Route path="/uags" component={Uags} />
                <Route path="/" component={Home} />
            </Switch>
        </Router>
    );
};

export default App;