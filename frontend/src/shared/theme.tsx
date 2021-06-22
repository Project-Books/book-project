/*
The book project lets a user keep track of different books they would like to read, are currently
reading, have read or did not finish.
copyright (c) 2020  karan kumar

this program is free software: you can redistribute it and/or modify it under the terms of the
gnu general public license as published by the free software foundation, either version 3 of the
license, or (at your option) any later version.

this program is distributed in the hope that it will be useful, but without any
warranty; without even the implied warranty of merchantability or fitness for a particular
purpose.  see the gnu general public license for more details.

you should have received a copy of the gnu general public license along with this program.
if not, see <https://www.gnu.org/licenses/>.
*/

import { createMuiTheme } from '@material-ui/core/styles'

export const theme  = createMuiTheme({
    palette: {
        primary: {
            main: "#000000",
        },
        type:'light'
    },
    overrides: {
        // eslint-disable-next-line @typescript-eslint/naming-convention
        MuiButton: {
            root: {
                borderRadius: 100,
                textTransform: "none"
            }
        }
    }
})

export const darkTheme = createMuiTheme({
    palette: {
      background: {
        default: "#fafafa",
      },
      type: 'dark',
    },
    overrides: {
        // eslint-disable-next-line @typescript-eslint/naming-convention
        MuiButton: {
            root: {
                borderRadius: 100,
                textTransform: "none"
            },
        },
    }
  });
