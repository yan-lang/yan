package yan.foundation.compiler.frontend.ast;

import java.util.Objects;

public final class Range {
    public final int from;
    public final int to;

    public Range(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "Range(" + from + ", " + to + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range range = (Range) o;
        return from == range.from &&
                to == range.to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
