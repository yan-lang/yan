package yan.skeleton.compiler.frontend.semantic;

import yan.skeleton.driver.BaseConfig;
import yan.skeleton.driver.Phase;

public abstract class NameResolver<Tree> extends Phase<Tree, Tree> {

    public NameResolver(String name, BaseConfig config) {
        super(name, config);
    }
}
