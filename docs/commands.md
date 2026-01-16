# Commands API

## Class Hierarchy
```
AbstractCommand
  └── AbstractAsyncCommand
        ├── AbstractPlayerCommand  (use this for player commands)
        ├── AbstractWorldCommand
        └── AbstractTargetPlayerCommand
```

## AbstractPlayerCommand
**Package:** `com.hypixel.hytale.server.core.command.system.basecommands`

Most common base class for player-executed commands.

### Constructors
```java
AbstractPlayerCommand(String name, String description)
AbstractPlayerCommand(String name, String description, boolean hidden)
AbstractPlayerCommand(String name)  // no description
```

### Abstract Method to Implement
```java
protected abstract void execute(
    CommandContext commandContext,
    Store<EntityStore> store,
    Ref<EntityStore> ref,
    PlayerRef playerRef,
    World world
);
```

### Usage Example
```java
public class MyCommand extends AbstractPlayerCommand {
    public MyCommand() {
        super("mycommand", "Does something cool");
    }

    @Override
    protected void execute(CommandContext ctx, Store<EntityStore> store,
                          Ref<EntityStore> ref, PlayerRef playerRef, World world) {
        playerRef.sendMessage(Message.raw("Hello!"));
    }
}
```

## CommandContext
**Package:** `com.hypixel.hytale.server.core.command.system`

Provides access to command arguments and sender information.

### Methods
```java
<T> T get(Argument<?, T> arg)           // Get argument value
String[] getInput(Argument<?, ?> arg)   // Get raw input for argument
boolean provided(Argument<?, ?> arg)    // Check if optional arg was provided
String getInputString()                 // Full input string
void sendMessage(Message msg)           // Send message to sender
boolean isPlayer()                      // Check if sender is player
<T extends CommandSender> T senderAs(Class<T> clazz)  // Cast sender
Ref<EntityStore> senderAsPlayerRef()    // Get player ref
CommandSender sender()                  // Get sender
AbstractCommand getCalledCommand()      // Get command that was called
```

## CommandRegistry
**Package:** `com.hypixel.hytale.server.core.command.system`

Register commands with the server.

```java
CommandRegistration registerCommand(AbstractCommand command)
```

### Registration Example
```java
@Override
protected void setup() {
    getCommandRegistry().registerCommand(new MyCommand());
}
```

## AbstractCommand Arguments
**Package:** `com.hypixel.hytale.server.core.command.system`

Define command arguments in your command class.

### Argument Types
```java
// Required argument (must be provided)
withRequiredArg(String name, String description, ArgumentType<D> type)

// Optional argument (may be omitted)
withOptionalArg(String name, String description, ArgumentType<D> type)

// Default argument (uses default if omitted)
withDefaultArg(String name, String description, ArgumentType<D> type, D defaultValue, String defaultDisplay)

// Flag argument (boolean switch like --verbose)
withFlagArg(String name, String description)

// List variants
withListRequiredArg(...)
withListOptionalArg(...)
withListDefaultArg(...)
```

### Other AbstractCommand Methods
```java
// Aliases & Subcommands
void addAliases(String... aliases)              // Add command aliases
void addSubCommand(AbstractCommand cmd)         // Add subcommand
void addUsageVariant(AbstractCommand cmd)       // Add usage variant

// Command Info
String getName()                                // Get command name
String getDescription()                         // Get description
String getFullyQualifiedName()                  // Get full command path (e.g., "parent subcommand")
Message getUsageString(CommandSender sender)    // Get usage help
Message getUsageShort(CommandSender sender, boolean showAliases)  // Get short usage

// Permissions
void requirePermission(String permission)       // Require permission
void setPermissionGroups(String... groups)      // Set permission groups
void setPermissionGroup(GameMode mode)          // Set permission by game mode
boolean hasPermission(CommandSender sender)     // Check permission
boolean canGeneratePermission()                 // Check if can auto-generate permission
String generatePermissionNode()                 // Generate permission node string

// Configuration
void setUnavailableInSingleplayer(boolean unavailable)  // Mark multiplayer-only
void setAllowsExtraArguments(boolean allows)    // Allow trailing arguments
void setOwner(CommandOwner owner)               // Set owning plugin

// Matching
boolean matches(String input, String alias, int depth)  // Check if input matches command
```

## AbstractAsyncCommand
**Package:** `com.hypixel.hytale.server.core.command.system.basecommands`

Base class for async commands. All player commands inherit from this.

```java
// Execute async (override this for custom async commands)
protected abstract CompletableFuture<Void> executeAsync(CommandContext context)

// Run task asynchronously
CompletableFuture<Void> runAsync(CommandContext ctx, Runnable task, Executor executor)
```

---

## AbstractWorldCommand
**Package:** `com.hypixel.hytale.server.core.command.system.basecommands`

Base class for commands that operate on a world context.

### Constructors
```java
AbstractWorldCommand(String name)
AbstractWorldCommand(String name, String description)
AbstractWorldCommand(String name, String description, boolean allowsExtraArgs)
```

### Abstract Method to Implement
```java
protected abstract void execute(
    CommandContext commandContext,
    Store<EntityStore> store,
    World world
);
```

---

## AbstractTargetPlayerCommand
**Package:** `com.hypixel.hytale.server.core.command.system.basecommands`

Base class for commands that target another player (e.g., admin commands).

### Constructors
```java
AbstractTargetPlayerCommand(String name)
AbstractTargetPlayerCommand(String name, String description)
AbstractTargetPlayerCommand(String name, String description, boolean allowsExtraArgs)
```

### Abstract Method to Implement
```java
protected abstract void execute(
    CommandContext commandContext,
    Store<EntityStore> store,
    Ref<EntityStore> targetRef,
    PlayerRef targetPlayer,
    World world
);
```

### Usage Example
```java
public class KickCommand extends AbstractTargetPlayerCommand {
    public KickCommand() {
        super("kick", "Kick a player from the server");
        requirePermission("server.kick");
    }

    @Override
    protected void execute(CommandContext ctx, Store<EntityStore> store,
                          Ref<EntityStore> targetRef, PlayerRef targetPlayer, World world) {
        // targetPlayer is the player being kicked (not the sender)
        targetPlayer.sendMessage(Message.raw("You have been kicked!"));
        // Kick logic here
    }
}
```
