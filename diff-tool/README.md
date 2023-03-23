# Diff-Tool

This is a thin wrapper around `java-diff-utils` which provides the convenience of rendering diffs to a nice human
readable String.

## Usage

```java
final String expected = "...";
final String actual = "...";

// Create a diff using default configuration
final StringDiff diff1 = StringDiff.simple(expected, actual);

// Render as unified diff
final String unifiedDiff = diff1.toString();
// Render as split diff
final String splitDiff = diff1.toString(SplitDiffRenderer.INSTANCE);

// Test whether compared Strings differed in line separators
final boolean lineSeparatorDifference = diff1.hasLineSeparatorDifference()
// Tests whether compared Strings differed in actual text
final boolean textDifference = diff1.hasTextDifference()

// Create a diff by configuring the java-diff-utils _DiffRowGenerator_
final DiffAlgorithm diffAlgorithm = DiffUtilsDiffAlgorithm.create(builder -> builder
        .showInlineDiffs(true)
        .inlineDiffByWord(true)
        .ignoreWhiteSpaces(true));

final StringDiff diff2 = StringDiff.using(diffAlgorithm, expected, actual);
```

## Sample output

Unified Diff:
```
[...]
  6  6   Some unchanged lines6
  7  7   Some unchanged lines7
  8  8   Some unchanged lines8
  9    - This is a test <<senctence>>.
     9 + This is a test <<for diffutils>>.
 10 10   This is the second line.
 11 11   Some unchanged lines9
 12 12   Some unchanged lines10
 13 13   Some unchanged lines11
 14 14   Some unchanged lines12
 15    - And here is the finish with way more than 80 characters and I'm very curious how this is going to be displayed in split view diff.
 16 15   This line is unchanged
    16 + This line has been added
 17 17   Some unchanged lines13
 18 18   Some unchanged lines14
 19 19   Some unchanged lines15
[...]
[...]
 22 22   Some unchanged lines18
 23 23   Some unchanged lines19
 24 24   Some unchanged lines20
 25    - <<Another>> <<difference>>
    25 + <<This>> <<has changed>>
 26 26   Some unchanged lines21
 27 27   Some unchanged lines22
 28 28   Some unchanged lines23
[...]
```

Split Diff
```
[...]
  6   Some unchanged lines6                                                                                                              |  6   Some unchanged lines6
  7   Some unchanged lines7                                                                                                              |  7   Some unchanged lines7
  8   Some unchanged lines8                                                                                                              |  8   Some unchanged lines8
  9 ! This is a test <<senctence>>.                                                                                                      |  9 ! This is a test <<for diffutils>>.
 10   This is the second line.                                                                                                           | 10   This is the second line.
 11   Some unchanged lines9                                                                                                              | 11   Some unchanged lines9
 12   Some unchanged lines10                                                                                                             | 12   Some unchanged lines10
 13   Some unchanged lines11                                                                                                             | 13   Some unchanged lines11
 14   Some unchanged lines12                                                                                                             | 14   Some unchanged lines12
 15 - And here is the finish with way more than 80 characters and I'm very curious how this is going to be displayed in split view diff. |      
 16   This line is unchanged                                                                                                             | 15   This line is unchanged
                                                                                                                                         | 16 + This line has been added
 17   Some unchanged lines13                                                                                                             | 17   Some unchanged lines13
 18   Some unchanged lines14                                                                                                             | 18   Some unchanged lines14
 19   Some unchanged lines15                                                                                                             | 19   Some unchanged lines15
[...]
[...]
 22   Some unchanged lines18                                                                                                             | 22   Some unchanged lines18
 23   Some unchanged lines19                                                                                                             | 23   Some unchanged lines19
 24   Some unchanged lines20                                                                                                             | 24   Some unchanged lines20
 25 ! <<Another>> <<difference>>                                                                                                         | 25 ! <<This>> <<has changed>>
 26   Some unchanged lines21                                                                                                             | 26   Some unchanged lines21
 27   Some unchanged lines22                                                                                                             | 27   Some unchanged lines22
 28   Some unchanged lines23                                                                                                             | 28   Some unchanged lines23
[...]
```