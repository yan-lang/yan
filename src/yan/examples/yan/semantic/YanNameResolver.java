package yan.examples.yan.semantic;

import yan.examples.yan.tree.YanVisitor;
import yan.skeleton.driver.BaseConfig;
import yan.examples.yan.tree.YanTree.*;

public class YanNameResolver extends yan.skeleton.driver.Phase<Program, Program> implements YanVisitor<Object> {
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