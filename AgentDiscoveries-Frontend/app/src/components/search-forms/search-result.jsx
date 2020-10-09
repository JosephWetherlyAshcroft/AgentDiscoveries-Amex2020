import * as React from 'react';
import {Button, Panel} from 'react-bootstrap';
import Pdf from 'react-to-pdf';


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
                <Panel id={index} key={index}>
                    <Panel.Heading>Result {this.renderDownloadButton(index)}</Panel.Heading>
                    <Panel.Body>{this.renderResultBody(result)}</Panel.Body>
                </Panel>
            );

            // const ref = React.createRef();
            // return (
            //     <Document id={index}>
            //         <Pdf targetRef={ref} filename="code-example.pdf">
            //             {({ toPdf }) => <button onClick={toPdf}>Generate Pdf</button>}
            //         </Pdf>
            //         <div ref={ref} key={index}>
            //             <Panel.Heading>Result </Panel.Heading>
            //             <Panel.Body>{this.renderResultBody(result)}</Panel.Body>
            //         </div>
            //     </Document>
            // );
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
                ? <h3>{`${results.length} result`}</h3>
                : <h3>{`${results.length} results`}</h3>)
            : '';
    }

    downloadReport(that){
        console.log('clicked on ' + that);
    }

    renderDownloadButton() {
        return <button onClick={this.downloadReport.bind(this)}>Download as PDF</button>;
    }



}