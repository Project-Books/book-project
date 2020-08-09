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
   1. [One statement per line](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#one-statement-per-line)
   1. [Empty blocks can be concise](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#empty-blocks-can-be-concise)
   1. [Whitespace](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#whitespace)
      1. [Vertical whitespace](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#vertical-whitespace)
      1. [No horizontal alignment](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#no-horizontal-alignment)
   1. [Optional grouping parentheses: recommended](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#optional-grouping-parentheses-recommended)
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

### Whitespace

#### Vertical whitespace

*Where there should be one blank line*

- Between the end of one method (the closing brace) and the start of another

- Between groups of logical statements. This could be a group of related imports, fields, or statements within a method.

Generally speaking, there should not be more than one blank line.

*Where there should not be one blank line*

- Between the class name and the first field declaration (or method declaration or defintion)

#### No horizontal alignment

```java
// acceptable
private int numberOfPages;
private String authorFirstName;

// unacceptable
private int     numberOfPages;
private Strring authorFirstName;
```

There should be no horizontal alignment. While it looks nice and improves readability, it is can be harder to maintain.

### Optional grouping parentheses: recommended

Optional grouping parentheses are recommended as they can improve readability. It can help when not everyone is clear with certain precedence rules.

### Enum classes

If an enum class has no methods, it can be written as an array initialiser:
```java
private enum EventType { SAVED, DELETED }
```

#### Comment fall-through

If a case continues onto the next statement group with a `break`, this should be commented. Something like  `// fall through` is sufficient. For example:

```java
switch (param) {
   case 1:
   case 2:
      foo();
      // fall through
   case 3:
      bar();
      break;
   default:
      baz();
}
```

The final statement in the switch block, the `default` case above, does not require a fall-through comment.

#### All cases or default cases

Either cover all of the cases or use the `default` case. Covering all cases is the preferred approach.

### Annotations

Annotations should appear after any javadoc for the class, field or method.

Each annotation should appear on its own line. For example:
```java
@Override
@Nullable
public Integer getPagesReadIfPresent() {
   ...
}
```

Hoewver, for fields, multiple annotations can be on the same line:
```java
@NotNull @VisibleForTesting String authorFirstName;
```

### Comments

This section is to do with implementatoin comments, not Javadoc.

Comments should not be decorated with asterisks or any other characters.

### Modifiers

Where applicable, class and member modifiers should appear in the following order, as recommended by the Java Language Specification:
```java
public protected private abstract default static final transient volatile synchronized native strictfp
```

### Numeric literals

#### Underscores in numeric literals

For numeric literals that are ten thousand or higher, underscores are recommended to separate digits by thousands:

```java
// bad
int booksSold = 10000;

// good
int booksSold = 10_000;
```

#### Long suffixes

`long` variables should be suffixed with an uppercase `L`. The lowercase `l` should not be used as it may cause confusion with the digit `1`. For example, `1_000_000_000L` instead of `1_000_000_000l`.


### Variable declarations

#### One variable declaration per line

A variable declaration should appear on its own line. There should not be multiple declarations on one line:
```java
// good
private String authorFirstName;
private String authorLastName;

// bad
private String authorFirstName, authorLastName;
```

#### Declare as close as possible to use

Local variables should be declared as close as reasonably possible to where it is used. This can help limit scope and improve readability. Local variables should not be declared at the top of a method for the sake of it.

### Arrays

#### No C-style array declarations

The square brackets should be next to the type name, not the variable name. This is because they a part of the type.

```java
// good
String[] args

// bad
String args[]
```

### Indentation

#### 4 Spaces, no tabs

4 spaces should be used for indentation. This is clearer than 2 spaces (more than 4 is extraneous).

This also applies to CSS.

#### Aligning method calls

An exception to the above 4 spaces indentation rule is aligning method calls (see below). This can improve readability.

```java
// good
bookGrid.addColumn(AUTHOR_KEY)
        .setSortable(true);

// bad
bookGrid.addColumn(AUTHOR_KEY)
    .setSortable(true);
```

## Naming

Special prefixes or suffixes, such as to represent member variables, are not used.  For example, he following are not permitted: `mTitle` and `_title`.

### Package names

Package names are written in lowercase. Consecutive words are concatenated together without any underscores. For example, `com.example.readinggoal`, not `com.example.readingGoal` or `com.example.reading_goal`. 

To aid readability, try to keep package names short and one-word names wherever possible.

### Class names

Class names are written in PascalCase (also referred to as UpperCamelCase).

Class names should be nouns or noun phrases.

Interface names can be nouns or noun phrases. However, in some cases, adjectives or adjective phrases are better (e.g. `Iterable`).

Test classes should be the name of the class that it is testing followed by the suffix `Test`. For example. the test class for the `GenreStatistics` class should be called `GenreStatisticsTest`.

### Method names

Method names are written in lowerCamelCase.

Method names should usually be verb or verb phrases. For example, `processTransaction` or `findId`.

Underscores are allowed in JUnit tes tmethods to separate logical components. One popular pattern is `<methodUnderTest>_<state>`; for example, `pop_emptyStack`. The most important thing is that the test method name clearly summarises what is being tested.

### Constant names

Constnats are written in CONSTANT_CASE. It is written in uppercase letters with each word separated by an underscore.

### Non-constant field names

Non-constant field names, regardless of whether they are static, are written in`lowerCamelCase`.

The names tend to be noun or noun phrases.

### Parameter names

Paramter names are written in `lowerCamelCase`.

Single letter parameter names should be avoided in methods (common exception: for loops).

### Local variables names

Local variables names are written in `lowerCamelCase`.

Even if they are immutable (e.g. marked as `final`), they are not considered constants, so they not be styles as constants.

### Type variables names

Type variables should be either:
- One capital letter, which can be optionally succeeded by a letter (e.g. `T`, `E` or `T2`)
- A class name followed by the capital letter `T` (e.g. `AuthorT`)

## Javadoc

### No @author tag

Author tags can quickly become outdated as methods are updated or completely written by new authors. Git is far better
at tracking changes.

## Programming practices

### Use @Override

The @Override annotation should be used for overriden methods. This is so that the compiler can run
a check at compile-time to see whether the method annotated with @Override actually overrides a method.

### Don't swallow exceptions

Rarely should there be no response to a caught exception (e.g. you may want to log it).

If it is actually right to not do anything, then this has to be justified in a comment.

An exception to ignore exeptions is if the exception is expected in a test. For example, you may want to test whether the code under test does indeed throw an exception:
```java
try {
   foo(-1);
} catch (IndexOutOfBoundsException expected) {
}
```

In such cases, the exception parameter should be called or include the word `expected`, as above.

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
