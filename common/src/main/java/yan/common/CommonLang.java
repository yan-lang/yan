package yan.common;

import yan.foundation.driver.lang.*;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonLang<TopLevel> extends Language {
    List<CompilerTarget<Code, ?>> compilerTargets = new ArrayList<>();
    List<InterpreterTarget<Code, ?>> interpreterTargets = new ArrayList<>();

    @Override
    public List<CompilerTarget<Code, ?>> getCompilerTargets() { return compilerTargets; }

    @Override
    public List<InterpreterTarget<Code, ?>> getInterpreterTargets() { return interpreterTargets; }

    public CommonLang(CommonTaskFactory<TopLevel> t, CommonFormatterFactory<TopLevel> f) {
        buildTargets(t, f, false, new BuildTargetAction() {
            @Override
            public <out> void exec(String name, Phase<Code, out> phase, Formatter<out> formatter) {
                compilerTargets.add(new CompilerTarget<>(name, phase, formatter));
            }
        });
        buildTargets(t, f, true, new BuildTargetAction() {
            @Override
            public <out> void exec(String name, Phase<Code, out> phase, Formatter<out> formatter) {
                interpreterTargets.add(new InterpreterTarget<>(name, phase, formatter));
            }
        });
    }

    protected void buildTargets(CommonTaskFactory<TopLevel> t, CommonFormatterFactory<TopLevel> f, boolean isInterpreting, BuildTargetAction action) {
        t.lex(isInterpreting).ifPresent(phase -> action.exec("lex", phase, f.lex(isInterpreting)));
        t.parse(isInterpreting).ifPresent(phase -> action.exec("parse", phase, f.parse(isInterpreting)));
        t.checkControlStructure(isInterpreting).ifPresent(phase -> action.exec("cs", phase, f.cs(isInterpreting)));
        t.resolveName(isInterpreting).ifPresent(phase -> action.exec("name", phase, f.nameResolve(isInterpreting)));
        t.checkType(isInterpreting).ifPresent(phase -> action.exec("type", phase, f.typeCheck(isInterpreting)));
    }

    protected interface BuildTargetAction {
        <out> void exec(String name, Phase<Code, out> phase, Formatter<out> formatter);
    }
}
