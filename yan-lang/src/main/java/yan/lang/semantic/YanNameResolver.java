package yan.lang.semantic;

import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.Phase;
import yan.lang.tree.YanTree.Function;
import yan.lang.tree.YanTree.Program;
import yan.lang.tree.YanVisitor;

public class YanNameResolver extends Phase<Program, Program> implements YanVisitor<Object> {
    public YanNameResolver(String name, BaseConfig config) {
        super(name, config);
    }

    @Override
    public Program transform(Program input) {
        input.accept(this);
        return input;
    }

    @Override
    protected boolean hasError() {
        return false;
    }

    @Override
    public Object visit(Program program) {
        System.out.println("Visit Program");
        for (var func : program.functions) {
            func.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Function function) {
        System.out.println("Visit Function");
        return null;
    }
}