package MultiThreadExp;

import MultiThreadExp.Objects.Doc;
import MultiThreadExp.Objects.User;
import MultiThreadExp.Users.Administrator;
import MultiThreadExp.Users.Browser;
import MultiThreadExp.Users.Operator;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.*;

public class DataProcessing {
    static Hashtable<String, User> users;
    static Hashtable<String, Doc> docs;
    public static boolean isConnected = false;

    static {
        docs = new Hashtable<>();
        users = new Hashtable<>();
        users.put("jack", new Operator("jack", "123", "operator"));
        users.put("rose", new Browser("rose", "123", "browser"));
        users.put("kate", new Administrator("kate", "123", "administrator"));
    }

    /**
     * 模拟真实数据库的initialize过程
     * 0.5的概率产生错误
     */
    public static void init() {
        isConnected = true;
    }

    public static User searchUserByName(String name) throws SQLException {
        DataProcessing.init();
        if (!isConnected) {
            throw new SQLException();
        }
        if (users.containsKey(name)) {
            return users.get(name);
        }
        return null;
    }

    public static Enumeration<User> getAllUser() throws SQLException {
        DataProcessing.init();
        if (!isConnected) {
            throw new SQLException();
        }
        return users.elements();
    }

    public static Enumeration<Doc> getAllFiles() throws SQLException {
        DataProcessing.init();
        if (!isConnected) {
            throw new SQLException();
        }
        return docs.elements();
    }

    public static boolean updateUser(String name, String password, String role) throws SQLException {
        DataProcessing.init();
        if (!isConnected) {
            throw new SQLException();
        }
        User user;
        if (users.containsKey(name)) {
            if (role.equalsIgnoreCase("administrator")) user = new Administrator(name, password, role);
            else if (role.equalsIgnoreCase("operator")) user = new Operator(name, password, role);
            else user = new Browser(name, password, role);
            users.put(name, user);
            return true;
        } else return false;
    }

    public static boolean insertUser(User user) throws SQLException {
        DataProcessing.init();
        if (!isConnected) {
            throw new SQLException();
        }
        if (users.containsKey(user.getName())) return false;
        else {
            users.put(user.getName(), user);
            return true;
        }
    }

    public static boolean deleteUser(User user) throws SQLException {
        return deleteUser(user.getName());
    }

    public static boolean deleteUser(String username) throws SQLException {
        DataProcessing.init();
        if (!isConnected) {
            throw new SQLException();
        }
        if (users.containsKey(username)) {
            users.remove(username);
            return true;
        } else return false;
    }

    public static boolean insertFile(String id, Doc file) throws SQLException {
        DataProcessing.init();
        if (!isConnected) {
            throw new SQLException();
        }
        if (docs.containsKey(id)) return false;
        else docs.put(id, file);
        return true;
    }

    public static @Nullable Doc getDoc(String id) throws SQLException {
        DataProcessing.init();
        if (!isConnected) {
            throw new SQLException();
        }
        return docs.get(id);
    }
}
