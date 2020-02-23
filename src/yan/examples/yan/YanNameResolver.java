package yan.examples.yan;

import yan.skeleton.compiler.frontend.semantic.NameResolver;
import yan.skeleton.driver.Config;
import yan.examples.yan.YanTree.*;

public class YanNameResolver extends NameResolver<YanTree.Program> implements YanVisitor<Object> {
    public YanNameResolver(String name, Config config) {
        super(name, config);
    }

    @Override
    public Program transform(Program input) {
        input.accept(this);
        return input;
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