package yan.foundation.exec;

import java.util.List;
import java.util.Scanner;

public interface ExternalFunction {
    GenericValue call(List<GenericValue> args);

    // scanner不能在readInt里初始化, 不然在System.in的内容是redirect得到的时候,
    // 多次调用readInt，第二次会报NoSuchElement
    Scanner scanner = new Scanner(System.in);

    static ExternalFunction readInt() {
        return args -> GenericValue.Int(scanner.nextInt());
    }

    static ExternalFunction printInt() {
        return args -> {
            System.out.println(args.get(0).intValue);
            return GenericValue.Void();
        };
    }

    static ExternalFunction readFloat() {
        return args -> GenericValue.Float(scanner.nextFloat());
    }

    static ExternalFunction printFloat() {
        return args -> {
            System.out.println(args.get(0).floatValue);
            return GenericValue.Void();
        };
    }

    static ExternalFunction readDouble() {
        return args -> GenericValue.Double(scanner.nextDouble());
    }

    static ExternalFunction printDouble() {
        return args -> {
            System.out.println(args.get(0).doubleValue);
            return GenericValue.Void();
        };
    }
}
