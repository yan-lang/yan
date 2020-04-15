package yan.lang;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.lang.Code;
import yan.foundation.driver.lang.Phase;
import yan.lang.predefine.YanLang;
import yan.lang.predefine.YanTree;

import java.util.List;
import java.util.Optional;

public class TaskFactoryImpl implements YanLang.TaskFactory {
    @Override
    public Optional<Phase<Code, List<Token>>> lex() {
        return Optional.of(new YanLexer());
    }

    @Override
    public Optional<Phase<Code, YanTree.Program>> parse() {
        return Optional.of(new YanLexer().pipe(new YanParser()));
    }

    @Override
    public Optional<Phase<Code, YanTree.Program>> checkControlStructure() {
        return Optional.of(new YanLexer().pipe(new YanParser()).pipe(new YanCSAnalyzer()));
    }

    @Override
    public Optional<Phase<Code, YanTree.Program>> resolveName() {
        return Optional.of(new YanLexer().pipe(new YanParser()).pipe(new YanNamer()));
    }
}
