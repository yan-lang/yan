package yan.lang;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.lang.Code;
import yan.foundation.driver.lang.Phase;
import yan.lang.predefine.YanLang;
import yan.lang.predefine.YanTree;
import yan.lang.semantic.YanCSAnalyzer;
import yan.lang.semantic.YanNameResolver;
import yan.lang.semantic.YanTypeBuilder;
import yan.lang.semantic.YanTypeChecker;

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
        return Optional.of(new YanLexer().pipe(new YanParser())
                                         .pipe(new YanCSAnalyzer())
                                         .pipe(new YanTypeBuilder())
                                         .pipe(new YanNameResolver()));
    }

    @Override
    public Optional<Phase<Code, YanTree.Program>> checkType() {
        return Optional.of(new YanLexer().pipe(new YanParser())
                                         .pipe(new YanCSAnalyzer())
                                         .pipe(new YanTypeBuilder())
                                         .pipe(new YanNameResolver())
                                         .pipe(new YanTypeChecker()));
    }
}
