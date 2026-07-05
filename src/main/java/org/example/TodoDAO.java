package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {

    private static final String URL = "jdbc:sqlite:todo.db";

    public static void initializeDatabase() {

        String sql = """
                CREATE TABLE IF NOT EXISTS todo (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    start DATETIME NOT NULL,
                    end DATETIME NOT NULL,
                    priority INTEGER NOT NULL,
                    memo TEXT,
                    icon TEXT NOT NULL,
                    achieve BOOLEAN NOT NULL DEFAULT 0
                );
                """;

        try (
                Connection conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement()
        ) {

            stmt.execute(sql);
            System.out.println("initializeDatabase");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Todo> selectMonth(YearMonth yearMonth) {

        List<Todo> todos = new ArrayList<>();
        String startDate = yearMonth.atDay(1).toString();
        String endDate = yearMonth.atEndOfMonth().toString();

        String sql = """
            SELECT id, title, start, end, priority, memo, icon FROM todo
            WHERE achieve = 0 AND end BETWEEN ? AND ?
            ORDER BY start
            """;

        try (
                Connection conn = DriverManager.getConnection(URL);
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {

            ps.setString(1, startDate);
            ps.setString(2, endDate);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    todos.add(new Todo(
                            rs.getInt("id"),
                            rs.getString("title"),
                            LocalDateTime.parse(rs.getString("start")),
                            LocalDateTime.parse(rs.getString("end")),
                            rs.getInt("priority"),
                            rs.getString("memo"),
                            rs.getString("icon")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return todos;
    }

    public static List<Todo> selectByDate(LocalDate date) {

        List<Todo> list = new ArrayList<>();

        String sql = """
            SELECT * FROM todo
            WHERE DATE(start) = ? AND achieve = 0
            ORDER BY start
            """;

        try (
                Connection conn = DriverManager.getConnection(URL);
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, date.toString());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(new Todo(
                        rs.getInt("id"),
                        rs.getString("title"),
                        LocalDateTime.parse(rs.getString("start")),
                        LocalDateTime.parse(rs.getString("end")),
                        rs.getInt("priority"),
                        rs.getString("memo"),
                        rs.getString("icon")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Todo> selectAchieve() {

//        List<String> achieveIcons = new ArrayList<>();
        List<Todo> list = new ArrayList<>();

        String sql = "SELECT id, priority, icon FROM todo WHERE achieve = 1 ORDER BY id";

        try (
                Connection conn = DriverManager.getConnection(URL);
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                list.add(new Todo(
                        rs.getInt("id"),
                        null,
                        null,
                        null,
                        rs.getInt("priority"),
                        null,
                        rs.getString("icon")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void insert(String title, LocalDateTime start, LocalDateTime end, Integer priority, String memo, String icon) {

        String sql = """
                INSERT INTO todo (title, start, end, priority, memo, icon, achieve)
                VALUES (?,?,?,?,?,?,0);
                """;

        try (
                Connection conn = DriverManager.getConnection(URL);
        ) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setObject(1, title);
            ps.setObject(2, start);
            ps.setObject(3, end);
            ps.setObject(4, priority);
            ps.setObject(5, memo);
            ps.setObject(6, icon);

            int successCnt = ps.executeUpdate();
            System.out.println("insertDatabase " + successCnt);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id) {

        String sql = """
                DELETE FROM todo WHERE id = ?;
                """;

        try (
                Connection conn = DriverManager.getConnection(URL);
        ) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            int successCnt = ps.executeUpdate();
            System.out.println("deleteDatabase " + successCnt);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void achieve(int id) {

        String sql = """
                UPDATE todo
                SET achieve = 1 WHERE id = ?;
                """;

        try (
                Connection conn = DriverManager.getConnection(URL);
        ) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            int successCnt = ps.executeUpdate();
            System.out.println("achieveDatabase " + successCnt);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}