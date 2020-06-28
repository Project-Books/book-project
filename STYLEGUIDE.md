# Java style guide

*Multiple versions of this styleguide may exist throughout this repository. As with other documents on this repository,
the version on the master branch should be followed, as this version should be the most up-to-date.*

*Please also ensure that you remain familiar with this document as it may change from time to time*

Good judgement should be followed. There may be times where it is more readable to not follow a particular guideline.
Readable code is preferred over code that strictly follows this guide.

A short summary explaining why the guideline is in place is included to help explain the rationale behind having it.

The styleguide used is an extension of the [Google Java style guide](https://google.github.io/styleguide/javaguide.html). 
Any differences are listed in this document. By contributing to this repository, you are expected to follow this style
guide and, where it does not conflict with this style guide, Google's Java style guide.

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

**4 Spaces, no tabs**

4 spaces should be used for indentation. This is clearer than 2 spaces (more than 4 is extraneous).

**One statement per line**


**Underscores in numeric literals**

For numeric literals that are one million or higher, underscores are recommended to separate digits by thousands:

```java
// bad
int booksSold = 1000000000;

// good
int booksSold = 1_000_000_000;
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

Adapted from [Google's Java style guide](https://google.github.io/styleguide/javaguide.html) and 
[Twitter's common style guide](https://github.com/twitter-archive/commons/blob/master/src/java/com/twitter/common/styleguide.md).
