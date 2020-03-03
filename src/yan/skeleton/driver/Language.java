package yan.skeleton.driver;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class Language {
    protected BaseConfig config = new BaseConfig();

    public BaseConfig getConfig() {

        return config;
    }

    public void setConfig(BaseConfig config) {
        this.config = config;
    }

}
