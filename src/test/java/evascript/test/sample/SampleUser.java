package evascript.test.sample;

import dev.kobura.evascript.security.PermissiveUser;

import java.util.Arrays;

public class SampleUser implements PermissiveUser {

    private String[] permissions = new String[]{"hello.world", "goodbye.world"};
    @Override
    public boolean hasPermission(String permission) {
        return Arrays.asList(permissions).contains(permission);
    }

    @Override
    public String[] getPermissions() {
        return permissions;
    }

    public String getName() {
        return "Roberto";
    }
}
