package yan.lang;

import yan.foundation.Launcher;

public class Main {

    public static void main(String[] args) {
        YanLang language = new YanLang();

        Launcher launcher = new Launcher(language);
        System.exit(launcher.launch(args));
    }
}
