package yan.foundation.printer;

public interface PhasePrinter<Out> {

    String toString(Out out);

    String fileExtension();

    String targetName();

}
