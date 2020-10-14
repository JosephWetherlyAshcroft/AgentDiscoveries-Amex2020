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

        this.state = {
            title:'',
            callSign: '',
            locationId: '',
            fromTime: '',
            toTime: '',

            results: [],
            message: {}
        };

        this.onTitleChange = this.onTitleChange.bind(this);
        this.onCallSignChange = this.onCallSignChange.bind(this);
        this.onLocationChange = this.onLocationChange.bind(this);
        this.onFromChange = this.onFromChange.bind(this);
        this.onToChange = this.onToChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    render() {
        return (
            <div className='col-md-8 col-md-offset-2'>
                <Form onSubmit={this.onSubmit}>
                    <h3>Search Location Reports</h3>

                    <Message message={this.state.message} />
                    <FormGroup>
                        <ControlLabel>Title</ControlLabel>
                        <FormControl type='text'
                            placeholder='Enter location report title'
                            value={this.state.title}
                            onChange={this.onTitleChange}/>
                    </FormGroup>

                    <FormGroup>
                        <ControlLabel>Agent Call Sign</ControlLabel>
                        <FormControl type='text'
                            placeholder='Enter agent Call Sign'
                            value={this.state.callSign}
                            onChange={this.onCallSignChange}/>
                    </FormGroup>
                    <FormGroup>
                        <ControlLabel>Location</ControlLabel>
                        <FormControl type='number'
                            placeholder='Enter location ID'
                            value={this.state.locationId}
                            onChange={this.onLocationChange}/>
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
                </Form>
                <SearchResult results={this.state.results} />
            </div>
        );
    }
    onTitleChange(event){
        this.setState({title: event.target.value });
    }

    onCallSignChange(event) {
        this.setState({ callSign: event.target.value });
    }

    onLocationChange(event) {
        this.setState({ locationId: parseInt(event.target.value) });
    }

    onFromChange(event) {
        this.setState({ fromTime: event.target.value });
    }

    onToChange(event) {
        this.setState({ toTime: event.target.value });
    }

    onSubmit(event) {
        event.preventDefault();
        const params = {
            title: this.state.title,
            callSign: this.state.callSign,
            locationId: this.state.locationId,
            fromTime: this.state.fromTime && moment.utc(this.state.fromTime).startOf('day').toISOString(),
            toTime: this.state.toTime && moment.utc(this.state.toTime).endOf('day').toISOString()
        };
        const url = 'reports/locationstatuses?' + QueryString.stringify(params);
        apiGet(url)
            .then(results => this.setState({ results: results, message: {} }))
            .catch(error => this.setState({message: {message: error.message, type: 'danger'}}));
    }
}
