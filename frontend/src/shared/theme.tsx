import { createMuiTheme } from '@material-ui/core/styles'

export const theme  = createMuiTheme({
    palette: {
        primary: {
            main: "#000000",
        }
    },
    overrides: {
        MuiButton: {
            root: {
                borderRadius: "100px 100px 100px 100px",
                textTransform: "none"
            }
        }
    }
})
