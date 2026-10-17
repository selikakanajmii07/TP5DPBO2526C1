import java.sql.*;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/db_people";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection connection;

    // constructor
    public Database() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // digunakan untuk SELECT
    public ResultSet selectQuery(String sql, Object... params) {
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            // statement ikut tertutup otomatis saat ResultSet-nya ditutup
            ps.closeOnCompletion();
            isiParameter(ps, params);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // digunakan untuk INSERT, UPDATE, DELETE
    public int insertUpdateDeleteQuery(String sql, Object... params) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            isiParameter(ps, params);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void isiParameter(PreparedStatement ps, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    // getter
    public Connection getConnection() {
        return connection;
    }
}
