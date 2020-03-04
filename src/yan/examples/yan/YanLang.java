package yan.examples.yan;

import picocli.CommandLine.Command;
import yan.examples.yan.parse.YanLexer;
import yan.examples.yan.parse.YanParser;
import yan.examples.yan.tree.YanTree;
import yan.skeleton.driver.BaseConfig;
import yan.skeleton.driver.Language;

import java.util.List;

public class YanLang extends Language<YanTree.Program> {
    @Command(description = "Yan compiler && interpreter.",
            name = "yan", mixinStandardHelpOptions = true)
    static class Config extends BaseConfig { }

    public YanLang() {
        super(new Config());
        lexer = new YanLexer("YanLexer", config);
        parser = new YanParser("YanParser", config);
        buildCompilerTargets();
    }

}
