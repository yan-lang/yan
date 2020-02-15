package yan.examples.yan;

import yan.skeleton.driver.Launcher;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        Launcher launcher = new Launcher();
        launcher.setConfigType(CConfig.class);
        launcher.launch(args);
    }
}
