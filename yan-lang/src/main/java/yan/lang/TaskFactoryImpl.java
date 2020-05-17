package yan.lang;

import yan.foundation.driver.lang.Code;
import yan.foundation.driver.lang.Phase;
import yan.foundation.frontend.lex.Token;
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
    public Optional<Phase<Code, List<Token>>> lex(boolean isInterpreting) {
        return Optional.of(new YanLexer());
    }

    @Override
    public Optional<Phase<Code, YanTree.Program>> parse(boolean isInterpreting) {
        return Optional.of(new YanLexer().pipe(new YanParser()));
    }

    @Override
    public Optional<Phase<Code, YanTree.Program>> checkControlStructure(boolean isInterpreting) {
        return Optional.of(new YanLexer().pipe(new YanParser()).pipe(new YanCSAnalyzer()));
    }

    @Override
    public Optional<Phase<Code, YanTree.Program>> resolveName(boolean isInterpreting) {
        return Optional.of(new YanLexer().pipe(new YanParser())
                                         .pipe(new YanCSAnalyzer())
                                         .pipe(new YanTypeBuilder())
                                         .pipe(new YanNameResolver()));
    }

    @Override
    public Optional<Phase<Code, YanTree.Program>> checkType(boolean isInterpreting) {
        return Optional.of(new YanLexer().pipe(new YanParser())
                                         .pipe(new YanCSAnalyzer())
                                         .pipe(new YanTypeBuilder())
                                         .pipe(new YanNameResolver())
                                         .pipe(new YanTypeChecker()));
    }
}
