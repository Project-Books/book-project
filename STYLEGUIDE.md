# Style guide

By contributing to this repository, you are expected to follow this style guide. Please also ensure that you remain familiar with this document as it may change from time to time.

Multiple versions of this styleguide may exist throughout this repository. As with other documents on this repository,
the version on the master branch should be followed, as this version should be the most up-to-date.

## Table of Contents

1. [Introduction](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#introduction)
1. [IDE formatting](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#ide-formatting)
   1. [IntelliJ](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#intellij)
   1. [Eclipse](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#eclipse)
1. [Checking for violations](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#checking-for-violations)
1. [Source files](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#source-files)
   1. [Source file structure](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#source-file-structure)
      1. [No wildcard imports](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#no-wildcard-imports)
      1. [No line-wrapping for package & import statements](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#no-line-wrapping-for-package-and-import-statements)
   1. [Fields at the top](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#fields-at-the-top)
   1. [Overloads together](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#overloads-together)
   1. [Newline at end of file](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#newline-at-end-of-file)
1. [Formatting](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#formatting)
   1. [Braces](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#braces)
   1. [120 character column limit](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#120-character-column-limit)
   1. [One statement per line](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#120-character-column-limit)
   1. [Underscores in numeric literals](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#underscores-in-numeric-literals)
   1. [Indentation](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#indentation)
      1. [4 Spaces, no tabs](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#4-spaces-no-tabs)
      1. [Aligning method calls](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#aligning-method-calls)
1. [Javadoc](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#javadoc)
   1. [No @author tag](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#no-author-tag)
1. [Programming practices](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#programming-practices)
   1. [Use @Override](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#use-override)
   1. [StringBuilder over StringBuffer](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#stringbuilder-over-stringbuffer)
   1. [Overriding hashCode() and equals()](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#overriding-hashcode-and-equals)
   1. [Overriding toString()](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#overriding-tostring)
   1. [Short methods](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#short-methods)
   1. [JUnit](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#junit)
      1. [Fewest number of assertions in every test](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#fewest-number-of-assertions-in-every-test)
1. [Recommended reading](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#recommended-reading)
1. [Updates to this document](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#updates-to-this-document)

## Introduction

Good judgement should be followed. There may be times where it is more readable to not follow a particular guideline.
Readable code is preferred over code that strictly follows this guide.

A short summary explaining why the guideline is in place is included to help explain the rationale behind having it.

## IDE formatting

IDE-specific code style files have been exported and can be imported into your IDE. The files are located in the
[ide](https://github.com/knjk04/book-project/tree/master/ide) directory.

### IntelliJ

<p align="center">
  <img src="/media/intellij_code_style.png" alt="Import IntelliJ code style file"/>
</p>

For IntelliJ, you can use import the [code style file](https://github.com/knjk04/book-project/blob/master/ide/intellij/book_project_code_style.xml).
Go to File > Settings > Editor > Code Style > Java in Linux or Windows. Click on the settings cog and choose 
Import Scheme > IntelliJ IDEA code style XML. 

### Eclipse

<p align="center">
  <img src="/media/eclipse_code_style.png" alt="Import Eclipse code style file"/>
</p>

If you use Eclipse, you can import the formatter file by going to Window > Preferences > Java > Code Style > Formatter
and selecting the import button to import the [Eclipse code style file](https://github.com/knjk04/book-project/blob/master/ide/eclipse/book_project_formatter_profile.xml).

## Checking for violations

We're currently using Checkstyle to check for style guide violations. To o check for any violations, you can run the following command in the root directory of the project:

```
$ ./mvnw validate
```

By using the maven wrapper, you don't need to have Maven installed. Alternatively, you could run `mvn validate` if you do have Maven installed.

## Source files

### Source file structure

A source file should contain the following in order:

1. License notice and copyright information
2. Package statement
3. Import statements
4. Exactly one top-level class

There should be exactly one blank line to separate every section.

#### No wildcard imports

Wildcard imports, regardless of whether they are static, should not be used:
```java
// good
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;

// bad
import com.vaadin.flow.component.*;
```

#### No line-wrapping for package and import statements

The package statement and import statements should not be line-wrapped.

### Fields at the top

All fields should appear at the top of the class before any constructors. Fields should not be interspersed between methods.

### Overloads together

Overloaded methods, including constructors, should appear together with nothing in between. 

### Newline at end of file

Every source file should have one newline at the end of the file.

## Formatting

### Braces

#### One true brace style (1TBS)

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

### 120 character column limit

A line should generally not exceed 120 characters (an IntelliJ default).

Why 120 characters, not 100 characters (like the Google Java style guide)? You can have two files side by side that do not exceed 120 characters on a 1920x1080 display (at least in IntelliJ).

Exceptions to this rule:
- `package` and `import` statements
- Long URL in Javadoc

### One statement per line

Every statement should be followed by a line break. This includes variable declarations.

### Underscores in numeric literals

For numeric literals that are ten thousand or higher, underscores are recommended to separate digits by thousands:

```java
// bad
int booksSold = 10000;

// good
int booksSold = 10_000;
```

### Empty blocks can be concise

Empty blocks can be concise providing they do not form a part of a multi-block statement.

```java
// fine
void foo() {}

// also fine
void bar() {
}

// not fine
if (condition) {
  foo();
} else { }
```

This applies to `if/else` and `try/catch/finally` blocks.

## Indentation

### 4 Spaces, no tabs

4 spaces should be used for indentation. This is clearer than 2 spaces (more than 4 is extraneous).

This also applies to CSS.

### Aligning method calls

An exception to the above 4 spaces indentation rule is aligning method calls (see below). This can improve readability.

```java
// good
bookGrid.addColumn(AUTHOR_KEY)
        .setSortable(true);

// bad
bookGrid.addColumn(AUTHOR_KEY)
    .setSortable(true);
```

## Javadoc

### No @author tag

Author tags can quickly become outdated as methods are updated or completely written by new authors. Git is far better
at tracking changes.

## Programming practices

### Use @Override

The @Override annotation should be used for overriden methods. This is so that the compiler can run
a check at compile-time to see whether the method annotated with @Override actually overrides a method.

### StringBuilder over StringBuffer

StringBuilder should be used instead of a StringBuffer for single-threaded code.

### Overriding hashCode() and equals()

If you override `hashCode()`, it is good practice to also override `equals()`.

The overrides of `equals()` and `hashCode()` should be equivalent; if `x.equals(y)` is true, then `x.hashCode()` should have
the same value as `y.hashCode()`.

### Overriding toString()

It is sometimes worthwhile overriding `toString()` for logging purposes.

```java
// OK, but could be better. The hardcoded class name has to be changed if the class name changes
@Override
public String toString() {
    return "Book[title= " + title
        + ", published = " + published
        + "]" ;
}

// Better
@Override
public String toString() {
    return getClass.getName()
        + "[title= " + title
        + ", published = " + published
        + "]" ;
}
```

### Short methods

Wherever possible, try to keep methods short (under 15 lines). This makes it easier to test, comprehend and reuse.

### JUnit

### Fewest number of assertions in every test

In every test method, try to minimise the number of assertions. Generally speaking, ideally there should only be one.

## Recommended reading

- Clean code, Robert C. Martin

## Updates to this document

If you are considering making changes to any of the recommended styles in this guide, please note that the following may also need to be changed:

- [Checkstyle configuration](https://github.com/knjk04/book-project/blob/master/src/main/resources/checkstyle.xml)
- [IntelliJ formatter](https://github.com/knjk04/book-project/blob/master/ide/intellij/book_project_code_style.xml)
- [Eclipse formatter](https://github.com/knjk04/book-project/blob/master/ide/eclipse/book_project_formatter_profile.xml)

In addition, please update the [table of contents](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#contents).

## Acknowledgements

This style guide has been adapted from [Google's Java style guide](https://google.github.io/styleguide/javaguide.html) and 
[Twitter's common style guide](https://github.com/twitter-archive/commons/blob/master/src/java/com/twitter/common/styleguide.md).
