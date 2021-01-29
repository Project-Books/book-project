import { createMuiTheme } from '@material-ui/core/styles'

export const theme  = createMuiTheme({
    palette: {
        primary: {
            main: "#000000",
        }
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
