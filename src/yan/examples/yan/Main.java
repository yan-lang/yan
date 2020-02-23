package yan.examples.yan;

import yan.skeleton.driver.Language;
import yan.skeleton.driver.Launcher;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        Language language = new YanLang();
        language.setConfig(new YanConfig());
        language.addPhase(YanLexer.class);

        Launcher launcher = new Launcher(language);
        launcher.launch(args);
    }
}
