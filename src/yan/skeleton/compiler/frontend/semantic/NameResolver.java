package yan.skeleton.compiler.frontend.semantic;

import yan.skeleton.driver.Config;
import yan.skeleton.driver.Phase;

public abstract class NameResolver<Tree> extends Phase<Tree, Tree> {

    public NameResolver(String name, Config config) {
        super(name, config);
    }
}
