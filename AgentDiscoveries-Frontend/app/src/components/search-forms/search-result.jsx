import * as React from 'react';
import {Panel} from 'react-bootstrap';
import {jsPDF} from 'jspdf';

export default class SearchResult extends React.Component {
    render() {
        return (
            <div className='results'>
                {this.getResultsHeader(this.props.results)}
                {this.renderResults(this.props.results)}
            </div>
        );
    }

    renderResults(results) {
        return results.map((result, index) => {
            return (
                <Panel key={index}>
                    <Panel.Heading className='search-panel-heading'>
                        <span>Result</span> {this.renderDownloadButton(this)}
                    </Panel.Heading>
                    <Panel.Body>{this.renderResultBody(result)}</Panel.Body>
                </Panel>
            );
        });
    }

    renderResultBody(result) {
        return Object.keys(result).map(key => {
            return <p key={key} id={key}>{`${key}: ${result[key]}`}</p>;
        });
    }

    getResultsHeader(results) {
        return results.length > 0
            ? (results.length === 1
                ? <h3>Result</h3>
                : <h3>Results</h3>)
            : '';
    }

    downloadReport(event) {
        let doc = new jsPDF();
        let start = 10;
        let reportFields = event.target.parentElement.parentElement.getElementsByClassName('panel-body')[0].childNodes;
        reportFields.forEach(child => doc.text(child.innerHTML, 10, start = start + 10));
        doc.save('report.pdf');
    }

    renderDownloadButton(e) {
        return <button onClick={this.downloadReport.bind(e)}>Download as PDF</button>;
    }


}