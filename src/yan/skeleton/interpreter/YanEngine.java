package yan.skeleton.interpreter;

public class YanEngine implements ScriptEngine{

    @Override
    public Object execute(String statement) throws Exception {
        return "test";
    }

    @Override
    public String getName() {
        return "Yan";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}
