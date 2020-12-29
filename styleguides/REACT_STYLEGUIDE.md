# React  Style guide

By contributing to this repository, you are expected to follow this style guide. Please also ensure that you remain familiar with this document as it may change from time to time.

Multiple versions of this style guide may exist throughout this repository. As with other documents on this repository,
the version on the master branch should be followed, as this version should be the most up-to-date.

## Introduction

Good judgement should be followed. There may be times where it is more readable to not follow a particular guideline.
Readable code is preferred over code that strictly follows this guide.

Where useful, a short summary explaining why the guideline is in place is included to help explain the rationale behind having it.

## Fundamental rules

- Only have one React component per file
   - But mutliple stateless components are allowed per file
- Always use JSX syntax

## Files

- All source files should start with our [copyright notice](https://github.com/Project-Books/book-project/blob/react-login-558/COPYRIGHT) attached verbatim.

## Class vs. React.createClass

- Use `class extends React.Component` if you have internal state. Otherwise, use regular functions (not arrow functions)

## Naming

- **File extension**: use the `.tsx` extension for React components
- **File names**: Use PascalCase for file names
- **Component names** Use the file name for the component name
- **camelCase prop names**: Use camelCase for prop names
- **Avoid DOM component prop names**: avoid using DOM component prop names for a different purpose

## Ordering

Ordering for a class that extends `React.Component`:

1. `constructor`
2. `componentDidMount`
3. `componentWillUpdate`
4. `componentWillUnmount`
5. `render`
6. Additional render methods (e.g. `renderHeader`)

## Spacing

- Always have one space before the self-closing tag
```jsx
<Component/> // bad

<Component  /> // also bad (more than one space)

<Component /> // good
```

- Do not pad JSX curly braces with spaces

```jsx
<Component props={ something }> // bad 
<Component props={something}> // good
```

## Parentheses

Wrap JSX tags in parentheses if they span more than one line

```jsx
// bad
render() {
    return <Component>
             <ChildComponent />
           </Component>;
}

// good
render() {
    return (
        <Component>
            <ChildComponent />
        </Component>
    )
}

// also good when only one line
render() {
    return <Component>{some text}</Component>
}


```

## Acknowledgments

 Adapted from [Airbnb's React/JSX style guide](https://github.com/airbnb/javascript/tree/master/react).