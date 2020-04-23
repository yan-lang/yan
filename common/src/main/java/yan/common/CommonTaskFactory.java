package yan.common;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.lang.Code;
import yan.foundation.driver.lang.Phase;

import java.util.List;
import java.util.Optional;

public interface CommonTaskFactory<TopLevel> {
    Optional<Phase<Code, List<Token>>> lex();

    Optional<Phase<Code, TopLevel>> parse();

    Optional<Phase<Code, TopLevel>> checkControlStructure();

    Optional<Phase<Code, TopLevel>> resolveName();

    Optional<Phase<Code, TopLevel>> checkType();
}
