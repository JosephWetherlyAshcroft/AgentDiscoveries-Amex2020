import React from 'react';
import {Button} from 'react-bootstrap';
import Link from 'react-router-dom/Link';

export default class Entity extends React.Component {
    constructor (props) {
        super(props);

        // Assume that the first JSON property is the ID property
        this.id = Object.values(props.entity)[0];
    }

    render() {
        return (
            <tr key={this.id}>
                {this.getEntityRow()}
                <td key='edit'>
                    {this.getEditButton()}
                </td>
            </tr>
        );
    }

    getEntityRow() {
        return Object.keys(this.props.entity).map(key =>
            <td key={key}>{this.props.entity[key].toString()}</td>);
    }

    getEditButton() {
        console.log('Miro001');
        console.log(this.props.type);
        console.log(this.id);
        return (
            <Link to={`/admin/${this.props.type}/edit/${this.id}`}>
                <Button type='button'>Edit</Button>
            </Link>
        );
    }
}
