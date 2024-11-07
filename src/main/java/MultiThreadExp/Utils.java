package MultiThreadExp;

import org.jetbrains.annotations.Nullable;

import java.util.Scanner;

public class Utils {
    public static void log(String str) {
        System.out.println(str);
    }

    public static <T> @Nullable T read(String prompt, Class<T> type) {
        System.out.print(prompt);
        var scanner = new Scanner(System.in);
        if (type == Integer.class)
            return type.cast(scanner.nextInt());
        if (type == String.class)
            return type.cast(scanner.nextLine());
        if (type == Double.class)
            return type.cast(scanner.nextDouble());
        if (type == Float.class)
            return type.cast(scanner.nextFloat());
        return null;
    }
}
