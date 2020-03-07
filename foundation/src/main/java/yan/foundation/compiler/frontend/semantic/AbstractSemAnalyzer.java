package yan.foundation.compiler.frontend.semantic;

import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.Phase;

public abstract class AbstractSemAnalyzer<Tree> extends Phase<Tree, Tree> {

    public AbstractSemAnalyzer(String name, BaseConfig config) {
        super(name, config);
    }
}
