# Java style guide

*Multiple versions of this styleguide may exist throughout this repository. As with other documents on this repository,
the version on the master branch should be followed, as this version should be the most up-to-date.*

Good judgement should be followed. There may be times where it is more readable to not follow a particular guideline.
Readable code is preferred over code that strictly follows this guide.

In addition to stating the guidelines, a short summary explaining why the guideline is there is included to explain
why it exists.

This is an adaptation of [Google's Java style guide]() and [Twitter's common style guide]().

**Source file structure**

A source file should contain the following in order:

- License notice
- Package statement
- Imports
- Class

## Formatting

**Braces** 

We use the [One true brace style (1TBS)](https://en.wikipedia.org/wiki/Indentation_style#Variant:_1TBS_(OTBS)).

```java
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

**One statement per line**


## Naming

### Class names

Class names should follow lowerCamelCase.

### Method names

Method names should follow lowerCamelCase.

### Constant names

Constant names should follow CONSTANT_CASE.

### Parameter names

Parameter names should follow lowerCamelCase.

### Local variable names

Local variable names should follow lowerCamelCase.

- Avoid unicode

## Imports
**No wildcard imports** 

Wildcard imports can make it can be hard to identify where a particular import came from. 

```java
// bad: not clear where Entity originated from
import javax.persistence.*;

// good
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
```

*[How to turn off wildcard imports in IntelliJ](https://www.jetbrains.com/help/idea/creating-and-optimizing-imports.html#)*

## Javadoc

**No author tag**

Code may be changed by different people, and so having an author tag could quickly become outdated.
Version control is better at tracking changes.

## Programming practices

**Use @Override"
