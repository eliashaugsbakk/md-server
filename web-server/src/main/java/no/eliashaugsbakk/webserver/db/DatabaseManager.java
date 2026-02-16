package no.eliashaugsbakk.webserver.db;

import no.eliashaugsbakk.webserver.model.Page;

import java.sql.*;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private final String url;
    public DatabaseManager(String dbFile) {
        this.url = "jdbc:sqlite:" + dbFile;
    }

    public void initialize() {
        String sql = """
        CREATE TABLE IF NOT EXISTS page (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            slug TEXT NOT NULL UNIQUE,
            title TEXT NOT NULL,
            created TEXT NOT NULL,
            content TEXT
        );
    """;

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Could not initialize database: " + e.getMessage());
        }
    }


    public Page getPage(String slug) {
        String sql = "SELECT id, slug, title, created, content FROM page WHERE slug = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ) {
            preparedStatement.setString(1, slug);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Page(
                        resultSet.getLong("id"),
                        resultSet.getString("slug"),
                        resultSet.getString("title"),
                        Instant.parse(resultSet.getString("created")),
                        resultSet.getString("content")
                );
            }
        } catch (SQLException e) {
            System.err.println("DB Error: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.err.println("Date Parsing Error: " + e.getMessage());
        }
        return null; // Page does not exist
    }

    public List<Page> getAllPages() {
        // We only select what we need for the links to keep it fast
        String sql = "SELECT id, slug, title FROM page ORDER BY title ASC";
        List<Page> pages = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                pages.add(new Page(
                        resultSet.getLong("id"),
                        resultSet.getString("slug"),
                        resultSet.getString("title"),
                        null, // No need for date in the sidebar
                        null  // No need for content in the sidebar
                ));
            }
        } catch (SQLException e) {
            System.err.println("DB Error while fetching all pages: " + e.getMessage());
        }
        return pages;
    }


    /**
     * AI-generated method to populate the db for testing.
     */
    public void seed() {
        String checkSql = "SELECT COUNT(*) FROM page";
        String insertSql = "INSERT INTO page (slug, title, created, content) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {

            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Database empty. Seeding test data...");

                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    // Page 1: Home
                    pstmt.setString(1, "home");
                    pstmt.setString(2, "Welcome to the Wiki");
                    pstmt.setString(3, java.time.Instant.now().toString());
                    pstmt.setString(4, "<h1>Hello!</h1><p>This is the auto-generated home page.</p>");
                    pstmt.executeUpdate();

                    // Page 2: A Test Page
                    pstmt.setString(1, "norway");
                    pstmt.setString(2, "All about Norway");
                    pstmt.setString(3, java.time.Instant.now().toString());
                    pstmt.setString(4, "<h3>Geography</h3><p>Norway is a country in Scandinavia.</p>");
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Seed Error: " + e.getMessage());
        }
    }

}
