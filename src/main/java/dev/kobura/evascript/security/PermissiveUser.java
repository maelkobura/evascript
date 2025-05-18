package dev.kobura.evascript.security;

public interface PermissiveUser {

    boolean hasPermission(String permission);
    String[] getPermissions();

}
