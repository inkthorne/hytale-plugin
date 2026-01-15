# Permissions API

## PermissionHolder
**Package:** `com.hypixel.hytale.server.core.permissions`

Interface for entities that can have permissions. `Player` implements this.

### Methods
```java
boolean hasPermission(String permission)
boolean hasPermission(String permission, boolean defaultValue)
```

## Usage

### Check Permission in Command
```java
@Override
protected void execute(CommandContext ctx, Store<EntityStore> store,
                      Ref<EntityStore> ref, PlayerRef playerRef, World world) {
    Player player = store.getComponent(ref, Player.getComponentType());

    if (player.hasPermission("myplugin.admin")) {
        // Admin-only action
        playerRef.sendMessage(Message.raw("Admin access granted"));
    } else {
        playerRef.sendMessage(Message.raw("Permission denied"));
    }
}
```

### With Default Value
```java
// Returns true if permission not explicitly set
boolean canUse = player.hasPermission("myplugin.feature", true);

// Returns false if permission not explicitly set
boolean isAdmin = player.hasPermission("myplugin.admin", false);
```

## Command Permissions

Commands can require permissions using `AbstractCommand`:

```java
public class AdminCommand extends AbstractPlayerCommand {
    public AdminCommand() {
        super("admin", "Admin-only command");
        requirePermission("myplugin.admin");  // Require this permission
    }
}
```

Players without the required permission won't be able to execute the command.
