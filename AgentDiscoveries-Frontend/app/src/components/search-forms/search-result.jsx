import * as React from 'react';
import {Panel} from 'react-bootstrap';

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
                        <span>Result</span>{this.renderViewReportButton(result['reportId'])}
                    </Panel.Heading>
                    <Panel.Body>{this.renderResultBody(result)}</Panel.Body>
                </Panel>
            );
        });
    }

    renderResultBody(result) {
        return Object.keys(result).map(key => {
            return <p key={key} id={key}>{`${key}: ${this.truncate(result[key], 140)}`}</p>;
        });
    }

    getResultsHeader(results) {
        return results.length > 0
            ? (results.length === 1
                ? <h3>{`${results.length} result`}</h3>
                : <h3>{`${results.length} results`}</h3>)
            : '';
    }

    renderViewReportButton(id){
        return <a target='_blank' href={'/#/admin/locationReport/' + id}><button>View Report</button></a>;
    }

    truncate(string, chars){
        return string.length > chars ? string.substring(0, chars) + '..' : string;
    }
}