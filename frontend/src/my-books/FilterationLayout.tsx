import React from 'react'
import { 
    FormControl,
    InputLabel,
    OutlinedInput,
    InputAdornment,
    IconButton 
} from "@material-ui/core";
import SearchIcon from '@material-ui/icons/Search';
import FilterListIcon from '@material-ui/icons/FilterList';
import './FilterationLayout.css'

type FilterProps = {
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void
}

export function FilterationLayout(props: FilterProps): JSX.Element {
    return (
        <div className="rounded">
            <FormControl variant="outlined" className="filter">
                <InputLabel htmlFor="oneThing">Filter</InputLabel>
                <OutlinedInput
                    id="oneThing"
                    label="Search by book title"
                    type="text"
                    disabled
                    endAdornment={
                    <InputAdornment position="end">
                    <IconButton
                        aria-label="filter"
                        size="small"
                    >
                        <FilterListIcon></FilterListIcon>
                    </IconButton>
                    </InputAdornment>
                    }
                />
            </FormControl>
            <FormControl variant="outlined" className="search">
                <InputLabel htmlFor="outlined-adornment-password">Search by book title</InputLabel>
                <OutlinedInput
                    id="outlined-adornment-password"
                    label="Search by book title"
                    type="text"
                    onChange={props.onChange}
                    className="search-text"
                    endAdornment={
                    <InputAdornment position="end">
                    <IconButton
                        aria-label="search"
                        size="small"
                        style={{paddingRight: '10px'}}
                    >
                        <SearchIcon></SearchIcon>
                    </IconButton>
                    </InputAdornment>
                    }
                />
            </FormControl>   
        </div>
    );
}