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

import React, { useState } from "react";
import "./Goal.css";

import { Layout } from "../shared/components/Layout";
import Modal from "../shared/components/Modal";
export default function Goal(): JSX.Element {
  const [modalState, setModalState] = useState(0);
  const [showModal, setShowModal] = useState(false);
  return (
    <Layout title="Reading goal">
      <div className="current-goal-container">
        <h3 className="open-modal" onClick={() => setShowModal(true)}>
          No goal set..
        </h3>
        <p className="open-modal" onClick={() => setShowModal(true)}>
          Click here to add a new goal
        </p>
        <Modal open={showModal}>
          <div className="goal-modal-inner">
            <h2 className="goal-modal-heading">Add Goal</h2>
            <div className="goal-modal-selector">
              <span
                onClick={() => setModalState(0)}
                className={
                  modalState === 0
                    ? "goal-modal-pages-selector goal-modal-current"
                    : "goal-modal-pages-selector"
                }
              >
                Pages
              </span>
              <span
                onClick={() => setModalState(1)}
                className={
                  modalState === 1
                    ? "goal-modal-books-selector goal-modal-current"
                    : "goal-modal-books-selector"
                }
              >
                Books
              </span>
            </div>
            <div className="goal-modal-input-container">
              <span>I want to read</span>
              <input type="number" min="0" className="goal-modal-input" />
              <span>{modalState === 0 ? "pages" : "books"}</span>
            </div>
            <div className="goal-modal-buttons">
              <button
                onClick={() => setShowModal(false)}
                className="goal-modal-cancel-btn"
              >
                Cancel
              </button>
              <button className="goal-modal-add-btn">Add goal</button>
            </div>
          </div>
        </Modal>
      </div>
    </Layout>
  );
}
