import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { Home } from '../components/routing/Home';
import { Uags } from '../components/manipulation/Uags';
import {Temperature} from '../components/representation/Temperature'
import { ReadingChoice } from '../components/routing/ReadingChoice';
import { Levels } from '../components/representation/Levels';
import { Humidity } from '../components/representation/Humidity';
import { UpdateChoice } from '../components/routing/UpdateChoice';
import { InsertUag } from '../components/manipulation/InsertUag';
import { Uag } from '../components/representation/Uag';
import { Prediction } from '../components/manipulation/Prediction';
import { IPMA } from '../components/representation/Ipma';
import { InsertOrUpdateLevel } from '../components/manipulation/InsertOrUpdateLevel';


const App = () => {

    return (
        <Router>
            <Switch>
                <Route path="/humidity" component={Humidity}></Route>
                <Route path="/readings" component={ReadingChoice}></Route>
                <Route path="/insertions"component={UpdateChoice}></Route>
                <Route path="/levels" component={Levels}></Route>
                <Route path="/temperature" component={Temperature} />
                <Route path="/predict" component={Prediction} />
                <Route path="/insertuag" component={InsertUag} />
                <Route path="/uags" component={Uags} />
                <Route path="/ipma" component={IPMA}></Route>
                <Route path="/uag/:name" component={Uag}></Route>
                <Route path="/update" component={InsertOrUpdateLevel} />
                <Route path="/" component={Home} />
                
            </Switch>
        </Router>
    );
};

export default App;