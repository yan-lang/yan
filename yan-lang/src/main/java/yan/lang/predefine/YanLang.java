package yan.lang.predefine;


import yan.common.CommonFormatterFactory;
import yan.common.CommonTaskFactory;
import yan.foundation.driver.lang.*;
import yan.foundation.frontend.lex.Token;
import yan.foundation.frontend.lex.formatter.SimpleTokenFormatter;
import yan.foundation.frontend.lex.formatter.XMLTokenFormatter;
import yan.lang.predefine.formatter.CSTreeFormatter;
import yan.lang.predefine.formatter.NameTreeFormatter;
import yan.lang.predefine.formatter.ParseTreeFormatter;
import yan.lang.predefine.formatter.TypeTreeFormatter;

import java.util.ArrayList;
import java.util.List;

public class YanLang extends Language {
    List<CompilerTarget<Code, ?>> compilerTargets = new ArrayList<>();
    List<InterpreterTarget<Code, ?>> interpreterTargets = new ArrayList<>();

    @Override
    public List<CompilerTarget<Code, ?>> getCompilerTargets() { return compilerTargets; }

    @Override
    public List<InterpreterTarget<Code, ?>> getInterpreterTargets() { return interpreterTargets; }

    @Override
    public String version() { return "1.0"; }

    @Override
    public String name() { return "Yan"; }

    @Override
    public String extension() { return "yan"; }

    public YanLang(TaskFactory t) {
        this(t, new DefaultFormatterFactory());
    }

    public YanLang(TaskFactory t, FormatterFactory f) {
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

    private void buildTargets(TaskFactory t, FormatterFactory f, boolean isInterpreting, BuildTargetAction action) {
        t.lex(isInterpreting).ifPresent(phase -> action.exec("lex", phase, f.lex(isInterpreting)));
        t.parse(isInterpreting).ifPresent(phase -> action.exec("parse", phase, f.parse(isInterpreting)));
        t.checkControlStructure(isInterpreting).ifPresent(phase -> action.exec("cs", phase, f.cs(isInterpreting)));
        t.resolveName(isInterpreting).ifPresent(phase -> action.exec("name", phase, f.nameResolve(isInterpreting)));
        t.checkType(isInterpreting).ifPresent(phase -> action.exec("type", phase, f.typeCheck(isInterpreting)));
    }

    private interface BuildTargetAction {
        <out> void exec(String name, Phase<Code, out> phase, Formatter<out> formatter);
    }

    public interface TaskFactory extends CommonTaskFactory<YanTree.Program> {}

    public interface FormatterFactory extends CommonFormatterFactory<YanTree.Program> {}

    public static class DefaultFormatterFactory implements FormatterFactory {
        @Override
        public Formatter<List<Token>> lex(boolean isInterpreting) {
            return isInterpreting ? new SimpleTokenFormatter() : new XMLTokenFormatter();
        }

        @Override
        public Formatter<YanTree.Program> parse(boolean isInterpreting) {
            return new ParseTreeFormatter();
        }

        @Override
        public Formatter<YanTree.Program> cs(boolean isInterpreting) {
            return new CSTreeFormatter();
        }

        @Override
        public Formatter<YanTree.Program> nameResolve(boolean isInterpreting) {
            return new NameTreeFormatter();
        }

        @Override
        public Formatter<YanTree.Program> typeCheck(boolean isInterpreting) {
            return new TypeTreeFormatter();
        }
    }
}
