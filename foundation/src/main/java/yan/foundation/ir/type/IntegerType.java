package yan.foundation.ir.type;

import yan.foundation.ir.constant.ConstantInt;

import java.util.HashMap;
import java.util.Map;

public class IntegerType extends IRType {
    public int width;

    private IntegerType(int width) {
        super(Kind.INTEGER);
        this.width = width;
    }

    public static IntegerType get(int bitSize) {
        if (buffer.containsKey(bitSize)) {
            return buffer.get(bitSize);
        }
        IntegerType type = new IntegerType(bitSize);
        buffer.put(bitSize, type);
        return type;
    }

    private static final Map<Integer, IntegerType> buffer = new HashMap<>();

    public static final IntegerType int1 = new IntegerType(1);
    public static final IntegerType int8 = new IntegerType(8);
    public static final IntegerType int16 = new IntegerType(16);
    public static final IntegerType int32 = new IntegerType(32);
    public static final IntegerType int64 = new IntegerType(64);
    public static final IntegerType int128 = new IntegerType(64);

    static {
        buffer.put(1, int1);
        buffer.put(8, int8);
        buffer.put(16, int16);
        buffer.put(32, int32);
        buffer.put(64, int64);
        buffer.put(128, int128);
    }

    // Constant

    public ConstantInt constant(int value) { return new ConstantInt(this, value); }

    public ConstantInt zero() { return new ConstantInt(this, 0); }

    // Type check

    @Override
    public boolean isIntegerType() {
        return true;
    }

    @Override
    public String toString() {
        return "i" + width;
    }
}
