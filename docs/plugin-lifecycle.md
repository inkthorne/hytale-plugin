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
protected void setup();     // Register commands, events, etc.
protected void start();     // Called after setup
public CompletableFuture<Void> preLoad();  // Async pre-loading
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
        getLogger().info("Plugin setup complete!");
    }
}
```
