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
void addAliases(String... aliases)              // Add command aliases
void addSubCommand(AbstractCommand cmd)         // Add subcommand
void requirePermission(String permission)       // Require permission
String getName()                                // Get command name
String getDescription()                         // Get description
boolean hasPermission(CommandSender sender)     // Check permission
Message getUsageString(CommandSender sender)    // Get usage help
```

## AbstractAsyncCommand
**Package:** `com.hypixel.hytale.server.core.command.system.basecommands`

Base class for async commands. All player commands inherit from this.

```java
CompletableFuture<Void> runAsync(CommandContext ctx, Runnable task, Executor executor)
```
