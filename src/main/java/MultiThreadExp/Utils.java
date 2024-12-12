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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String formatTimestamp(Timestamp timestamp) {
        var sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SS");
        return sdf.format(new Date(timestamp.getTime()));
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
