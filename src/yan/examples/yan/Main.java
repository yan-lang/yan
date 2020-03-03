package yan.examples.yan;

import yan.examples.yan.parse.YanLexer;
import yan.skeleton.driver.Language;
import yan.skeleton.driver.Launcher;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) {
        Language language = new YanLang();
        language.setConfig(new YanConfig());

        Launcher launcher = new Launcher(language);
        launcher.launch(args);
    }
}
