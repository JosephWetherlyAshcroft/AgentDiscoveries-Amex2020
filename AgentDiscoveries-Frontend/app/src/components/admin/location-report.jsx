import * as React from 'react';
import {Panel} from 'react-bootstrap';
import QueryString from 'query-string';
import {apiGet} from '../utilities/request-helper';

export default class LocationReport extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            id: '2',
            result: {},
            message: ''
        };
    }

    componentWillMount() {
        console.log('started');
        const params = {
            reportId: this.state.id
        };
        const url = 'reports/locationstatuses?' + QueryString.stringify(params);
        apiGet(url)
            .then(results => this.setState({result: results, message: {}}))
            .catch(error => this.setState({message: {message: error.message, type: 'danger'}}));
    }

    render() {
        return (
            <Panel>
                <Panel.Heading>Result <a href="/#/admin/locationReport/3">Download as PDF</a></Panel.Heading>
                <Panel.Body>{this.renderBody(this.state.result)}</Panel.Body>
            </Panel>
        );
    }

    renderBody(result) {
        console.log(result);
    }

}