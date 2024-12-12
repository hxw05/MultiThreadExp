package MultiThreadExp;

import MultiThreadExp.Objects.Doc;
import MultiThreadExp.Objects.TableData;
import MultiThreadExp.Objects.User;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

public class Utils {
    public static final String UPLOAD_PATH = "/Users/su/code/MultiThreadExp/upload";

    public static void init() {
        var uploadPath = new File(UPLOAD_PATH);
        if (!uploadPath.exists()) {
            if (!uploadPath.mkdir()) {
                throw new IllegalStateException();
            }
        }
    }

    public static void logClient(String str) {
        System.out.println("[CLIENT] " + str);
    }

    public static void logServer(String str) {
        System.out.println("[SERVER] " + str);
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
        return sdf.format(new Date(timestamp.getTime()));
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
        var operationPath = new File(UPLOAD_PATH + "/" + file.getName());
        return copy(file, operationPath);
    }

    public static boolean downloadFile(String filename, String targetPath) {
        var source = new File(UPLOAD_PATH + "/" + filename);
        var target = new File(targetPath + "/" + filename);

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

    public static void showConfirmDialog(String text, Runnable func) {
        var confirm = JOptionPane.showConfirmDialog(null, text, "确认", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) func.run();
    }

    public static String[][] toDataVector(List<? extends TableData> list) {
        return list.stream().map(TableData::toDataRow).toList().toArray(new String[0][0]);
    }

    public static @Nullable Doc toDoc(ResultSet rs) {
        try {
            return new Doc(
                    rs.getString("id"),
                    rs.getString("creator"),
                    rs.getTimestamp("timestamp"),
                    rs.getString("description"),
                    rs.getString("filepath")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static @Nullable User toUser(ResultSet rs) {
        try {
            return new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String base64FileToString(File f) throws IOException {
        var bytes = Files.readAllBytes(f.toPath());
        var encoded = Base64.getEncoder().encode(bytes);
        return new String(encoded, StandardCharsets.US_ASCII);
    }

    public static byte[] base64StringToBytes(String s) {
        return Base64.getDecoder().decode(s.getBytes(StandardCharsets.US_ASCII));
    }

    public static void writeTo(String path, String filename, byte[] bytes) throws IOException {
        Files.write(Path.of(path + "/" + filename), bytes);
    }

    public static <T> String serializeList(List<T> list) {
        return String.join("#", list.stream().map(T::toString).toList());
    }

    public static List<Doc> deserializeDocList(String serialized) {
        var split = serialized.split("#");
        return Arrays.stream(split).map(Doc::fromString).toList();
    }

    public static List<User> deserializeUserList(String serialized) {
        var split = serialized.split("#");
        return Arrays.stream(split).map(User::fromString).toList();
    }
}
