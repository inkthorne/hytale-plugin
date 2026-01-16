# World API

## World
**Package:** `com.hypixel.hytale.server.core.universe.world`

Represents a game world. Extends TickingThread, implements Executor.

### Core Properties
```java
String getName()
boolean isAlive()
boolean isTicking()
void setTicking(boolean ticking)
boolean isPaused()
void setPaused(boolean paused)
long getTick()
HytaleLogger getLogger()
```

### Configuration
```java
WorldConfig getWorldConfig()
DeathConfig getDeathConfig()
GameplayConfig getGameplayConfig()
int getDaytimeDurationSeconds()
int getNighttimeDurationSeconds()
void setTps(int tps)
static void setTimeDilation(float dilation, ComponentAccessor<EntityStore> accessor)
```

### Players
```java
List<Player> getPlayers()
int getPlayerCount()
Collection<PlayerRef> getPlayerRefs()
void trackPlayerRef(PlayerRef ref)
void untrackPlayerRef(PlayerRef ref)

// Adding players
CompletableFuture<PlayerRef> addPlayer(PlayerRef ref)
CompletableFuture<PlayerRef> addPlayer(PlayerRef ref, Transform position)
CompletableFuture<PlayerRef> addPlayer(PlayerRef ref, Transform position, Boolean teleport, Boolean respawn)
CompletableFuture<Void> drainPlayersTo(World targetWorld)
```

### Entities
```java
Entity getEntity(UUID uuid)
Ref<EntityStore> getEntityRef(UUID uuid)
<T extends Entity> T spawnEntity(T entity, Vector3d position, Vector3f rotation)
<T extends Entity> T addEntity(T entity, Vector3d position, Vector3f rotation, AddReason reason)
```

### Chunks
```java
WorldChunk loadChunkIfInMemory(long chunkKey)
WorldChunk getChunkIfInMemory(long chunkKey)
WorldChunk getChunkIfLoaded(long chunkKey)
WorldChunk getChunkIfNonTicking(long chunkKey)
CompletableFuture<WorldChunk> getChunkAsync(long chunkKey)
CompletableFuture<WorldChunk> getNonTickingChunkAsync(long chunkKey)
```

### ECS Stores
```java
ChunkStore getChunkStore()
EntityStore getEntityStore()
```

### Messaging
```java
void sendMessage(Message msg)  // Broadcast to all players in world
```

### Features
```java
Map<ClientFeature, Boolean> getFeatures()
boolean isFeatureEnabled(ClientFeature feature)
void registerFeature(ClientFeature feature, boolean enabled)
void broadcastFeatures()
```

### Other
```java
ChunkLightingManager getChunkLighting()
WorldMapManager getWorldMapManager()
WorldPathConfig getWorldPathConfig()
WorldNotificationHandler getNotificationHandler()
EventRegistry getEventRegistry()
Path getSavePath()

// Lifecycle
CompletableFuture<World> init()
void stopIndividualWorld()
void execute(Runnable task)  // Execute on world thread
```

## Usage Example
```java
@Override
protected void execute(CommandContext ctx, Store<EntityStore> store,
                      Ref<EntityStore> ref, PlayerRef playerRef, World world) {
    // Get world info
    String worldName = world.getName();
    int playerCount = world.getPlayerCount();

    // Broadcast to all players in world
    world.sendMessage(Message.raw("Hello everyone!"));

    // Get all players
    for (Player player : world.getPlayers()) {
        player.sendMessage(Message.raw("Individual message"));
    }
}
```

---

## World Events

**Package:** `com.hypixel.hytale.server.core.universe.world.events`

Events related to world lifecycle (creation, removal, loading). These are **keyed by String** (world identifier).

### Event Summary

| Class | Description | Keyed | Cancellable |
|-------|-------------|-------|-------------|
| `WorldEvent` | Base class for world events | Yes (String) | - |
| `AddWorldEvent` | World is added to universe | Yes (String) | Yes |
| `RemoveWorldEvent` | World is being removed | Yes (String) | Yes |
| `StartWorldEvent` | World has started | Yes (String) | No |
| `AllWorldsLoadedEvent` | All worlds finished loading | No | No |

---

### WorldEvent (Base Class)

Abstract base class for world-related events.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getWorld()` | `World` | The world this event relates to |

---

### AddWorldEvent

Fired when a world is added to the universe. Implements `ICancellable`.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getWorld()` | `World` | The world being added |
| `isCancelled()` | `boolean` | Whether the event is cancelled |
| `setCancelled(boolean)` | `void` | Cancel or uncancel the event |

---

### RemoveWorldEvent

