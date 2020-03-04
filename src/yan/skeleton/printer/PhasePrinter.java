package yan.skeleton.printer;

public interface PhasePrinter<Out> {

    String toString(Out out);

    String fileExtension();

    String targetName();

}
