package yan.skeleton.driver;

public abstract class PrintablePhase<In, Out> extends Phase<In, Out>{
    public PrintablePhase(String name, BaseConfig config) {
        super(name, config);
    }

    abstract public String toString(Out out);

    abstract public String fileExtension();

    abstract public String targetName();

    @Override
    protected void onSucceed(Out output) {
        super.onSucceed(output);
    }
}
