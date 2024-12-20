package MultiThreadExp.Objects;

public class User extends TableData {
    private String name;
    private String password;
    private String role;

    public User(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return this.name + "," + this.password + "," + this.role;
    }

    public static User fromString(String s) {
        var split = s.split(",");
        if (split.length != 3) return null;
        return new User(split[0], split[1], split[2]);
    }

    @Override
    public String[] toDataRow() {
        return new String[]{this.name, this.password, this.role};
    }
}
