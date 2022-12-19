package de.skuzzle.test.snapshots.data.text;

import java.util.List;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRow.Tag;

final class DiffListLookahead {

    public static int indexOfNextNonEqual(List<DiffRow> fullDiff, int startIndex) {
        for (int i = startIndex; i < fullDiff.size(); ++i) {
            if (fullDiff.get(i).getTag() != Tag.EQUAL) {
                return i;
            }
        }
        return fullDiff.size();
    }

    private DiffListLookahead() {
        // hidden
    }
}
