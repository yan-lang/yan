package yan.foundation.ir;

import java.util.*;

/**
 * The ValueSymbolTable (doxygen) class provides a symbol table that the Function and Module classes use for
 * naming value definitions. The symbol table can provide a name for any Value.
 * <p>
 * Note that the SymbolTable class should not be directly accessed by most clients. It should only be used
 * when iteration over the symbol table names themselves are required, which is very special purpose.
 * Note that not all LLVM Values have names, and those without names (i.e. they have an empty name)
 * do not exist in the symbol table.
 * <p>
 * Symbol tables support iteration over the values in the symbol table with begin/end/iterator and supports
 * querying to see if a specific name is in the symbol table (with lookup). The ValueSymbolTable class exposes
 * no public mutator methods, instead, simply call setName on a value, which will autoinsert it into
 * the appropriate symbol table.
 *
 * @param <T> Value Type
 */
public class ValueSymbolTable<T extends Value> implements Iterable<T> {
    Map<String, T> map = new HashMap<>();
    List<T> list = new LinkedList<>();

    public T lookup(String key) {
        return map.get(key);
    }

    public void add(T t) {
        if (t.hasName())
            map.put(t.getName(), t);
        list.add(t);
    }

    public boolean has(String key) {
        return map.containsKey(key);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }
}
