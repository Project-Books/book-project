# Changelog #

## v2.0.1 (2018-12-12) ##
### Changes ###
- Bundled version is now exported as `unpkg` rather than `browser` in `package.json`.  This should help prevent dependencies from being duplicated when bundled downstream.

## v2.0.0 (2018-12-10) ##
### Changes ###
- Replaced `main` with `module` in `package.json`.  Both the `browser` and the `module` bundles use ES Modules.  We no longer distribute a CommonJS format.

### Additions ###
- Added named exports for `IndefiniteObservable` and `wrapWithObserver`.

## v1.0.2 (2018-11-27) ##
### Fixes ###
- Added `private` annotation to `_connect`.

### Changes ###
- Upgraded to `symbol-observable@1.2.0`.

## v1.0.1 (2017-01-17) ##
### Removals ###
- Moved `IndefiniteSubject` to [`material-motion`](https://github.com/material-motion/material-motion-js/blob/develop/packages/core/src/observables/IndefiniteSubject.ts) until it is more throughly documented here.

### Fixes ###
- Fixed missing `var` in inline version of `symbol-observable`.

## v0.3.0 (2016-12-12) ##
### Additions ###
- Added `IndefiniteSubject`.
- Added JSDoc comments.

### Fixes ###
- Fixed presumption that every observer had a `next` channel.
- Fixed `unsubscribe` closing over an unreachable `disconnect`.

## v0.2.0 (2016-12-02) ##
- Removed `browser` field from `package.json`.

## v0.1.0 (2016-12-06) ##
- Initial publication to npm.

