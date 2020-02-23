package yan.skeleton.compiler.frontend.semantic;

import yan.examples.yan.YanTree;
import yan.skeleton.compiler.frontend.ast.*;
import yan.skeleton.driver.Config;
import yan.skeleton.driver.Phase;

public abstract class NameResolver<Tree> extends Phase<Tree, Tree> {

    public NameResolver(String name, Config config) {
        super(name, config);
    }
}
