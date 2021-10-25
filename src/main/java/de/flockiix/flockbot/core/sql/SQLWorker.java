package de.flockiix.flockbot.core.sql;


import de.flockiix.flockbot.core.config.Config;
import de.flockiix.flockbot.feature.Bot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLWorker {
    private static final SQLConnector sql = Bot.sqlConnector;

    // PREFIX

    public static boolean isPrefixSet(String guildId) {
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

    // NEWSLETTER

    public static boolean isNewsletterSet(String userId) {
        ResultSet resultSet = sql.query("SELECT * FROM Newsletter WHERE USERID='" + userId + "';");
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

    public static boolean isNewsletterSubscribed(String userId) {
        ResultSet resultSet = sql.query("SELECT * FROM Newsletter WHERE USERID='" + userId + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getBoolean("SUBSCRIBED");
            }

            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public static void setNewsletter(String userId, boolean subscribed) {
        if (isNewsletterSet(userId))
            sql.update("UPDATE Newsletter SET SUBSCRIBED=" + subscribed + " WHERE USERID='" + userId + "';");
        else
            sql.update("INSERT INTO Newsletter (USERID, SUBSCRIBED) VALUES ('" + userId + "'," + subscribed + ");");
    }

    public static ArrayList<String> getNewsletterSubscribers() {
        ArrayList<String> userIds = new ArrayList<>();
        ResultSet resultSet = sql.query("SELECT * FROM Newsletter WHERE SUBSCRIBED=" + true + ";");
        try {
            while (resultSet != null && resultSet.next()) {
                userIds.add(resultSet.getString("USERID"));
            }

            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return userIds;
    }
}