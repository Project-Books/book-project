/*
The book project lets a user keep track of different books they would like to read, are currently
reading, have read or did not finish.
Copyright (C) 2020  Karan Kumar

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

export default class ShelfModal extends Component<IModalProps> {
  constructor(props: never) {
    super(props);
    this.state = { value: "" };
    this.submitShelf = this.submitShelf.bind(this);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange = (event: any) => {
    this.setState({ value: event.target.value });
  };

  submitShelf = () => {
    this.setState({ value: "" });
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
                  <p>Shelf name</p>
                  <TextField
                    className="shelfInput"
                    size="small"
                    id="outlined-basic"
                    variant="outlined"
                    onChange={this.handleChange}
                  />
                </Hidden>
                <Hidden mdUp implementation="css">
                  <TextField
                    className="shelfInput"
                    size="small"
                    id="name"
                    variant="outlined"
                    label="shelf name"
                    onChange={this.handleChange}
                  />
                </Hidden>
              </div>
            </div>
            <div className="modal-form-spacer" />

            <div className="shelf-button-container">
              <Button
                className="shelf-modal-button"
                variant="contained"
                onClick={this.props.onClose}
              >
                Cancel
              </Button>
              <Button
                className="shelf-modal-button"
                variant="contained"
                onClick={this.submitShelf}
                color="primary"
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
