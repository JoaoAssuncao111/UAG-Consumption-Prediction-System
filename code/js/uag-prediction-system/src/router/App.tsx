import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { Home } from '../components/Home';
import { Uags } from '../components/Uags';
import {Temperature} from '../components/Temperature'
import { ReadingChoice } from '../components/ReadingChoice';
import { Levels } from '../components/Levels';
import { Humidity } from '../components/Humidity';
import { UpdateChoice } from '../components/UpdateChoice';
import { InsertUag } from '../components/InsertUag';
import { Uag } from '../components/Uag';
import { Training } from '../components/Training';
import { Prediction } from '../components/Prediction';
import { IPMA } from '../components/Ipma';


const App = () => {

    return (
        <Router>
            <Switch>
                <Route path="/humidity" component={Humidity}></Route>
                <Route path="/readings" component={ReadingChoice}></Route>
                <Route path="/insertions"component={UpdateChoice}></Route>
                <Route path="/levels" component={Levels}></Route>
                <Route path="/temperature" component={Temperature} />
                <Route path="/training" component={Training} />
                <Route path="/predict" component={Prediction} />
                <Route path="/insertuag" component={InsertUag} />
                <Route path="/uags" component={Uags} />
                <Route path="/ipma" component={IPMA}></Route>
                <Route path="/uag/:name" component={Uag}></Route>
                <Route path="/" component={Home} />
                
            </Switch>
        </Router>
    );
};

export default App;