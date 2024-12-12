package MultiThreadExp.Server;

import MultiThreadExp.Objects.Doc;
import MultiThreadExp.Objects.User;
import MultiThreadExp.Utils;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Database {
    public static final String USERNAME = "test";
    public static final String PASSWORD = "123";
    public static MysqlDataSource dataSource;

    public Database() {
        try {
            dataSource = new MysqlDataSource();
            dataSource.setUser(USERNAME);
            dataSource.setPassword(PASSWORD);
            dataSource.setServerName("localhost");
            dataSource.setPort(3306);
            dataSource.setDatabaseName("MultiThreadExp");
            dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public PreparedStatement getPrepared(String sql) throws SQLException {
        var conn = dataSource.getConnection();
        return conn.prepareStatement(sql);
    }

    public @Nullable User getUserByUsername(String username) throws SQLException {
        try (var stmt = getPrepared("SELECT * FROM user_info WHERE username=?")) {
            stmt.setString(1, username);

            var rs = stmt.executeQuery();

            if (rs == null) return null;

            if (rs.next()) {
                return Utils.toUser(rs);
            }

            return null;
        }
    }

    public void updateUser(String username, String password, String role) throws SQLException {
        try (var stmt = getPrepared("UPDATE user_info SET username=?, password=?, role=? WHERE username=?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.setString(4, username);

            stmt.execute();
        }
    }

    public void insertUser(User user) throws SQLException {
        try (var stmt = getPrepared("INSERT INTO user_info (username, password, role) VALUES (?,?,?)")) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());

            stmt.execute();
        }
    }

    public void deleteUser(String username) throws SQLException {
        try (var stmt = getPrepared("DELETE FROM user_info WHERE username=?")) {
            stmt.setString(1, username);

            stmt.execute();
        }
    }

    public void insertFile(int id, Doc file) throws SQLException {
        try (var stmt = getPrepared("INSERT INTO doc_info (id, creator, timestamp, description, filepath) VALUES (?,?,?,?,?)")) {
            stmt.setInt(1, id);
            stmt.setString(2, file.getCreator());
            stmt.setTimestamp(3, file.getTimestamp());
            stmt.setString(4, file.getDescription());
            stmt.setString(5, file.getFilepath());

            stmt.execute();
        }
    }

    public @Nullable Doc getDocById(int id) throws SQLException {
        try (var stmt = getPrepared("SELECT * FROM doc_info WHERE id=?")) {
            stmt.setInt(1, id);

            var rs = stmt.executeQuery();

            if (rs == null) return null;

            if (rs.next()) return Utils.toDoc(rs);

            return null;
        }
    }

    public <T> List<T> get(String tablename, String condition, Function<ResultSet, T> generator) throws SQLException {
        try (var stmt = getPrepared("SELECT * FROM " + tablename + condition)) {
            List<T> result = new ArrayList<>();

            var rs = stmt.executeQuery();

            if (rs == null) return List.of();

            while (rs.next()) {
                result.add(generator.apply(rs));
            }

            return result;
        }
    }

    public List<User> getAllUser() throws SQLException {
        return get("user_info", "", Utils::toUser);
    }

    public List<Doc> getAllFiles() throws SQLException {
        return get("doc_info", "", Utils::toDoc);
    }
}
