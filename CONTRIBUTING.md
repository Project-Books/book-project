# Contributing

Thank you for taking an interest in contributing! Please note that by contributing to this project, you are expected to adhere to this contributing document.

## Notes

1. Multiple versions of this document may exist, so please always refer to the [version on the master branch](https://github.com/knjk04/book-project/blob/master/README.md).

2. Please also ensure that you remain familiar with this contributing document as it may change from time to time.

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
5. Bring your branch level with the branch you're going to be branching into before submitting a pull request (we recommend rebasing)
6. Submit a pull request to merge into the branch specified on the issue (or master if a branch is not specified)

### Commit messages

- Use the present tense (e.g. "add converter", not "added converter")

- Ensure your commit messages are descriptive (i.e. a commit message should effectively summarise what change(s) you made)
  - In line with this, you may want to break up your contribution into smaller commits
  - Generally speaking, the default GitHub commit messages (e.g. `add [file]` or `update [file]`) are not sufficient

### One problem per patch

For every patch (i.e. pull request) you make, please ensure it solves one problem. If you want to solve multiple problems, please spread them across multiple patches -- one patch per problem.

This makes it easier to keep track of what changes were made in what branch or pull request when looking back. Additionally, it makes code reviews go a lot quicker and smoother.

## Code contributions

Please adhere to [our code style guide](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md).

Before submitting a pull request, run the unit tests and ensure they pass.

### Writing tests

Generally speaking, if you're working on a new feature (any issue with the [enhancement label](https://github.com/knjk04/book-project/labels/enhancement)) or fixing a [bug](https://github.com/knjk04/book-project/labels/bug), please also write tests. If you need a hand with this, we can help.

## Pull requests

If you've opened a pull request but it is not yet ready for a review, please convert it to a draft. Otherwise, we may help you out by adding to your branch while you're also working on it if the changes needed are small or if the work needs to be completed quickly (e.g. if it is a high priority bug or if it is blocking other work). 

The same applies for pull requests where we've request changes -- mark it as a draft until you're ready for another review. 
  
## Stale issues & pull requests

If you have been assigned to an issue and we have not heard back to you for a week (either through a message or seeing a pull request you made), we will send a friendly message asking whether everything is OK and whether you need a hand. If you need more time, the work is non-urgent and is not blocking anyone, that's completely fine, just let us know. 
If possible, try to let us know how long it will take you to submit a ready-to-review pull request. If we have not heard back from you one week after our friendly reminder (i.e. 2 weeks after you were first assigned to the issue), we will unassign you from the issue. This is so that we can assign someone else to the issue. 

Pull requests are similar. We will send a friendly reminder after one week of receiving the pull request or receiving your last message. If we don't hear back from you, we will close the pull request and label it as [stale](https://github.com/knjk04/book-project/labels/stale).

For urgent work, such as fixing important bugs or work that is blocking others, a similar process will be followed but with shorter timescales. Exactly how long depends on the severity of the stale work. Where possible, we will inform you whether an issue is important so that you can know before picking it up. For high priority bugs, we recommend that you only take it up if you are able to commit to finishing it reasonably quickly.

## Unassigning yourself

If you no longer wish to work an issue, that's fine. All we ask is that you let us know. If you unassign yourself from the issue, we don't get notified, so a quick message (no explanation required) will do. This allows to easily manage issues. We can then assign it to someone else. 

Where possible, please let us know as soon as possible if you no longer wish to work on an issue. 

Please also consider that you're finding something difficult on the issue you're assigned to, we're here to help on [Gitter](https://gitter.im/book-project-community/community).

## Ways to contribute

- Code: adding new features, fixing bugs and general refactoring to name a few
  - Issues and pull requests labelled with [needs-repro](https://github.com/knjk04/book-project/labels/needs-repro) have yet to be reproduced. If you're able to reproduce it, letting us know would help a lot
  - Our test coverage is low, so we need a lot more tests, particularly the areas that are undertested or not covered at all

- Documentation: improvements (including fixing typos!) to files such as the [README](https://github.com/knjk04/book-project/blob/master/README.md), any of the [wiki](https://github.com/knjk04/book-project/wiki) pages, the [style guide](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md), comments in source files, this contributing document or the [code of conduct](https://github.com/knjk04/book-project/blob/master/CODE_OF_CONDUCT.md).

- Design: helping to design the UI and UX of the web app through prototypes (such as wireframes and drawings)

- Feedback: we're eager to find new ways to improve, so please do let us know!

- Translations: it would be good if we could support multiple languages. If you're able to help with this, feel free to get in touch with us over [Gitter](https://gitter.im/book-project-community).
