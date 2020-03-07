package yan.lang;

import picocli.CommandLine.Command;
import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.Language;
import yan.lang.parse.YanLexer;
import yan.lang.parse.YanParser;
import yan.lang.tree.YanTree;


public class YanLang extends Language<YanTree.Program> {

    @Command(description = "Yan compiler && interpreter.",
            name = "yan", mixinStandardHelpOptions = true)
    static class Config extends BaseConfig {
    }

    public YanLang() {
        super(new Config());
        lexer = new YanLexer("YanLexer", config);
        parser = new YanParser("YanParser", config);
        buildCompilerTargets();
    }

}
