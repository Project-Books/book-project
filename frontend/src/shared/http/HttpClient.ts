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

import Endpoints from "../api/endpoints";
import Axios, { AxiosInstance } from "axios";

// eslint-disable-next-line @typescript-eslint/naming-convention
let HttpClient: () => HttpClientBase;
(function () {
  // Creating a singleton instance of an HTTP client
  let instance: HttpClientBase;
  HttpClient = function HttpClient(): HttpClientBase {
    if (instance !== undefined) {
      return instance;
    }
    instance = new HttpClientBase();
    return instance;
  };
})();

// eslint-disable-next-line @typescript-eslint/no-explicit-any
type HttpResponse = Promise<Record<string, any>>;

class HttpClientBase {
  axiosInstance: AxiosInstance;
  constructor() {
    this.axiosInstance = Axios.create({
      baseURL: "http://localhost:8080/",
      headers: {
        // eslint-disable-next-line @typescript-eslint/naming-convention
        "Content-Type": "application/json",
        // eslint-disable-next-line @typescript-eslint/naming-convention
        "Access-Control-Allow-Origin": "*",
      },
    });
  }
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  get(url: string): any {
    return this.axiosInstance(url, {
      method: "GET",
    })
      .then((res) => {
        return res.data;
      })
      .catch((err) => {
        if (Axios.isAxiosError(err)) {
          throw err.response;
        } else {
          throw err;
        }
      });
  }
  login(email: string, password: string): HttpResponse {
    return this.axiosInstance(Endpoints.login, {
      method: "POST",
      headers: {
        // eslint-disable-next-line @typescript-eslint/naming-convention
        Accept: "application/json",
      },
      data: {
        username: email,
        password: password,
      },
    }).then((res) => {
      this.axiosInstance.defaults.headers.common["Authorization"] =
        res.headers["Authorization"];
      return res.data;
    });
  }
  deleteAccount(password: string): HttpResponse {
    return this.axiosInstance(Endpoints.user, {
      method: "DELETE",
      data: {
        password: password,
      },
    }).then((res) => {
      this.axiosInstance.defaults.headers.common["Authorization"] = null;
      return res.data;
    });
  }
}

export default HttpClient();
