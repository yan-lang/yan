package yan.lang.semantic;

import yan.foundation.driver.lang.Phase;
import yan.foundation.frontend.semantic.v1.Scope;
import yan.foundation.frontend.semantic.v1.symbol.BuiltinSymbol;
import yan.lang.predefine.YanTree;
import yan.lang.predefine.YanTypes;

public class YanTypeBuilder extends Phase<YanTree.Program, YanTree.Program> implements YanTree.VoidVisitor {
    @Override
    public YanTree.Program transform(YanTree.Program input) {
        input.accept(this);
        return input;
    }

    @Override
    public void visit(YanTree.Program that) {
        if (global == null || !isInterpreting) {
            global = new Scope("global", null);
            global.define(new BuiltinSymbol("int", YanTypes.Int));
            global.define(new BuiltinSymbol("float", YanTypes.Float));
            global.define(new BuiltinSymbol("double", YanTypes.Double));
            global.define(new BuiltinSymbol("void", YanTypes.Void));
        }
        that.scope = global;
    }

    public static Scope global;
}
