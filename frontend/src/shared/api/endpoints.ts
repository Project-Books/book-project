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

export default class Endpoints {
  static readonly login: string = "login";
  static readonly user: string = "api/user";
  static readonly books: string = "api/books";
  static readonly shelf: string = "/api/shelf";
  static readonly didNotFinish: string = "/api/shelf/books/did-not-finish";
  static readonly read: string = "/api/shelf/books/read";
  static readonly reading: string = "/api/shelf/books/reading";
  static readonly toRead: string = "/api/shelf/books/to-read";
}

