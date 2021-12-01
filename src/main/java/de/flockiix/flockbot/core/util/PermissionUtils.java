package de.flockiix.flockbot.core.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.util.Set;
import java.util.stream.Collectors;

public class PermissionUtils {
    /**
     * Returns a Set with the missing permissions of the given user and required permissions.
     *
     * @param member              the member you want to check the missing permissions
     * @param requiredPermissions the required permissions of the user
     * @return a set with the missing permissions
     */
    public static Set<Permission> getMissingPermissions(Member member, Set<Permission> requiredPermissions) {
        return requiredPermissions.stream()
                .filter(permission -> !member.getPermissions().contains(permission))
                .collect(Collectors.toSet());
    }
}