/*
The book project lets a user keep track of different books they would like to read, are currently
reading, have read or did not finish.
Copyright (C) 2021  Karan Kumar

This program is free software: you can redistribute it and/or modify it under the terms of the
GNU General Public License as published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.
If not, see <https://www.gnu.org/licenses/>.
*/

import React, { Component } from "react";
import Modal, { IModalProps } from "../shared/components/Modal";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import "./ShelfModal.css";
import Hidden from "@material-ui/core/Hidden";

type MyState = { name: string };
export default class ShelfModal extends Component<IModalProps, MyState> {
    constructor(props: never) {
        super(props);
        this.state = { name: "" };
        this.submitShelf = this.submitShelf.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange = (event: any) => {
        this.setState({ name: event.target.value });
    };

    submitShelf = (event: any) => {
        event.preventDefault();
    };
    render(): JSX.Element {
        return (
            <div>
                <Modal open={this.props.open} onClose={this.props.onClose}>
                    <div className="shelf-modal-container">
                        <div className="modal-content">
                            <div className="modal-title">Add shelf</div>
                            <div className="shelf-modal-desc-container">
                                <Hidden smDown implementation="css">
                                    <div className="shelf-modal-desc-items">
                                        <p>Shelf name</p>
                                        <TextField
                                            className="shelfInput"
                                            size="small"
                                            id="outlined-basic"
                                            variant="outlined"
                                            value={this.state.name}
                                            onChange={this.handleChange}
                                        />
                                    </div>
                                </Hidden>
                                <Hidden mdUp implementation="css">
                                    <div className="shelf-modal-desc-items">
                                        <TextField
                                            className="shelfInput"
                                            size="small"
                                            id="name"
                                            variant="outlined"
                                            label="shelf name"
                                            value={this.state.name}
                                            onChange={this.handleChange}
                                        />
                                    </div>
                                </Hidden>
                            </div>
                        </div>
                        <div className="modal-form-spacer" />

                        <div className="shelf-button-container">
                            <Button
                                className="shelf-modal-button"
                                variant="contained"
                                onClick={this.props.onClose}
                                disableElevation
                            >
                                Cancel
                            </Button>
                            <Button
                                className="shelf-modal-button"
                                variant="contained"
                                onClick={this.submitShelf}
                                color="primary"
                                disableElevation
                            >
                                Add shelf
                            </Button>
                        </div>
                    </div>
                </Modal>
            </div>
        );
    }
}
