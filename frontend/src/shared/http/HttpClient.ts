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

import Verb from "./verb";
import Endpoints from "../api/endpoints";

// eslint-disable-next-line @typescript-eslint/naming-convention
let HttpClient: () => HttpClientBase;
(function() {
    // Creating a singleton instance of an HTTP client
    let instance : HttpClientBase;
    HttpClient = function HttpClient(): HttpClientBase {
        if(instance !== undefined) {
            return instance;
        }
        instance = new HttpClientBase();
        return instance;
    }
})();

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
type HttpReponse = Promise<Record <string,any>>

// Base class for managing HTTP requests
class HttpClientBase {
    mode = "cors";
    cache = "no-cache";
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    headers: any = {
         // eslint-disable-next-line @typescript-eslint/naming-convention
        "Authorization": null,
        // eslint-disable-next-line @typescript-eslint/naming-convention
        "Content-Type": "application/json",
        // eslint-disable-next-line @typescript-eslint/naming-convention
        "Access-Control-Allow-Origin": "*",
    };

    get(url: string): HttpReponse {
        const requestOptions = {
            method:Verb.GET,
            headers: this.headers
        }
        return fetch(url, requestOptions)
            .then(response => response.json())
            .catch((error) => {
                console.log(error);
            })
    }

    login(email: string, password: string):HttpReponse  {
        const requestOptions = {
            method: Verb.POST,
            headers: {
                 // eslint-disable-next-line @typescript-eslint/naming-convention
                Accept: 'application/json',
                // eslint-disable-next-line @typescript-eslint/naming-convention
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: email,
                password: password
            })
        }
        return fetch(Endpoints.login, requestOptions)
        .then(response => {
            const headers = response.headers;
            this.headers['Authorization'] = headers.get('authorization');
            return response;
        })
    }
}

export default HttpClient();

