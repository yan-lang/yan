package yan.lang;

import picocli.CommandLine.Command;
import yan.foundation.InterpretableLanguage;
import yan.foundation.driver.BaseConfig;


public class YanLang extends InterpretableLanguage<YanTree.Program> {

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
