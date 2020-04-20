package yan.lang.semantic;

import yan.foundation.compiler.frontend.semantic.v1.Scope;
import yan.foundation.compiler.frontend.semantic.v1.symbol.BuiltinSymbol;
import yan.foundation.driver.lang.Phase;
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
        that.scope = new Scope("global", null);
        that.scope.define(new BuiltinSymbol("int", YanTypes.Int));
        that.scope.define(new BuiltinSymbol("float", YanTypes.Float));
        that.scope.define(new BuiltinSymbol("double", YanTypes.Double));
        that.scope.define(new BuiltinSymbol("void", YanTypes.Void));
    }
}
