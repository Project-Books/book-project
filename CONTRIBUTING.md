# Contributing

*Notes:*

*1) Multiple versions of this document may exist, so please always refer to the [version on the master branch](https://github.com/knjk04/book-project/blob/master/README.md).*

*2) Please also ensure that you remain familiar with this contributing document as it may change from time to time.*

Thank you for taking an interest in contributing!

## Code of Conduct

By contributing to this project, you are expected to adhere to the [Book Project Code of Conduct](https://github.com/knjk04/book-project/blob/master/CODE_OF_CONDUCT.md). 

## Questions, suggestions & feedback

All questions, suggestions and feedback are both welcome and encouraged. 

Questions and feedback should be discussed over [Gitter](https://gitter.im/book-project-community). This allows other to benefit from seeing the questions, too.

For suggestions, please vote on, or add to, the existing relevant issue. If no such issue exists, feel free to open a new issue.

If in doubt, talk to us over [Gitter](https://gitter.im/book-project-community)!

## Before making changes

### Tell us what issue you want to work on

*This applies to both code and documentation changes.*

If an issue already exists for what you want to work and it is unassigned, and it is not labelled as [blocked](https://github.com/knjk04/book-project/labels/blocked), feel free to let us know that you want to work on it. We can then assign you to it. If an issue doesn't already exist, please create one and then let us know that you are happy to work on it.

Letting us know which issue you want to work on before working on it helps to minimise the chances of duplicated work.

### Discuss your implementation approach

After having been assigned to a ticket, please discuss your implementation approach with us first before working on it. It may be the case that you make a good change, but it isn't what we are looking for. Moreover, there may be a better way of doing something.

This step can help to save both your time and our time in the long run :)

## Workflow

Please follow the process below:

1. Tell us which issue you want to work on
2. Discuss your implementation approach with us
3. Fork the repository
4. Create a new branch off master (unless the corresponding issue says otherwise). Your branch should have a descriptive branch name (that corresponds to the relevant issue) and include the issue number (e.g. `add-styleguide-1048`)
5. Submit a pull request to merge into the branch specified on the issue

### Commit messages

Use the present tense (e.g. "add converter", not "added converter") for git commit messages.

Please also ensure that your commit messages are descriptive (i.e. a commit message should effectively summarise what change(s) you made).

### One problem per patch

For every patch (i.e. pull request) you make, please ensure it solves one problem. If you want to solve multiple problems, please spread them across multiple patches -- one patch per problem.

This makes it easier to keep track of what changes were made in what branch or pull request when looking back.

## Code contributions

Please adhere to [our Java style guide](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md). As mentioned in our style guide, you can check for any violations by running `./mvnw validate`.

Before submitting a pull request:
- Ensure your patch branch on your fork is up-to-date with this repository's master branch
- Run the unit tests and ensure they pass
  - Please note that PredefinedShelfTests currently fail when all of the tests run collectively, but it passes when run individually (see issue #88). As long as that test still passes when you run it by itself, please submit your pull request.

## Ways to contribute

- Code: adding new features, fixing bugs and general refactoring to name a few
  - Issues and pull requests labelled with [needs-repro](https://github.com/knjk04/book-project/labels/needs-repro) have yet to be reproduced. If you're able to reproduce it, letting us know would help a lot

- Documentation: improvements (including fixing typos!) to files such as the [README](https://github.com/knjk04/book-project/blob/master/README.md), any of the [wiki](https://github.com/knjk04/book-project/wiki) pages, the [style guide](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md), comments in source files, this contributing document or the [code of conduct](https://github.com/knjk04/book-project/blob/master/CODE_OF_CONDUCT.md).

- Design: helping to design the UI and UX of the web app through prototypes (such as wireframes and drawings)

- Feedback: we're eager to find new ways to improve, so please do let us know!

- Translations: it would be good if we could support multiple languages. If you're able to help with this, feel free to get in touch with us over [Gitter](https://gitter.im/book-project-community).
