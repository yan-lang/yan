package yan.foundation.driver;

import yan.foundation.driver.lang.Code;
import yan.foundation.driver.lang.Language;
import yan.foundation.driver.shell.ScriptEngine;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

public class ScriptEngineImpl implements ScriptEngine {

    public final Language language;
    protected List<String> targets;
    protected String target;

    public ScriptEngineImpl(Language language) {
        this.language = language;
        this.target = getDefaultTarget();
    }

    @Override
    public int execute(String statement, PrintStream out, PrintStream err) {
        return language.interpret(Code.of("stdin." + extension(), statement), target, out, err);
    }

    @Override
    public void setTarget(String targetName) {
        this.target = targetName;
    }

    @Override
    public String getTarget() {
        return this.target;
    }

    @Override
    public List<String> getTargets() {
        if (targets == null) targets = language.getInterpreterTargets()
                                               .stream()
                                               .map(target -> target.name)
                                               .collect(Collectors.toList());
        return targets;
    }

    public String getDefaultTarget() {
        return getTargets().get(getTargets().size() - 1);
    }

    @Override
    public String name() { return language.name(); }

    @Override
    public String version() { return language.version(); }

    @Override
    public String extension() { return language.extension(); }
}
