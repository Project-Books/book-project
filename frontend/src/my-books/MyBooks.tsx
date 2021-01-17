import React, { Component } from 'react'
import { NavBar } from '../shared/navigation/NavBar';
import './MyBooks.css'

interface IState {
}

type MyBooksProps = {
}

class MyBooks extends Component<{}, IState> {
    render() {
        return (
            // <p>My books page</p>
            <NavBar />
        )
    }
}

export default MyBooks;
