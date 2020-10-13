import * as React from 'react';
import {apiGet} from './utilities/request-helper';
import {errorLogAndRedirect} from './error';

export default class Home extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            entities: []
        };
    }

    componentDidMount() {
        this.loadEntities();
    }

    render() {
        return (
            <div className='col-md-8 col-md-offset-2'>
                <h1 style={{textAlign: 'center', marginBottom: '30px'}}>Welcome to Agent Discoveries home page.</h1>
                <div className='timezone-wrapper'>
                    {this.renderTimeZones()}
                </div>
            </div>
        );
    }

    renderTimeZones() {
        let filtered = this.removeDuplicates(this.state.entities, 'timeZone');
        return filtered.map((location) =>
            <div key={location.timeZone} className={'panel panel-default timezone'}>
                <div className='panel-heading'>{location.location}</div>
                <div className='panel-body'>
                    <div className='time'>{this.getTimeFromTimeZone(location.timeZone)}</div>
                    <div className='zone'>{location.timeZone} </div>
                </div>
            </div>
        );
    }

    getTimeFromTimeZone(timeZone) {
        return new Date().toLocaleTimeString('en-US', {timeZone: timeZone});
    }

    removeDuplicates(myArr, prop) {
        return myArr.filter((obj, pos, arr) => {
            return arr.map(mapObj => mapObj[prop]).indexOf(obj[prop]) === pos;
        });
    }

    loadEntities() {
        apiGet(this.props.api)
            .then(results => this.setState({entities: results}))
            .catch(errorLogAndRedirect);
    }
}