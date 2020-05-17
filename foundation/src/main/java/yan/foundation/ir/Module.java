package yan.foundation.ir;

import yan.foundation.ir.type.*;

import java.io.File;
import java.util.Iterator;

public class Module implements Iterable<Function> {
    public final String moduleID;

    public final ValueSymbolTable<Function> functions = new ValueSymbolTable<>();
    public final ValueSymbolTable<GlobalVariable> globals = new ValueSymbolTable<>();

    public Module(String moduleID) {
        this.moduleID = moduleID;
    }

    // -------- Verification and Dumping -------- //

    public void verify() {}

    public String dump() {return null;}

    public void print(File file) {
    }


    //

    public StructType getNamedType(String name) {
        return StructType.get(name);
    }

    public GlobalVariable getNamedGlobalVariable(String name) {
        return globals.lookup(name);
    }

    public Function getNamedFunction(String name) {
        return functions.lookup(name);
    }

    //

    public Function addFunction(String name, FunctionType type) {
        var function = Function.create(type, name);
        functions.define(name, function);
        return function;
    }

    public GlobalVariable addGlobalVariable(String name, IRType type) {
        var gvar = new GlobalVariable(type, null, false, name);
        globals.define(name, gvar);
        return gvar;
    }

    public GlobalVariable addGlobalVariable(String name, Constant initializer) {
        var gvar = new GlobalVariable(initializer.getType(), initializer, false, name);
        globals.define(name, gvar);
        return gvar;
    }

    public GlobalVariable addGlobalString(String name, String value) {
        var gvar = new GlobalVariable(ArrayType.get(IntegerType.int8, value.length() + 1),
                                      null, true, name);
        globals.define(name, gvar);
        return gvar;
    }

    @Override
    public Iterator<Function> iterator() {
        return functions.iterator();
    }
}
