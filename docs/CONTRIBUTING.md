# Contributing

## Branch

Branch name should start with `ISSUE_ID` and after the id there should be `-` then continues with description of branch or title of ticket. Use `_` for empty spaces.

Example:

```
123-My_Cool_Feature_Or_Bug
```

## Commit Message

Every commit message should match the following format `[/#ISSUE_ID] Commit message`

Example:

```
[/#ISSUE_ID] My cool feature
```

## Pull Request

### Title

Pull Request title should follow below format:

```
[/#ISSUE_ID] ISSUE_TITLE
```

Example:

```
[/#ISSUE_ID] Whatever the name of ticket is
```

### Description

Description has to have `Resolves /#ISSUE_ID` with relevant issue. It will help automatically close relevant issue once the PR is merged.

Example:

```
Resolves /#123

Some description.
```
