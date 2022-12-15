package de.skuzzle.test.snapshots.data.text;

import java.util.List;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRow.Tag;

class DiffListLookahead {

    public static int indexOfNextNonEqual(List<DiffRow> fullDiff, int startIndex) {
        for (int i = startIndex; i < fullDiff.size(); ++i) {
            if (fullDiff.get(i).getTag() != Tag.EQUAL) {
                return i;
            }
        }
        return fullDiff.size();
    }

    public static int lookBehind(List<DiffRow> fullDiff, int startIndex) {
        int count = 0;
        for (int i = startIndex; i >= 0; --i) {
            if (fullDiff.get(i).getTag() != Tag.EQUAL) {
                return count;
            }
            count++;
        }
        return count;
    }
}
