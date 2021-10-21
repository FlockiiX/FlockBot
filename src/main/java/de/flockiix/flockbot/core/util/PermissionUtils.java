package de.flockiix.flockbot.core.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.util.Set;
import java.util.stream.Collectors;

public class PermissionUtils {
    public static Set<Permission> getMissingPermissions(Member selfMember, Set<Permission> requiredPermissions) {
        return requiredPermissions.stream()
                .filter(permission -> !selfMember.getPermissions().contains(permission))
                .collect(Collectors.toSet());
    }
}