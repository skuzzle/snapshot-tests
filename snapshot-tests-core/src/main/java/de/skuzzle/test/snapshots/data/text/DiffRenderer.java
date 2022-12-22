package de.skuzzle.test.snapshots.data.text;

import java.util.List;

import com.github.difflib.text.DiffRow;

interface DiffRenderer {

    String renderDiff(List<DiffRow> rows, int contextLines, int lineNumerOffset);
}
