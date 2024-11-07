package MultiThreadExp;

import MultiThreadExp.Objects.Archive;
import MultiThreadExp.Objects.User;
import MultiThreadExp.Users.Administrator;
import MultiThreadExp.Users.Browser;
import MultiThreadExp.Users.Operator;

import java.util.*;

public class DataProcessing {
    static Hashtable<String, User> users;
    static Hashtable<String, Archive> archives;

    static {
        archives = new Hashtable<>();
        users = new Hashtable<>();
        users.put("jack", new Operator("jack", "123", "operator"));
        users.put("rose", new Browser("rose", "123", "browser"));
        users.put("kate", new Administrator("kate", "123", "administrator"));
    }

    public static User searchUserByName(String name) {
        if (users.containsKey(name)) {
            return users.get(name);
        }
        return null;
    }

    public static Enumeration<User> getAllUser() {
        return users.elements();
    }

    public static Enumeration<Archive> getAllArchives() {
        return archives.elements();
    }

    public static boolean updateUser(String name, String password, String role) {
        User user;
        if (users.containsKey(name)) {
            if (role.equalsIgnoreCase("administrator")) user = new Administrator(name, password, role);
            else if (role.equalsIgnoreCase("operator")) user = new Operator(name, password, role);
            else user = new Browser(name, password, role);
            users.put(name, user);
            return true;
        } else return false;
    }

    public static boolean insertUser(String name, String password, String role) {
        User user;
        if (users.containsKey(name)) return false;
        else {
            if (role.equalsIgnoreCase("administrator")) user = new Administrator(name, password, role);
            else if (role.equalsIgnoreCase("operator")) user = new Operator(name, password, role);
            else user = new Browser(name, password, role);
            users.put(name, user);
            return true;
        }
    }

    public static boolean deleteUser(String name) {
        if (users.containsKey(name)) {
            users.remove(name);
            return true;
        } else return false;
    }

    public static boolean insertArchive(String id, Archive archive) {
        if (archives.containsKey(id)) return false;
        else archives.put(id, archive);
        return true;
    }
}
