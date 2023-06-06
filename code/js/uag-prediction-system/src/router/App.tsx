import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { Home } from '../components/Home';

const routes = [
    {
        path: '/home',
        element: <Home />
    }
];

const App = () => {


    return (
        <Router>
            <Switch>
                <Route path="/home" component={Home} />
            </Switch>
        </Router>
    );
};

export default App;