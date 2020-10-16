import * as React from 'react';
import {Panel} from 'react-bootstrap';
import QueryString from 'query-string';
import {apiGet} from '../utilities/request-helper';
import {jsPDF} from 'jspdf';

export default class LocationReport extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            result: {},
            message: ''
        };

        if (this.props.id) {
            this.loadReport(this.props.id);
        }
    }

    render() {
        return (
            <div className='col-md-8 col-md-offset-2'>
                <Panel>
                    <Panel.Heading className='search-panel-heading'>
                        <span>Report</span> {this.renderDownloadButton(this)}
                    </Panel.Heading>
                    <Panel.Body>{this.renderBody(this.state.result)}</Panel.Body>
                </Panel>
            </div>
        );
    }

    renderBody(result) {
        if (result[0]) {
            return Object.keys(result[0]).map(key => {
                return <p key={key} id={key}>{`${key}: ${result[0][key]}`}</p>;
            });
        }
    }

    downloadReport(event) {
        let doc = new jsPDF();
        let offset = 10;
        let reportFields = event.target.parentElement.parentElement.getElementsByClassName('panel-body')[0].childNodes;

        reportFields.forEach(field => {
            let lines = doc.splitTextToSize(field.innerHTML, 180);
            doc.text(lines, 10, offset = offset + 10);
            if (lines.length > 1) {
                offset += 6 * lines.length;
            }
        });

        doc.save('report.pdf');
    }

    renderDownloadButton(e) {
        return <button onClick={this.downloadReport.bind(e)}>Download as PDF</button>;
    }

    loadReport(id) {
        const params = {
            reportId: id
        };
        const url = 'reports/locationstatuses?' + QueryString.stringify(params);
        apiGet(url)
            .then(results => this.setState({result: results, message: {}}))
            .catch(error => this.setState({message: {message: error.message, type: 'danger'}}));
    }
}