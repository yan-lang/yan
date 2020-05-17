package yan.foundation.exec;

import yan.foundation.ir.Function;
import yan.foundation.ir.type.ArrayType;
import yan.foundation.ir.type.IRType;
import yan.foundation.ir.type.StructType;

import java.util.ArrayList;
import java.util.List;

public class GenericValue {
    public int intValue;
    public float floatValue;
    public double doubleValue;

    public GenericValue pointerValue;

    // For aggregate data type (struct and array)
    public List<GenericValue> aggregateValue;

    // Generic value might be a function
    public Function function;

    public GenericValue() { }

    public static GenericValue ZeroInitialized(IRType type) {
        GenericValue ret = new GenericValue();
        ret.zeroInitialize(type);
        return ret;
    }

    public static GenericValue Double(double doubleValue) {
        var value = new GenericValue();
        value.doubleValue = doubleValue;
        return value;
    }

    public static GenericValue Float(float floatValue) {
        var value = new GenericValue();
        value.floatValue = floatValue;
        return value;
    }

    public static GenericValue Int(int intValue) {
        var value = new GenericValue();
        value.intValue = intValue;
        return value;
    }

    public void zeroInitialize(IRType type) {
        switch (type.kind) {
            case FLOAT: floatValue = 0; break;
            case DOUBLE: doubleValue = 0; break;
            case INTEGER: intValue = 0; break;
            case ARRAY:
                ArrayType arrayType = (ArrayType) type;
                aggregateValue = new ArrayList<>();
                for (int i = 0; i < arrayType.numOfElements(); i++) {
                    aggregateValue.add(GenericValue.ZeroInitialized(arrayType.getElementType()));
                }
                break;
            case STRUCT:
                StructType structType = (StructType) type;
                aggregateValue = new ArrayList<>();
                for (int i = 0; i < structType.numOfElementTypes(); i++) {
                    aggregateValue.add(GenericValue.ZeroInitialized(structType.getElementTypeAt(i)));
                }
                break;
            case POINTER:
                break;
            case FUNCTION: break;
            default: throw new IllegalStateException("Unexpected value: " + type.kind);
        }
    }
}
