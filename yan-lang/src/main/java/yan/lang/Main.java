package yan.lang;

import yan.foundation.Language;
import yan.foundation.Launcher;

public class Main {

    public static void main(String[] args) throws Exception {
        Language language = new Language();
        language.addPhase(new YanLexer("YanLexer", language.config), "lex");
        language.addPhase(new YanParser("Parser", language.config), "parse");
        Launcher.launch(language, args);
    }
}
