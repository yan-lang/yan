package yan.lang;

import yan.foundation.driver.Launcher;
import yan.lang.predefine.YanLang;

public class Main {

    public static void main(String[] args) {
        YanLang yan = new YanLang(new TaskFactoryImpl());
        Launcher.instance(yan).commandName("yan").launch(args);
    }

}
