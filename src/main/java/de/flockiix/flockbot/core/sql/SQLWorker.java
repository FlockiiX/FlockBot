package de.flockiix.flockbot.core.sql;


import de.flockiix.flockbot.core.config.Config;
import de.flockiix.flockbot.feature.Bot;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLWorker {
    private static final SQLConnector sql = Bot.sqlConnector;

    // PREFIX

    private static boolean isPrefixSet(String guildId) {
        ResultSet resultSet = sql.query("SELECT * FROM Prefixes WHERE GUILDID='" + guildId + "';");
        try {
            if (resultSet.next()) {
                return true;
            }

            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public static String getPrefix(String guildId) {
        ResultSet resultSet = sql.query("SELECT * FROM Prefixes WHERE GUILDID='" + guildId + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getString("PREFIX");
            }

            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return Config.get("default_prefix");
    }

    public static void setPrefix(String guildId, String prefix) {
        if (isPrefixSet(guildId))
            sql.update("UPDATE Prefixes SET PREFIX='" + prefix + "' WHERE GUILDID='" + guildId + "';");
        else
            sql.update("INSERT INTO Prefixes (GUILDID, PREFIX) VALUES ('" + guildId + "','" + prefix + "');");
    }
}