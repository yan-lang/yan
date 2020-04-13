package yan.lang.predefine;


import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.compiler.frontend.lex.formatter.SimpleTokenFormatter;
import yan.foundation.compiler.frontend.lex.formatter.XMLTokenFormatter;
import yan.foundation.driver.lang.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class YanLang extends Language {
    List<Target<Code, ?>> targets = new ArrayList<>();

    @Override
    public List<Target<Code, ?>> getTargets() { return targets; }

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
        t.lex().ifPresent(phase -> targets.add(new Target.Builder<Code, List<Token>>()
                                                       .name("lex")
                                                       .phase(phase)
                                                       .compatibility(Target.Compatibility.BOTH)
                                                       .cformatter(f.clex())
                                                       .iformatter(f.ilex())
                                                       .build()));
        t.parse().ifPresent(phase -> targets.add(new Target.Builder<Code, YanTree.Program>()
                                                         .name("parse")
                                                         .phase(phase)
                                                         .compatibility(Target.Compatibility.BOTH)
                                                         .cformatter(f.parse())
                                                         .iformatter(f.parse())
                                                         .build()));
    }

    public interface TaskFactory {
        Optional<Phase<Code, List<Token>>> lex();

        Optional<Phase<Code, YanTree.Program>> parse();
    }

    public interface FormatterFactory {
        Formatter<List<Token>> clex();

        Formatter<List<Token>> ilex();

        Formatter<YanTree.Program> parse();
    }

    public static class DefaultFormatterFactory implements FormatterFactory {
        @Override
        public Formatter<List<Token>> clex() {
            return new XMLTokenFormatter();
        }

        @Override
        public Formatter<List<Token>> ilex() {
            return new SimpleTokenFormatter();
        }

        @Override
        public Formatter<YanTree.Program> parse() {
            return new ParseTreePrinter();
        }
    }
}
