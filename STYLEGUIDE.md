# Java style guide

Good judgement should be followed. There may be times where it is more readable to not follow a particular guideline.
Readable code is preferred over code that strictly follows this guide.

A short summary explaining why the guideline is in place is included to help explain the rationale behind having it.

The styleguide used is an extension of the [Google Java style guide](https://google.github.io/styleguide/javaguide.html). 
Any differences are listed in this document. By contributing to this repository, you are expected to follow this style
guide and, where it does not conflict with this style guide, Google's Java style guide.

## Notes

1. Multiple versions of this styleguide may exist throughout this repository. As with other documents on this repository,
the version on the master branch should be followed, as this version should be the most up-to-date.

2. Please also ensure that you remain familiar with this document as it may change from time to time

This style guide has been dapted from [Google's Java style guide](https://google.github.io/styleguide/javaguide.html) and 
[Twitter's common style guide](https://github.com/twitter-archive/commons/blob/master/src/java/com/twitter/common/styleguide.md).

## IDE formatting

IDE-specific code style files have been exported and can be imported into your IDE. The files are located in the
[ide](https://github.com/knjk04/book-project/tree/master/ide) directory.

<p align="center">
  <img src="/media/intellij_code_style.png" alt="Import IntelliJ code style file"/>
</p>

For IntelliJ, you can use import the [code style file](https://github.com/knjk04/book-project/blob/master/ide/intellij/book_project_code_style.xml).
Go to File > Settings > Editor > Code Style > Java in Linux or Windows. Click on the settings cog and choose 
Import Scheme > IntelliJ IDEA code style XML. 

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

**120 character limit**

A line should generally not exceed 120 characters (an IntelliJ default).

Why 120 characters, not 100 characters (like the Google Java style guide)? You can have two files side by side that do not exceed 120 characters on a 1920x1080 display (at least in IntelliJ).

**One statement per line**

**Underscores in numeric literals**

For numeric literals that are ten thousand or higher, underscores are recommended to separate digits by thousands:

```java
// bad
int booksSold = 10000;

// good
int booksSold = 10_000;
```

### Indentation

**4 Spaces, no tabs**

4 spaces should be used for indentation. This is clearer than 2 spaces (more than 4 is extraneous).

This also applies to CSS.

**Aligning method calls**

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

**No @author tag**

Author tags can quickly become outdated as methods are updated or completely written by new authors. Git is far better
at tracking changes.

## Programming practices

**Use @Override**

Wherever possible, the @Override annotation should be used for overriden methods. This is so that the compiler can run
a check at compile-time to see whether the method annotated with @Override actually overrides a method.

**StringBuilder over StringBuffer**

StringBuilder should be used instead of a StringBuffer for single-threaded code.

**Overriding hashCode() and equals()**

If you override `hashCode()`, it is good practice to also override `equals()`.

The overrides of `equals()` and `hashCode()` should be equivalent; if `x.equals(y)` is true, then `x.hashCode()` should have
the same value as `y.hashCode()`.

**Overriding toString()**

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

**Short methods**

Wherever possible, try to keep methods short (under 15 lines). This makes it easier to test, comprehend and reuse.

## Updates to this document

If you are considering making changes to any of the recommended styles in this guide, please note that the following may also need to be changed:

- [Checkstyle configuration](https://github.com/knjk04/book-project/blob/master/src/main/resources/checkstyle.xml)
- [IntelliJ formatter](https://github.com/knjk04/book-project/blob/master/ide/intellij/book_project_code_style.xml)
- [Eclipse formatter](https://github.com/knjk04/book-project/blob/master/ide/eclipse/book_project_formatter_profile.xml)
