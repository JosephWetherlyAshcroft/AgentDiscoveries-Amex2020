import * as React from 'react';
import {Button, ControlLabel, Form, FormControl, FormGroup} from 'react-bootstrap';
import QueryString from 'query-string';
import moment from 'moment';
import Message from '../message';
import SearchResult from './search-result';
import {apiGet} from '../utilities/request-helper';

export default class LocationReportsSearch extends React.Component {
    constructor(props) {
        super(props);
        let resultsPerPageDefault = 4;

        this.state = {
            locations: [],
            callSigns: [],

            callSign: '',
            locationId: '',
            fromTime: '',
            toTime: '',

            results: [],
            resultsPerPage: resultsPerPageDefault,
            resultsRangeFrom0: [0, resultsPerPageDefault - 1],
            message: {}
        };

        this.onCallSignChange = this.onCallSignChange.bind(this);
        this.onLocationChange = this.onLocationChange.bind(this);
        this.onFromChange = this.onFromChange.bind(this);
        this.onToChange = this.onToChange.bind(this);
        this.onLocationChange = this.onLocationChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.loadNextPage = this.loadNextPage.bind(this);
        this.loadPreviousPage = this.loadPreviousPage.bind(this);


    }

    componentWillMount() {
        apiGet('locations')
            .then(results => this.setState({locations: results}))
            .catch(() => this.addMessage('Error fetching locations, please try again later', 'danger'));
        apiGet('agents')
            .then(results => this.setState({callSigns: results}))
            .catch(() => this.addMessage('Error fetching locations, please try again later', 'danger'));
    }

    render() {
        return (
            <div className='col-md-8 col-md-offset-2'>
                <Form onSubmit={this.onSubmit}>
                    <h3>Search Location Reports</h3>

                    <Message message={this.state.message}/>

                    <FormGroup>
                        <ControlLabel>Call Sign</ControlLabel>
                        <FormControl componentClass='select' required
                                     value={this.state.callSign}
                                     onChange={this.onCallSignChange}
                                     id='callSign-select'>
                            <option value='' hidden>Choose a call sign</option>
                            {this.state.callSigns.map(callSign =>
                                <option key={callSign.callSign}
                                        value={callSign.callSign}>{callSign.callSign}, {callSign.firstName}</option>)}
                        </FormControl>
                    </FormGroup>
                    <FormGroup>
                        <ControlLabel>Location</ControlLabel>
                        <FormControl componentClass='select' required
                                     value={this.state.locationId}
                                     onChange={this.onLocationChange}
                                     id='location-select'>
                            <option value='' hidden>Choose a location</option>
                            {this.state.locations.map(location =>
                                <option key={location.locationId}
                                        value={location.locationId}>{location.location}, {location.siteName}</option>)}
                        </FormControl>
                    </FormGroup>
                    <FormGroup className='form-inline'>
                        <ControlLabel className='rm-3'>From</ControlLabel>
                        <FormControl className='rm-3' type='date'
                                     value={this.state.fromTime}
                                     onChange={this.onFromChange}/>

                        <ControlLabel className='rm-3'>To</ControlLabel>
                        <FormControl className='rm-3' type='date'
                                     value={this.state.toTime}
                                     onChange={this.onToChange}/>
                    </FormGroup>
                    <Button type='submit'>Search</Button>
                    <div className="previous_nextButtons">
                        <Button id='previousLotOfResultsButton'
                                onClick={this.loadPreviousPage}>Previous {this.state.miroTest}</Button>
                        <Button id='nextLotOfResultsButton'
                                onClick={this.loadNextPage}>Next {this.state.miroTest}</Button>
                    </div>
                </Form>
                <SearchResult results={this.state.results}/>
            </div>
        );
    }

    onCallSignChange(event) {
        this.setState({callSign: event.target.value});
    }

    onLocationChange(event) {
        this.setState({locationId: event.target.value && parseInt(event.target.value)});
    }

    onFromChange(event) {
        this.setState({fromTime: event.target.value});
    }

    onToChange(event) {
        this.setState({toTime: event.target.value});
    }

    onSubmit(event) {
        event.preventDefault();
        console.log("Miro url params: ", this.getSearchParametersFromState());
        const url = 'reports/locationstatuses?' + QueryString.stringify(this.getSearchParametersFromState());
        apiGet(url)
            .then(results => {
                //check the length of results, if < max, hide "next" button
                console.log("numberOfResults received from backend: ", results.length);
                if (results.length === this.state.resultsPerPage) document.querySelector('#nextLotOfResultsButton').style.visibility = 'visible';
                this.setState({results: results, message: {}})
            })
            .catch(error => this.setState({message: {message: error.message, type: 'danger'}}));
    }

    loadNextPage(event) {
        event.preventDefault();
        this.setState(previousState => ({
            resultsRangeFrom0: [
                previousState.resultsRangeFrom0[0] + previousState.resultsPerPage,
                previousState.resultsRangeFrom0[1] + previousState.resultsPerPage
            ]
        }), () => {
            const url = 'reports/locationstatuses?' + QueryString.stringify(this.getSearchParametersFromState());
            apiGet(url)
                .then(results => {
                    if (results.length < this.state.resultsPerPage) document.querySelector('#nextLotOfResultsButton').style.visibility = 'hidden';
                    document.querySelector('#previousLotOfResultsButton').style.visibility = 'visible';
                    if(results.length===0) {
                        return;
                    } else {
                        this.setState({results: results, message: {}})
                    }
                })
                .catch(error => this.setState({message: {message: error.message, type: 'danger'}}));
        });
    }

    loadPreviousPage(event) {
        event.preventDefault();
        this.setState(previousState => ({
            resultsRangeFrom0: [
                previousState.resultsRangeFrom0[0] - previousState.resultsPerPage,
                previousState.resultsRangeFrom0[1] - previousState.resultsPerPage
            ]
        }), () => {
            if (this.state.resultsRangeFrom0[0] <= 0) document.querySelector('#previousLotOfResultsButton').style.visibility = 'hidden';
            if (this.state.resultsRangeFrom0[0] < 0) return;
            const url = 'reports/locationstatuses?' + QueryString.stringify(this.getSearchParametersFromState());
            apiGet(url)
                .then(results => {
                    if (this.state.results.length > 0) document.querySelector('#nextLotOfResultsButton').style.visibility = 'visible';
                    else document.querySelector('#nextLotOfResultsButton').style.visibility = 'hidden';
                    this.setState({results: results, message: {}})
                })
                .catch(error => this.setState({message: {message: error.message, type: 'danger'}}));
        });
    }

    getSearchParametersFromState() {
        return ({
            callSign: this.state.callSign,
            locationId: this.state.locationId,
            fromTime: this.state.fromTime && moment.utc(this.state.fromTime).startOf('day').toISOString(),
            toTime: this.state.toTime && moment.utc(this.state.toTime).endOf('day').toISOString(),
            resultsRange: `${this.state.resultsRangeFrom0[0]}-${this.state.resultsRangeFrom0[1]}`
        });
    }
}
