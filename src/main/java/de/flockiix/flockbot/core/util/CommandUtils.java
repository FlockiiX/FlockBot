package de.flockiix.flockbot.core.util;

import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

public class CommandUtils {
    /**
     * Map with userId, commandName and coolDown
     */
    public static Map<String, Map<String, Long>> commandCoolDowns = new HashedMap<>();

    /**
     * Checks if the command contains a CoolDown for the given command and user.
     *
     * @param commandName the commandName of the command
     * @param userId      the userId of the user
     * @return true if the command contains a CoolDown for the user and false otherwise
     */
    public static boolean commandContainsCoolDown(String commandName, String userId) {
        return commandCoolDowns.computeIfAbsent(commandName, name -> new HashedMap<>()).containsKey(userId);
    }

    /**
     * Gets the CoolDown milliseconds for the given command and user.
     *
     * @param commandName the commandName of the command
     * @param userId      the userId of the user
     * @return the left milliseconds of the CoolDown from the given command and user
     */
    public static long getCommandCoolDown(String commandName, String userId) {
        return commandCoolDowns.computeIfAbsent(commandName, name -> new HashedMap<>()).computeIfAbsent(userId, user -> System.currentTimeMillis());
    }

    /**
     * Adds a CoolDown from the given command to the given user.
     *
     * @param commandName the commandName of the command
     * @param userId      the userId of the user
     */
    public static void addCommandCoolDown(String commandName, String userId) {
        final Map<String, Long> cmdCoolDowns = commandCoolDowns.get(commandName);
        cmdCoolDowns.put(userId, System.currentTimeMillis());
        commandCoolDowns.put(commandName, cmdCoolDowns);
    }
}
