package yan.foundation.ir.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructType extends IRType {
    private final String name;
    private final List<IRType> elementTypes;

    private StructType(String name, List<IRType> elementTypes) {
        super(Kind.STRUCT);
        this.name = name;
        this.elementTypes = elementTypes;
    }

    // ------------- Factory ------------- //

    protected static Map<String, StructType> buffer = new HashMap<>();

    public static StructType get(String name, List<IRType> elementTypes) {
        if (get(name) != null) return get(name);
        var type = new StructType(name, elementTypes);
        buffer.put(name, type);
        return type;
    }

    public static StructType get(String name) {
        return buffer.get(name);
    }

    // ------------- Instance methods ------------- //

    public boolean hasName() { return name != null; }

    public String getName() { return name; }

    public List<IRType> getElementTypes() { return elementTypes; }

    public int numOfElementTypes() { return elementTypes.size(); }

    public IRType getElementTypeAt(int index) { return elementTypes.get(index); }


    @Override
    public boolean isStructType() {
        return true;
    }
}
