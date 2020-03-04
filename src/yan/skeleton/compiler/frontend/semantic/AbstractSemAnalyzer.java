package yan.skeleton.compiler.frontend.semantic;

import yan.skeleton.driver.BaseConfig;
import yan.skeleton.driver.Phase;

public abstract class AbstractSemAnalyzer<Tree> extends Phase<Tree, Tree> {

    public AbstractSemAnalyzer(String name, BaseConfig config) {
        super(name, config);
    }
}