Fired when a world is being removed. Implements `ICancellable`.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getWorld()` | `World` | The world being removed |
| `getRemovalReason()` | `RemovalReason` | Why the world is being removed |
| `isCancelled()` | `boolean` | Whether the event is cancelled |
| `setCancelled(boolean)` | `void` | Cancel or uncancel the event |

**RemovalReason Enum:**
| Value | Description |
|-------|-------------|
| `GENERAL` | Normal removal |
| `EXCEPTIONAL` | Removal due to an error or exception |

---

### StartWorldEvent

Fired when a world starts (after loading completes).

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getWorld()` | `World` | The world that started |

---

### AllWorldsLoadedEvent

Fired once when all worlds have finished loading. This is a **non-keyed event** (use `register()` not `registerGlobal()`).

```java
// No additional methods - just signals all worlds are loaded
getEventRegistry().register(AllWorldsLoadedEvent.class, event -> {
    // All worlds are now loaded and ready
});
```

---

### World Events Registration Example

```java
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.world.events.*;

@Override
protected void setup() {
    // Listen to all world additions (keyed event)
    getEventRegistry().registerGlobal(AddWorldEvent.class, event -> {
        System.out.println("World added: " + event.getWorld());
    });

    // Listen to world removals
    getEventRegistry().registerGlobal(RemoveWorldEvent.class, event -> {
        if (event.getRemovalReason() == RemoveWorldEvent.RemovalReason.EXCEPTIONAL) {
            System.out.println("World removed due to error: " + event.getWorld());
        }
    });

    // Listen for world start
    getEventRegistry().registerGlobal(StartWorldEvent.class, event -> {
        System.out.println("World started: " + event.getWorld());
    });

    // Listen for all worlds loaded (non-keyed)
    getEventRegistry().register(AllWorldsLoadedEvent.class, event -> {
        System.out.println("All worlds have finished loading!");
    });
}
```

---

## Chunk Events

Events related to chunk saving and unloading.

### Event Summary

| Class | Package | Description | Cancellable |
|-------|---------|-------------|-------------|
| `ChunkEvent` | `...universe.world.events` | Base class for chunk events | - |
| `ChunkSaveEvent` | `...universe.world.events.ecs` | Chunk is being saved (ECS) | Yes |
| `ChunkUnloadEvent` | `...universe.world.events.ecs` | Chunk is being unloaded (ECS) | Yes |
| `MoonPhaseChangeEvent` | `...universe.world.events.ecs` | Moon phase changed (ECS) | No |

---

### ChunkEvent (Base Class)

**Package:** `com.hypixel.hytale.server.core.universe.world.events`

Abstract base class for chunk-related events. Keyed by String.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getChunk()` | `WorldChunk` | The chunk this event relates to |

---

### ChunkSaveEvent

**Package:** `com.hypixel.hytale.server.core.universe.world.events.ecs`

ECS event fired when a chunk is being saved. Extends `CancellableEcsEvent`.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getChunk()` | `WorldChunk` | The chunk being saved |
| `isCancelled()` | `boolean` | Whether save is cancelled |
| `setCancelled(boolean)` | `void` | Cancel or uncancel the save |

---

### ChunkUnloadEvent

**Package:** `com.hypixel.hytale.server.core.universe.world.events.ecs`

ECS event fired when a chunk is being unloaded. Extends `CancellableEcsEvent`.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getChunk()` | `WorldChunk` | The chunk being unloaded |
| `isCancelled()` | `boolean` | Whether unload is cancelled |
| `setCancelled(boolean)` | `void` | Cancel or uncancel the unload |
| `willResetKeepAlive()` | `boolean` | Whether keep-alive will be reset |
| `setResetKeepAlive(boolean)` | `void` | Control keep-alive reset behavior |

---

### MoonPhaseChangeEvent

**Package:** `com.hypixel.hytale.server.core.universe.world.events.ecs`

ECS event fired when the moon phase changes. Extends `EcsEvent` (not cancellable).

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getNewMoonPhase()` | `int` | The new moon phase index |

---

### Chunk Events Usage Notes

Chunk events (`ChunkSaveEvent`, `ChunkUnloadEvent`, `MoonPhaseChangeEvent`) extend `EcsEvent` rather than implementing `IEvent`. Handle them using an `EntityEventSystem`:

```java
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.universe.world.events.ecs.ChunkUnloadEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class ChunkUnloadSystem extends EntityEventSystem<EntityStore, ChunkUnloadEvent> {

    public ChunkUnloadSystem() {
        super(ChunkUnloadEvent.class);
    }

    @Override
    public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                       Store<EntityStore> store, CommandBuffer<EntityStore> buffer,
                       ChunkUnloadEvent event) {
        var worldChunk = event.getChunk();
        System.out.println("Chunk unloading: " + worldChunk);

        // Optionally prevent unload
        // event.setCancelled(true);
    }

    @Override
    public Query<EntityStore> getQuery() {
        // Return appropriate query for entities you want to match
        return null; // Or a specific component type
    }
}
```

Register it in your plugin:

```java
@Override
protected void setup() {
    getEntityStoreRegistry().registerSystem(new ChunkUnloadSystem());
}
```
