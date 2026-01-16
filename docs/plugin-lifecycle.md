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
