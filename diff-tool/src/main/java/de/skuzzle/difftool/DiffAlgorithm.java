package de.skuzzle.difftool;

import java.util.List;

public interface DiffAlgorithm {

    List<DiffLine> diffOf(List<String> left, List<String> right);
}
