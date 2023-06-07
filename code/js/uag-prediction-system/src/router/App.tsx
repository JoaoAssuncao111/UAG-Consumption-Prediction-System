import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { Home } from '../components/Home';
import { Uags } from '../components/Uags';
import {Temperature} from '../components/Temperature'
import { ReadingDataInput } from '../components/ReadingDataInput';


const App = () => {

    return (
        <Router>
            <Switch>
                <Route path="/temperature" component={Temperature} />
                <Route path="/uags" component={Uags} />
                <Route path="/" component={Home} />
            </Switch>
        </Router>
    );
};

export default App;