package de.flockiix.flockbot.core.sql;


import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.config.Config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLWorker {
    private static final SQLConnector sql = BotInfo.sqlConnector;

    // PREFIX

    /**
     * Checks if a guild has a custom prefix.
     *
     * @param guildId the id of the guild
     * @return true if the guild has a custom prefix and false otherwise
     */
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


    /**
     * Gets the prefix of the guild.
     *
     * @param guildId the id of the guild
     * @return the custom prefix if set otherwise the default prefix
     */
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

    /**
     * Sets a prefix for the guild
     *
     * @param guildId the id of the guild
     * @param prefix  the prefix you want to set
     */
    public static void setPrefix(String guildId, String prefix) {
        if (isPrefixSet(guildId))
            sql.update("UPDATE Prefixes SET PREFIX='" + prefix + "' WHERE GUILDID='" + guildId + "';");
        else
            sql.update("INSERT INTO Prefixes (GUILDID, PREFIX) VALUES ('" + guildId + "','" + prefix + "');");
    }

    // NEWSLETTER

    /**
     * Checks if the user has the newsletter set.
     *
     * @param userId the id of the user
     * @return true if the user has the newsletter subscribed or unsubscribed and false otherwise
     */
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

    /**
     * Checks if the user has subscribed to the newsletter.
     *
     * @param userId the id of the user
     * @return true if the user has subscribed and false otherwise
     */
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

    /**
     * Sets the Subscription to the newsletter for a user.
     *
     * @param userId     the id of the user
     * @param subscribed true for subscribed and false for unsubscribed
     */
    public static void setNewsletter(String userId, boolean subscribed) {
        if (isNewsletterSet(userId))
            sql.update("UPDATE Newsletter SET SUBSCRIBED=" + subscribed + " WHERE USERID='" + userId + "';");
        else
            sql.update("INSERT INTO Newsletter (USERID, SUBSCRIBED) VALUES ('" + userId + "'," + subscribed + ");");
    }

    /**
     * Gets all newsletter subscribers.
     *
     * @return a list with all newsletter subscribers
     */
    public static ArrayList<String> getNewsletterSubscribers() {
        ArrayList<String> userIds = new ArrayList<>();
        ResultSet resultSet = sql.query("SELECT * FROM Newsletter WHERE SUBSCRIBED=" + true + ";");
        try {
            while (resultSet.next()) {
                userIds.add(resultSet.getString("USERID"));
            }

            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return userIds;
    }

    // BLACKLIST

    /**
     * Checks if there are any words on the blacklist for the guild.
     *
     * @param guildId the id of the guild
     * @return true if there are any words on the blacklist and false otherwise
     */
    public static boolean isBlacklistSet(String guildId) {
        ResultSet resultSet = sql.query("SELECT * FROM Blacklist WHERE GUILDID='" + guildId + "';");
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

    /**
     * Checks if a word is blacklisted on the guild or not.
     *
     * @param guildId the id of the guild
     * @param word    the word you want to check if it is blacklisted
     * @return true if the word is blacklisted and false otherwise
     */
    public static boolean isWordOnBlackList(String guildId, String word) {
        ResultSet resultSet = sql.query("SELECT * FROM Blacklist WHERE GUILDID='" + guildId + "' AND WORD='" + word + "';");
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

    /**
     * Adds a word to the blacklist of the guild.
     *
     * @param guildId the id of the guild
     * @param word    the word you want to blacklist on the guild
     */
    public static void addWordToBlacklist(String guildId, String word) {
        if (!isWordOnBlackList(guildId, word))
            sql.update("INSERT INTO Blacklist (GUILDID, WORD) VALUES ('" + guildId + "','" + word + "');");
    }

    /**
     * Removes a word from the blacklist of the guild.
     *
     * @param guildId the id of the guild
     * @param word    the word you want to unblock on the guild
     */
    public static void removeWordFromBlacklist(String guildId, String word) {
        if (isWordOnBlackList(guildId, word))
            sql.update("DELETE FROM Blacklist WHERE GUILDID='" + guildId + "' AND WORD='" + word + "';");
    }

    /**
     * Gets all words of the blacklist of a guild.
     *
     * @param guildId the id of the guild
     * @return the list with all blacklisted words from the guild
     */
    public static ArrayList<String> getBlackListWordsFromGuild(String guildId) {
        ArrayList<String> words = new ArrayList<>();
        ResultSet resultSet = sql.query("SELECT * FROM Blacklist WHERE GUILDID='" + guildId + "';");
        try {
            while (resultSet.next()) {
                words.add(resultSet.getString("WORD"));
            }

            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return words;
    }

    // WARNINGS

    /**
     * Checks if a user has any warnings on the guild.
     *
     * @param guildId the id of the guild
     * @param userId  the id of the user
     * @return true if the user has any warnings on the guild and false otherwise
     */
    public static boolean isWarningSet(String guildId, String userId) {
        ResultSet resultSet = sql.query("SELECT * FROM Warnings WHERE GUILDID='" + guildId + "' AND USERID='" + userId + "';");
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

    /**
     * Gets the amount of warnings of a user of a guild.
     *
     * @param guildId the id of the guild
     * @param userId  the id of the user
     * @return the amount of warnings the user has on this guild
     */
    public static int getWarnings(String guildId, String userId) {
        ResultSet resultSet = sql.query("SELECT * FROM Warnings WHERE GUILDID='" + guildId + "' AND USERID='" + userId + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("WARNINGS");
            }

            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return 0;
    }

    /**
     * Sets the amount of warnings for a user of the guild.
     *
     * @param guildId  the id of the guild
     * @param userId   the id of the user
     * @param warnings the amount of warnings you want to set
     */
    public static void setWarnings(String guildId, String userId, int warnings) {
        if (isWarningSet(guildId, userId))
            sql.update("UPDATE Warnings SET WARNINGS=" + warnings + " WHERE GUILDID='" + guildId + "' AND USERID='" + userId + "';");
        else
            sql.update("INSERT INTO Warnings (GUILDID, USERID, WARNINGS) VALUES ('" + guildId + "','" + userId + "'," + warnings + ");");
    }
}