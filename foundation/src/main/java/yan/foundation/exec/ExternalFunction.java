package yan.foundation.exec;

import java.util.List;
import java.util.Scanner;

public interface ExternalFunction {
    GenericValue call(List<GenericValue> args);

    static ExternalFunction readInt() {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            return GenericValue.Int(scanner.nextInt());
        };
    }

    static ExternalFunction printInt() {
        return args -> {
            System.out.println(args.get(0).intValue);
            return GenericValue.Void();
        };
    }
}
