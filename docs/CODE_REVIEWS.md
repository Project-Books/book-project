# Code reviews

The purpose of this document is to outline the process of code reviews. This serves as a guideline for reviewers, sa we want to create a standardized way of reviewing code.
However, this could also be useful for contributors that want to understand the process. We want to be as transparent as possible.

As with other documents, this document may change from time to time. Therefore, as a code reviewer, it is important that you stay up-to-date with any changes made.
You should always refer to the [latest version of this document on the master branch](https://github.com/knjk04/book-project/blob/master/.github/CODE_REVIEWS.md).

## Process

1. Look at the pull request (PR) description to ensure that the template has been correctly filled out. For an example of an acceptable PR description,
you can refer to any of the latest merged in PRs.
  - The contributor who created the PR must have already been assigned to the issue
  - The build must not fail. All tests must pass.
  - They must have only solved for the issue they were working on. We want to keep PRs as small as possible.
2. Run the code. Even if the change looks small and isolated, still run it! There may be unintended side effects. Things to check for are whether the app
compiles and runs with no errors, whether you're able to see books in all of the different shelves, delete them and modify them.
4. Open the code in an IDE. Have a look at the diff to see what was changed and look for those parts of code in an IDE. This can help to find areas of improvement
that are hard to identify by just looking at the code in GitHub's diff window (e.g. unused variables).

## Things to look for as a reviewer

- Style guide violations
  - Please ensure you are familiar with the latest version of our [style guide](https://github.com/knjk04/book-project/blob/master/STYLEGUIDE.md).
- Areas to improve readability (e.g. does the name of the method accurately describe what it does?, can a comment be replaced with extracting a block of code into
a method?, etc.)
- Areas to improve efficiency
- Code duplication (e.g. has the contributor redefined a utility method?)
- Grammatical mistakes
- If you seen a failing test that works when you it again, don't ignore it. Look into why that may have failed.

This is by no means an exhaustive list. This is only indicative of common problems that arise.

## Etiquette

In line with our [code of conduct](https://github.com/knjk04/book-project/blob/master/CODE_OF_CONDUCT.md), always be welcoming.

Tips:
- Acknowledge the PR and tell them when you're able to review it (or that you'll review it shortly if you don't yet know when you'll be able to review it)
- Always thank the contributor when they first create the PR
- Politely offer constructive criticism. 
- Point out at least some of the good things as well if you're offering lots of feedback on areas to improve
- Always thank the contributor again when merging the PR (if you have permission)

Things to avoid:
- Being condescending (e.g. don't use ellipsis). We try hard to be supportive of people with all abilities, from beginner to pro.
- Reacting if you think someone has violated the code of conduct. It is best to speak to the maintainer to discuss the matter rather than taking it into your
own hands while you're angry, upset or in another emotional state.

Again, these are not exhaustive lists. 

As a code reviewer, you'll be representing the Book Project, so it's incredibly important that you conduct yourself to
the highest standard of our expectations.

## Timelines

This applies only if you are the main reviewer (e.g. you were asked to review some code).

### When to review a PR by

If possible, try to review the code within seven days of the PR having been created. This also applies for updates. If a contributor updates a pull request,
try to get back to them within seven days.

For high priority bug fixes, we would recommend the PR as soon as possible as it is important that add patches as soon as possible. 

If you are do not have a sufficient amount of time to review a PR, please inform the project maintainer (e.g. over Slack) so that they or someone else can review it.

### Providing updates

As a lot of us are busy people (jobs, family, etc.), it's understandable if seven days isn't always possible. In this case, we ask that you still contact the
contributor within seven days with an update on when you will able to review it (or that you'll look into shortly if you don't yet know when). 

Regular updates to keep the contributor informed that you're looking into the PR is important.
