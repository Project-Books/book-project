import React, { Component, ReactElement } from 'react'
import Endpoints from '../shared/api/endpoints';
import HttpClient from '../shared/http/HttpClient';
import { Book } from '../shared/types/Book';

interface Props {
    match: {
        params: {
            id: number
        }
    }
}

interface IState {
    book: Book;
}

class BookOverview extends Component<Props, IState> {

    constructor(props: Props) {
        super(props);
        this.state = {
            book: {
                id: 0,
                title: "",
                predefinedShelf: {
                    shelfName: ""
                },
                author: {
                    fullName: ""
                },
                bookGenre: [],
                rating: 0
            }
        };
    }

    componentDidMount():void {
        this.getBook();
        console.log(this.state)
    }

    getBook():void {
        HttpClient.get(Endpoints.concreteBook + this.props.match.params.id).then((response: Book) => {
          this.setState({
            book: response
          });
        })
        .catch((error: Record<string, string>) => {
          console.error('error: ', error);
        });
    }

    render(): ReactElement {
        return (
            <div>
            </div>
        )
    }
}

export default BookOverview;