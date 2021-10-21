package de.flockiix.flockbot.core.util;

import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

public class CommandUtils {
    public static Map<String, Map<String, Long>> commandCoolDowns = new HashedMap<>();

    public static boolean commandContainsCoolDown(String commandName, String userId) {
        return commandCoolDowns.computeIfAbsent(commandName, name -> new HashedMap<>()).containsKey(userId);
    }

    public static long getCommandCoolDown(String commandName, String userId) {
        return commandCoolDowns.computeIfAbsent(commandName, name -> new HashedMap<>()).computeIfAbsent(userId, user -> System.currentTimeMillis());
    }

    public static void addCommandCoolDown(String commandName, String userId) {
        final Map<String, Long> cmdCoolDowns = commandCoolDowns.get(commandName);
        cmdCoolDowns.put(userId, System.currentTimeMillis());
        commandCoolDowns.put(commandName, cmdCoolDowns);
    }
}
