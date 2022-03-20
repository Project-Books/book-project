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

import React from 'react'
import { Layout } from '../shared/components/Layout';
import { LibraryBooks, MenuBookSharp, Star, StarOutlined } from '@material-ui/icons';
import './Stats.css'
import { ThumbUp } from '@material-ui/icons';


function Stats(): JSX.Element {
    return (
        <Layout title="Statistics" centered={true}>
            <div className="books-list">
                <div className='book-container'>
                    <h3 className='book-title'>Most liked book</h3>
                    <img src="https://inliterature.net/wp-content/uploads/2014/04/harry-potter-1-709x1024.jpg" className='image' />
                    <div className="book-rating">
                        <StarOutlined className='star-icon' fontSize='small' />
                        <p className='rating-number'>8</p>
                    </div>
                </div>
                <div className='book-container'>
                    <h3 className='book-title'>Least liked book</h3>
                    <img src="https://inliterature.net/wp-content/uploads/2014/04/harry-potter-1-709x1024.jpg" className='image' />
                    <div className="book-rating red">
                        <StarOutlined className='star-icon' fontSize='small' />
                        <p className='rating-number'>3</p>
                    </div>
                </div>
                <div className='book-container'>
                    <h3 className='book-title'>Longest book read</h3>
                    <img src="https://inliterature.net/wp-content/uploads/2014/04/harry-potter-1-709x1024.jpg" className='image' />
                    <div className="book-rating black">
                        <p className='rating-number'>628 pages</p>
                    </div>
                </div>
            </div>

            <div className="statistics-container">
                <div className='stat-container'>
                    <MenuBookSharp fontSize='large' />
                    <div className="content">
                        <h5 className='content-heading'>Most read genre</h5>
                        <p className='content-desc'>biography</p>
                    </div>
                </div>
                <div className='stat-container'>
                    <Star fontSize='large' />
                    <div className="content">
                        <h5 className='content-heading'>Average rating given</h5>
                        <p className='content-desc'>4.5/5</p>
                    </div>
                </div>
                <div className='stat-container'>
                    <ThumbUp fontSize='large' />
                    <div className="content">
                        <h5 className='content-heading'>Most liked genre</h5>
                        <p className='content-desc'>biography</p>
                    </div>
                </div>
                <div className='stat-container'>
                    <LibraryBooks fontSize='large' />
                    <div className="content">
                        <h5 className='content-heading'>Average pages read</h5>
                        <p className='content-desc'>368 pages</p>
                    </div>
                </div>
            </div>
        </Layout>
    )
}

export default Stats;
