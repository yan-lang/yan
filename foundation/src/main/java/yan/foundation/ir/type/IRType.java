package yan.foundation.ir.type;

import java.util.Objects;

public abstract class IRType {
    public enum Kind {
        // Primitive Types
        VOID, FLOAT, DOUBLE, LABEL,

        // Derived Types
        INTEGER, FUNCTION, STRUCT, ARRAY, POINTER,
    }

    public final Kind kind;

    public IRType(Kind kind) {
        this.kind = kind;
    }

    public boolean isIntegerType() { return false; }

    public boolean isFloatingPointType() { return false; }

    public boolean isFunctionType() { return false; }

    public boolean isPointerType() { return false; }

    public boolean isArrayType() { return false; }

    public boolean isStructType() { return false; }

    public boolean isVoidType() { return false; }

    public boolean isLabelType() { return false; }

    /**
     * Things that don’t have a size are abstract types, labels and void.
     *
     * @return true if the type has known size
     */
    public boolean isSized() { return false; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IRType type = (IRType) o;
        return kind == type.kind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind);
    }
}
