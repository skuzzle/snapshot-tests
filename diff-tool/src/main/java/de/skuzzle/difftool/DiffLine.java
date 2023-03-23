package de.skuzzle.difftool;

import java.util.Objects;

public final class DiffLine {

    private final Type type;
    private final String oldLine;
    private final String newLine;

    public DiffLine(Type type, String oldLine, String newLine) {
        this.type = Objects.requireNonNull(type);
        this.oldLine = Objects.requireNonNull(oldLine);
        this.newLine = Objects.requireNonNull(newLine);
    }

    public enum Type {
        INSERT,
        DELETE,
        CHANGE,
        EQUAL
    }

    public Type type() {
        return type;
    }

    public String oldLine() {
        return oldLine;
    }

    public String newLine() {
        return newLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final DiffLine diffLine = (DiffLine) o;
        return type == diffLine.type && oldLine.equals(diffLine.oldLine) && newLine.equals(diffLine.newLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, oldLine, newLine);
    }

    @Override
    public String toString() {
        return "[" + this.type + "," + this.oldLine + "," + this.newLine + "]";
    }
}
