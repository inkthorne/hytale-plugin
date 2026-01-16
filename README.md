# Hytale Plugin Template

A starter template for creating Hytale server plugins using Java 25 and Gradle.

## Features

- Pre-configured Gradle build with Shadow JAR plugin
- Example `/hello` command implementation
- Complete API documentation in `docs/` folder
- Build and deployment scripts

## Quick Start

### Prerequisites

- Java 25 JDK
- Hytale server installation (for `HytaleServer.jar`)

### Setup

1. **Use this template** - Click "Use this template" on GitHub
2. **Clone your new repository**
3. **Copy HytaleServer.jar** - Copy from your Hytale installation:
   ```
   %APPDATA%\Hytale\install\release\package\game\latest\Server\HytaleServer.jar
   ```
   to the `libs/` folder

4. **Customize the template** - Find and replace these placeholders:
   - `com.example.myplugin` - Your package name (e.g., `com.yourname.pluginname`)
   - `ExamplePlugin` - Your plugin class name
   - `ExampleCommand` - Your command class name
   - `YourName` - Your name in `manifest.json`
   - `example-plugin` - Your JAR name in `build.gradle`

5. **Rename directories** - Move source files to match your package name

### Build

```batch
build.bat          REM Build the plugin JAR
clean.bat          REM Clean build artifacts
deploy.bat         REM Copy JAR to Hytale mods folder
```

Or use Gradle directly:
```batch
gradlew shadowJar
```

### Deploy

The built JAR is output to `build/libs/`. Copy it to:
```
%APPDATA%\Hytale\UserData\Mods\
```

## Project Structure

```
hytale-plugin-template/
├── src/main/java/          # Plugin source code
├── src/main/resources/     # manifest.json
├── docs/                   # Hytale API documentation
├── libs/                   # HytaleServer.jar (not included)
├── build.gradle            # Gradle build configuration
└── CLAUDE.md               # AI assistant instructions
```

## API Documentation

See the `docs/` folder for extracted Hytale API documentation:

- [Overview](docs/overview.md) - Start here for API navigation
- [Plugin Lifecycle](docs/plugin-lifecycle.md)
- [Commands](docs/commands.md)
- [Entities](docs/entities.md)
- [Player](docs/player.md)
- [NPC](docs/npc.md)
- [World](docs/world.md)
- [Blocks](docs/blocks.md)
- [Prefabs](docs/prefabs.md)
- [Events](docs/events.md)
- [Components (ECS)](docs/components.md)
- [Combat](docs/combat.md)
- [Projectiles](docs/projectiles.md)
- [Collision](docs/collision.md)
- [Adventure](docs/adventure.md)
- [Permissions](docs/permissions.md)
- [Inventory](docs/inventory.md)
- [Tasks](docs/tasks.md)
- [UI](docs/ui.md)
- [Assets](docs/assets.md)
- [Codecs](docs/codecs.md)
- [Math](docs/math.md)

## Creating Commands

```java
public class MyCommand extends AbstractPlayerCommand {
    public MyCommand() {
        super("mycommand", "Description of command");
    }

    @Override
    protected void execute(@Nonnull CommandContext ctx,
                          @Nonnull Store<EntityStore> store,
                          @Nonnull Ref<EntityStore> ref,
                          @Nonnull PlayerRef playerRef,
                          @Nonnull World world) {
        playerRef.sendMessage(Message.raw("Hello!"));
    }
}
```

Register in your plugin's `setup()` method:
```java
@Override
protected void setup() {
    getCommandRegistry().registerCommand(new MyCommand());
}
```

## License

MIT License - See [LICENSE](LICENSE) file.
