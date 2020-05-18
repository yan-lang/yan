package yan.foundation.ir.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StructType extends IRType {
    private final String name;
    private final List<IRType> elementTypes;

    private StructType(String name, List<IRType> elementTypes) {
        super(Kind.STRUCT);
        this.name = name;
        this.elementTypes = elementTypes;
    }

    // ------------- Factory ------------- //

    public final static Map<String, StructType> namedTypes = new HashMap<>();

    public static StructType get(String name, List<IRType> elementTypes) {
        if (get(name) != null) return get(name);
        var type = new StructType(name, elementTypes);
        namedTypes.put(name, type);
        return type;
    }

    public static StructType get(String name) {
        return namedTypes.get(name);
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

    @Override
    public String toString() {
        return name;
    }

    public String toStructString() {
        var members = elementTypes.stream()
                                  .map(Object::toString)
                                  .collect(Collectors.joining(", "));
        return "{" + members + "}";
    }
}
