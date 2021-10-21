package de.flockiix.flockbot.core.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLConnector.class);

    private Connection connection;
    private Statement statement;

    public SQLConnector() {
        connect();
        createTables();
    }

    private boolean isConnected() {
        return connection != null;
    }

    private void connect() {
        if (!isConnected()) {
            try {
                File file = new File("database.db");

                if (!file.exists()) {
                    file.createNewFile();
                }

                String url = "jdbc:sqlite:" + file.getPath();
                connection = DriverManager.getConnection(url);
                statement = connection.createStatement();

                LOGGER.info("Connection to the database established");
            } catch (SQLException | IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void close() {
        if (isConnected()) {
            try {
                connection.close();
                LOGGER.info("Disconnected from database");
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public ResultSet query(String sql) {
        try {
            if (isConnected()) {
                return statement.executeQuery(sql);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public void update(String sql) {
        try {
            statement.execute(sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void createTables() {
        createTable("CREATE TABLE IF NOT EXISTS Prefixes (GUILDID VARCHAR(30), PREFIX VARCHAR(5));");
        LOGGER.info("Tables created");
    }

    private void createTable(String sql) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
