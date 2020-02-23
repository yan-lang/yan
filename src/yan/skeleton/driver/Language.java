package yan.skeleton.driver;

import yan.skeleton.interpreter.ScriptEngine;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class Language {
    protected Config config = new Config();

    protected List<Phase> phases = new ArrayList<>();

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void addPhase(Class<? extends Phase> phaseClazz) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        phases.add(phaseClazz.getDeclaredConstructor(String.class, Config.class)
                .newInstance(phaseClazz.getSimpleName(), config));
    }


}
