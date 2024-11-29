package MultiThreadExp;

import MultiThreadExp.Objects.UserActionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.function.Consumer;

public class Utils {
    public static final String UPLOAD_PATH = "/Users/su/code/MultiThreadExp/upload";
    public static final String DOWNLOAD_PATH = "/Users/su/code/MultiThreadExp/download";

    public static void init() {
        var uploadPath = new File(UPLOAD_PATH);
        if (!uploadPath.exists()) {
            if (!uploadPath.mkdir()) {
                throw new IllegalStateException();
            }
        }
        var downloadPath = new File(DOWNLOAD_PATH);
        if (!downloadPath.exists()) {
            if (!downloadPath.mkdir()) {
                throw new IllegalStateException();
            }
        }
    }

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

    public static <T> void enumForEach(Enumeration<T> enumeration, Consumer<T> formatter) {
        while (enumeration.hasMoreElements()) {
            var e = enumeration.nextElement();
            formatter.accept(e);
        }
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String formatTimestamp(Timestamp timestamp) {
        var sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SS");
        return sdf.format(timestamp.toLocalDateTime());
    }

    public static @NotNull File getFileOperationPath(String filename, UserActionType type) {
        return switch (type) {
            case UPLOAD -> new File(UPLOAD_PATH + "/" + filename);
            case DOWNLOAD -> new File(DOWNLOAD_PATH + "/" + filename);
        };
    }

    /**
     * 将 from 处文件复制到 to 处
     *
     * @param from 来源文件
     * @param to   目标文件（是一个目前不存在的文件）
     * @return 如果复制过程没有出现 IOException，返回 true
     */
    public static boolean copy(File from, File to) {
        try {
            Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean uploadFile(File file) {
        var operationPath = getFileOperationPath(file.getName(), UserActionType.UPLOAD);
        return copy(file, operationPath);
    }

    public static boolean downloadFile(String filename) {
        var source = getFileOperationPath(filename, UserActionType.UPLOAD);
        var target = getFileOperationPath(filename, UserActionType.DOWNLOAD);

        return copy(source, target);
    }

    public static void showErrorDialog(String text) {
        JOptionPane.showMessageDialog(null, text, "错误", JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarnDialog(String text) {
        JOptionPane.showMessageDialog(null, text, "警告", JOptionPane.WARNING_MESSAGE);
    }

    public static void showOKDialog(String text) {
        JOptionPane.showMessageDialog(null, text, "成功", JOptionPane.INFORMATION_MESSAGE);
    }
}
