# Plugin Lifecycle API

## Class Hierarchy
```
PluginBase (abstract, implements CommandOwner)
  └── JavaPlugin (abstract)
        └── YourPlugin
```

## JavaPlugin
**Package:** `com.hypixel.hytale.server.core.plugin`

Your plugin must extend this class.

```java
public abstract class JavaPlugin extends PluginBase {
    public JavaPlugin(JavaPluginInit init);
    public Path getFile();
    public PluginClassLoader getClassLoader();
    public final PluginType getType();
}
```

### Required Constructor
```java
public YourPlugin(JavaPluginInit init) {
    super(init);
}
```

## PluginBase
**Package:** `com.hypixel.hytale.server.core.plugin`

Base class providing all plugin functionality.

### Lifecycle Methods
Override these to hook into plugin lifecycle:
```java
protected void setup();                       // Register commands, events, etc.
protected void setup0();                      // Internal setup (called by framework)
protected void start();                       // Called after setup
protected void start0();                      // Internal start (called by framework)
public CompletableFuture<Void> preLoad();     // Async pre-loading
protected void shutdown();                    // Clean up resources
protected void shutdown0(boolean graceful);   // Internal shutdown
```

### Registries (from PluginBase)
Access these via getter methods:
```java
getCommandRegistry()       // CommandRegistry - register commands
getEventRegistry()         // EventRegistry - register event listeners
getTaskRegistry()          // TaskRegistry - schedule tasks
getEntityRegistry()        // EntityRegistry - register entities
getBlockStateRegistry()    // BlockStateRegistry - register block states
getAssetRegistry()         // AssetRegistry - register assets
getEntityStoreRegistry()   // ComponentRegistryProxy<EntityStore>
getChunkStoreRegistry()    // ComponentRegistryProxy<ChunkStore>
getClientFeatureRegistry() // ClientFeatureRegistry
```

### Configuration
```java
// Load configuration from file with default fallback
<T> T withConfig(BuilderCodec<T> codec)
```

See [Codecs Documentation](codecs.md) for BuilderCodec details.

### Utility Methods
```java
getName()           // String - plugin name from manifest
getLogger()         // HytaleLogger - logging
getIdentifier()     // PluginIdentifier
getManifest()       // PluginManifest
getDataDirectory()  // Path - plugin data folder
getState()          // PluginState - current state
getBasePermission() // String - base permission node
isEnabled()         // boolean
isDisabled()        // boolean
```

## JavaPluginInit
**Package:** `com.hypixel.hytale.server.core.plugin`

Passed to plugin constructor by server. Do not instantiate yourself.

```java
public class JavaPluginInit extends PluginInit {
    public Path getFile();
    public PluginClassLoader getClassLoader();
    public boolean isInServerClassPath();
}
```

## Usage Example
```java
package com.example.myplugin;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

public class MyPlugin extends JavaPlugin {
    public MyPlugin(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        getCommandRegistry().registerCommand(new MyCommand());
        getLogger().atInfo().log("Plugin setup complete!");
    }
}
```

---

## Server Lifecycle Events

**Package:** `com.hypixel.hytale.server.core.event.events`

Events related to server lifecycle.

| Class | Description |
|-------|-------------|
| `BootEvent` | Server boot has completed |
| `ShutdownEvent` | Server is shutting down |
| `PrepareUniverseEvent` | Universe preparation phase |

### Usage Example

```java
import com.hypixel.hytale.server.core.event.events.BootEvent;
import com.hypixel.hytale.server.core.event.events.ShutdownEvent;

@Override
protected void setup() {
    // Listen for server boot completion
    getEventRegistry().register(BootEvent.class, event -> {
        getLogger().atInfo().log("Server has finished booting!");
    });

    // Listen for server shutdown
    getEventRegistry().register(ShutdownEvent.class, event -> {
        getLogger().atInfo().log("Server is shutting down, saving data...");
    });
}
```

---

## Plugin Events

**Package:** `com.hypixel.hytale.server.core.plugin.event`

Events related to plugin lifecycle. These are **keyed by plugin class** (`Class<? extends PluginBase>`).

### Event Summary

| Class | Description |
|-------|-------------|
| `PluginEvent` | Base class for plugin lifecycle events |
| `PluginSetupEvent` | Plugin setup has completed |

---

### PluginEvent (Base Class)

Abstract base class for plugin-related events. Keyed by plugin class.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getPlugin()` | `PluginBase` | The plugin this event relates to |

---

### PluginSetupEvent

Fired when a plugin's setup has completed. Extends `PluginEvent`.

### Constructor

```java
public PluginSetupEvent(PluginBase plugin)
```

### Inherited Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getPlugin()` | `PluginBase` | The plugin that completed setup |

### Usage Example

```java
import com.hypixel.hytale.server.core.plugin.event.PluginSetupEvent;

@Override
protected void setup() {
    // Listen for when any plugin completes setup
    getEventRegistry().registerGlobal(PluginSetupEvent.class, event -> {
        System.out.println("Plugin setup completed: " + event.getPlugin());
    });

    // Listen for a specific plugin's setup (keyed by plugin class)
    getEventRegistry().register(PluginSetupEvent.class, MyPlugin.class, event -> {
        System.out.println("MyPlugin setup completed!");
    });
}
```

---

## Complete Lifecycle Example

```java
import com.hypixel.hytale.server.core.event.events.BootEvent;
import com.hypixel.hytale.server.core.event.events.ShutdownEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.plugin.event.PluginSetupEvent;

public class MyPlugin extends JavaPlugin {

    public MyPlugin(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        // Register commands
        getCommandRegistry().registerCommand(new MyCommand());

        // Register lifecycle event listeners
        getEventRegistry().register(BootEvent.class, event -> {
            getLogger().atInfo().log("Server boot complete - initializing plugin features");
            // Initialize features that require the server to be fully booted
        });

        getEventRegistry().register(ShutdownEvent.class, event -> {
            getLogger().atInfo().log("Saving plugin data before shutdown...");
            // Save any persistent data
        });

        // Listen for other plugins completing setup
        getEventRegistry().registerGlobal(PluginSetupEvent.class, event -> {
            if (event.getPlugin() != this) {
                getLogger().atInfo().log("Another plugin finished setup: " + event.getPlugin().getName());
            }
        });

        getLogger().atInfo().log("Plugin setup complete!");
    }

    @Override
    protected void shutdown() {
        getLogger().atInfo().log("Plugin shutdown method called");
        // Clean up plugin resources
    }
}
