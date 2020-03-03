package yan.examples.yan;

import picocli.CommandLine.*;
import yan.skeleton.driver.BaseConfig;

@Command(description = "Yan C compiler && interpreter.",
        name = "yancc", mixinStandardHelpOptions = true, version = "yancc 3.0")
public class YanConfig extends BaseConfig {
    enum CompilingTarget {
        lex, parse, semantic, ir, llvm_ir, binary
    }

    @Option(names = {"-t", "--target"}, defaultValue = "binary", description = "The stage you want to compile to." +
            " Valid values: ${COMPLETION-CANDIDATES}. Default: ${DEFAULT-VALUE}")
    public CompilingTarget target;
}
