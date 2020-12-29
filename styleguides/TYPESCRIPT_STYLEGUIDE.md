# TypeScript Style guide

By contributing to this repository, you are expected to follow this style guide. Please also ensure that you remain familiar with this document as it may change from time to time.

Multiple versions of this style guide may exist throughout this repository. As with other documents on this repository,
the version on the master branch should be followed, as this version should be the most up-to-date.

This also encompasses our JavaScript style.

## Introduction

Good judgement should be followed. There may be times where it is more readable to not follow a particular guideline.
Readable code is preferred over code that strictly follows this guide.

Where useful, a short summary explaining why the guideline is in place is included to help explain the rationale behind having it.

## Files

### Include copyright notice

All source files should start with our [copyright notice](https://github.com/Project-Books/book-project/blob/react-login-558/COPYRIGHT) attached verbatim.

## Formatting

Lines should generally not exceed our 100 characters column limit. This is to be consistent with our Java style guide.

### Braces

#### One true brace style (1TBS)

We use the [One true brace style (1TBS)](https://en.wikipedia.org/wiki/Indentation_style#Variant:_1TBS_(OTBS)).

```ts
// bad: braces should be used even where it is optional to do so in if, else if, while and do statements
if (condition)
    doSomething();

// good
if (condition) {
    doSomething();
} else {
    doSomethingElse();
}
```

## Language

- Avoid using `var`. Instead, prefer `const` and `let`
- No wildcard imports (so we have one export per file)

## Naming

### Method names

Method names should be written in `camelCase` and be be verb or verb phrases.

### Enum names

Enum names should be written in `PascalCase`. Enum items are written in `CONSTANT_CASE`.

### Parameter names

Parameter names should be written in `camcelCase`.

### Local variable names

Local variable names should be written in `camelCase` even where they are constants.

## Acknowledgements

Adapted from [Google's JavaScript style guide](https://google.github.io/styleguide/jsguide.html) and [Airbnb's JavaScript style guide](https://github.com/airbnb/javascript/blob/master/README.md).
