# Style guide

By contributing to this repository, you are expected to follow this style guide. Please also ensure that you remain familiar with this document as it may change from time to time.

Multiple versions of this style guide may exist throughout this repository. As with other documents on this repository,
the version on the master branch should be followed, as this version should be the most up-to-date.

## Table of Contents

1. [Introduction](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#introduction)
2. [IDE formatting](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#ide-formatting)
   1. [IntelliJ](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#intellij)
   2. [Eclipse](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#eclipse)
3. [Checking for violations](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#checking-for-violations)
4. [Source files](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#source-files)
   1. [American English](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#use-american-english)
   2. [Source file structure](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#source-file-structure)
      1. [No wildcard imports](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#no-wildcard-imports)
      2. [No line-wrapping for package & import statements](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#no-line-wrapping-for-package-and-import-statements)
   3. [Fields at the top](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#fields-at-the-top)
   4. [Overloads together](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#overloads-together)
   5. [Newline at end of file](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#newline-at-end-of-file)
5. [Formatting](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#formatting)
   1. [Braces](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#braces) 
      1. [One true brace style (1TBS)](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#one-true-brace-style-1TBS) 
   2. [100 character column limit ](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#100-character-column-limit)
   3. [One statement per line](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#one-statement-per-line)
   4. [Empty blocks can be concise](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#empty-blocks-can-be-concise)
   5. [Whitespace](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#whitespace)
      1. [Vertical whitespace](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#vertical-whitespace)
      2. [No horizontal alignment](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#no-horizontal-alignment)
   6. [Optional grouping parentheses: recommended](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#optional-grouping-parentheses-recommended)
   7. [Enum classes](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#enum-classes)
      1. [Comment fall-through](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#comment-fall-through)
      2. [Enumerate all cases or have a default case](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#enumerate-all-cases-or-have-a-default-case)
   8. [Annotations](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#annotations)
   9. [Comments](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#comments)
      1.  [Do you need the comment?](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#do-you-need-the-comment)
   10. [Modifiers](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#modifiers)
   11. [Numeric literals](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#numeric-literals)
       1. [Underscores in numeric literals](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#underscores-in-numeric-literals)
       2. [Long suffixes](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#long-suffixes)
   12. [Variable declarations](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#variable-declarations)
       1. [One variable declaration per line](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#one-variable-declaration-per-line)
       2. [Declare as close as possible to use](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#declare-as-close-as-possible-to-use)
   13. [Arrays](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#arrays)
       1. [No C-style array declarations](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#no-c-style-array-declarations)
   14. [Indentation](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#indentation)
       1. [4 Spaces, no tabs](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#4-spaces-no-tabs)
       2. [Aligning method calls](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#aligning-method-calls)
6. [Naming](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#naming)
    1. [Package names](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#package-names)
    2. [Class names](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#class-names)
    3. [Method names](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#method-names)
    4. [Constant names](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#constant-names)
    5. [Non-constant field names](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#non-constant-field-names)
    6. [Parameter names](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#parameter-names)
    7. [Local variable names](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#local-variables-names)
    8. [Type variable names](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#type-variables-names)
7. [Javadoc](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#javadoc)
   1. [No @author tag](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#no-author-tag)
8. [Programming practices](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#programming-practices)
   1. [Annotations](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#annotations-1)
      1. [Use @Override](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#use-override)
      2. [Use @VisibleForTesting](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#use-visiblefortesting)
   2. [Don't swallow exceptions](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#dont-swallow-exceptions)
   3. [StringBuilder over StringBuffer](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#stringbuilder-over-stringbuffer)
   4. [Overriding hashCode() and equals()](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#overriding-hashcode-and-equals)
   5. [Overriding toString()](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#overriding-tostring)
   6. [Short methods](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#short-methods)
   7. [TODOs](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#todos)
   8. [Prefer Optional over null](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#prefer-optional-over-null)
   9. [Favor EnumMap over HashMap](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#favor-enummap-over-hashmap)
9. [JUnit](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#junit)
   1. [Given/when/then pattern](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#givenwhenthen-pattern)
   2. [Fewest number of assertions in every test](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#fewest-number-of-assertions-in-every-test)
   3. [Assert all for multiple assertions](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#assert-all-for-multiple-assertions)
   4. [Disabling failing tests](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#disabling-failing-tests)
   5. [Using DisplayName](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#using-displayname)
   6. [Avoid randomness](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#avoid-randomness)
10.  [Recommended reading](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#recommended-reading)
11. [Updates to this document](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#updates-to-this-document)
12. [Acknowledgements](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#acknowledgements)

## Introduction

Good judgement should be followed. There may be times where it is more readable to not follow a particular guideline.
Readable code is preferred over code that strictly follows this guide.

A short summary explaining why the guideline is in place is included to help explain the rationale behind having it.

## IDE formatting

IDE-specific code style files have been exported and can be imported into your IDE. The files are located in the
[ide](https://github.com/knjk04/book-project/tree/master/ide) directory.

### IntelliJ

<p align="center">
  <img src="/media/styleguide/intellij_code_style.png" alt="Import IntelliJ code style file"/>
</p>

For IntelliJ, you can use import the [code style file](https://github.com/knjk04/book-project/blob/master/ide/intellij/book_project_code_style.xml).
Go to File > Settings > Editor > Code Style > Java in Linux or Windows. Click on the settings cog and choose 
Import Scheme > IntelliJ IDEA code style XML. 

### Eclipse

<p align="center">
  <img src="/media/styleguide/eclipse_code_style.png" alt="Import Eclipse code style file"/>
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

### Use American English

All code and comments should be written in English (United States).

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

### 100 character column limit

A line should generally not exceed 100 characters.

Exceptions to this rule:
- `package` and `import` statements
- Long URL in Javadoc

### One statement per line

A line break should follow every statement. This includes variable declarations.

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

- Between the class name and the first field declaration (or method declaration or definition)

#### No horizontal alignment

```java
// acceptable
private int numberOfPages;
private String authorFirstName;

// unacceptable
private int     numberOfPages;
private Strring authorFirstName;
```

There should be no horizontal alignment. While it looks nice and improves readability, it can be harder to maintain.

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

#### Enumerate all cases or have a default case

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

However, for fields, multiple annotations can be on the same line:
```java
@NotNull @VisibleForTesting String authorFirstName;
```

### Comments

This section is to do with implementation comments, not Javadoc.

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

Local variables should be declared as close as reasonably possible to where it is used. This can help limit the scope and improve readability. Local variables should not be declared at the top of a method for the sake of it.

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

Four spaces should be used for indentation. This is clearer than two spaces (more than four is extraneous).

This also applies to CSS.

#### Aligning method calls

An exception to the above four spaces indentation rule is aligning method calls (see below). This can improve readability.

```java
// good
bookGrid.addColumn(AUTHOR_KEY)
        .setSortable(true);

// bad
bookGrid.addColumn(AUTHOR_KEY)
    .setSortable(true);
```

## Naming

Special prefixes or suffixes, such as to represent member variables, are not used.  For example, the following are not permitted: `mTitle` and `_title`.

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

Underscores are allowed in JUnit test methods to separate logical components. One popular pattern is `<methodUnderTest>_<state>`; for example, `pop_emptyStack`. The most important thing is that the test method name clearly summarises what is being tested.

### Constant names

Constants are written in CONSTANT_CASE. It is written in uppercase letters with each word separated by an underscore.

### Non-constant field names

Non-constant field names, regardless of whether they are static, are written in`lowerCamelCase`.

The names tend to be noun or noun phrases.

### Parameter names

Parameter names are written in `lowerCamelCase`.

Single letter parameter names should be avoided in methods (common exception: for loops).

### Local variables names

Local variables names are written in `lowerCamelCase`.

Even if they are immutable (e.g. marked as `final`), they are not considered constants, so they are not styled as constants.

### Type variables names

Type variables should be either:
- One capital letter, which can be optionally succeeded by a letter (e.g. `T`, `E` or `T2`)
- A class name followed by the capital letter `T` (e.g. `AuthorT`)

## Javadoc

### No @author tag

Author tags can quickly become outdated as methods are updated or completely written by new authors. Git is far better
at tracking changes.

## Programming practices

### Annotations

#### Use @Override

The @Override annotation should be used for overridden methods. This is so that the compiler can run
a check at compile-time to see whether the method annotated with @Override actually overrides a method.

#### Use @VisibleForTesting

While it is usually better to limit members and methods, if it's required for testing, it will need to be made package-private. In such cases, they should be tagged as @VisibleForTesting to make it clear.

### Don't swallow exceptions

Rarely should there be no response to a caught exception (e.g. you may want to log it).

If it is right not to do anything, then this has to be justified in a comment.

An exception to ignore exceptions is if the exception is expected in a test. For example, you may want to test whether the code under test does indeed throw an exception:
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

## JUnit

### Given/when/then pattern
The pattern we follow for JUnit tests are given/when/then. For example, given some input some setup, when an action occurs, then assert as desired. Concrete example:

```java
    @Test
    void errorShownWhenEmailInUse() {
        // given
        userRepository.save(VALID_TEST_USER);
        EmailField emailField = _get(EmailField.class, spec -> spec.withId("email"));

        // when
        _setValue(emailField, VALID_TEST_USER.getEmail());

        // then
        assertThat(emailField.getErrorMessage()).isNotBlank();
    }
```

In general, we add comments to separate out the given, when and then sections. We find that this greatly improves readability.

### Fewest number of assertions in every test

In every test method, try to minimise the number of assertions. Ideally, there should only be one.

### Assert all for multiple assertions

If a test method needs multiple assertions, `assertAll()` (or `assertSoftly()`) should be used. Otherwise, lazy evaluation is used. For example, if you had two assertions, and both assertions fail, you will not know about the second assertion failing until you have fixed the first assertion.

### Disabling failing tests

Instead of commenting out tests, we use the `@Disable` annotation. Please note that if a test fails, you are generally expected to fix it. This should be used as a last resort if you are unable to fix the failing test.

### Using DisplayName

Where it would add value, a `@DisplayName` annotation should be used. For example, you could replace a Javadoc comment with a display name. This is also useful if the method name is concise but not comprehensive. Adding a display name should be used where it serves as useful documentation.

### Avoid randomness

While it may seem better to use pseudorandom bounded values so that you can test more cases, it rarely improves coverage. It's better to use fixed input data with well-defined edge cases.

### TODOs

TODO comments are acceptable and encouraged if they are useful. However, we ask that if you create a TODO, please also create a new corresponding issue.

## Recommended reading

- Clean code, Robert C. Martin

## Updates to this document

If you change any of the recommended styles in this guide, please note that the following may also need to be changed:

- [Checkstyle configuration](https://github.com/knjk04/book-project/blob/master/src/main/resources/checkstyle.xml)
- [IntelliJ formatter](https://github.com/knjk04/book-project/blob/master/ide/intellij/book_project_code_style.xml)
- [Eclipse formatter](https://github.com/knjk04/book-project/blob/master/ide/eclipse/book_project_formatter_profile.xml)

In addition, please update the [table of contents](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md#contents).

## Acknowledgements

This style guide has been adapted from [Google's Java style guide](https://google.github.io/styleguide/javaguide.html) and 
[Twitter's common style guide](https://github.com/twitter-archive/commons/blob/master/src/java/com/twitter/common/styleguide.md).
