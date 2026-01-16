# Events API

## EventRegistry
**Package:** `com.hypixel.hytale.server.core.event`

Register event listeners. Access via `getEventRegistry()` in your plugin.

### Basic Registration
```java
// Simple event listener (no key)
<EventType extends IBaseEvent<Void>> EventRegistration<Void, EventType>
    register(Class<? super EventType> eventClass, Consumer<EventType> handler)

// With priority
<EventType extends IBaseEvent<Void>> EventRegistration<Void, EventType>
    register(EventPriority priority, Class<? super EventType> eventClass, Consumer<EventType> handler)

// With numeric priority (lower = earlier)
<EventType extends IBaseEvent<Void>> EventRegistration<Void, EventType>
    register(short priority, Class<? super EventType> eventClass, Consumer<EventType> handler)
```

### Keyed Registration
For events filtered by a key (e.g., specific block type):
```java
<KeyType, EventType extends IBaseEvent<KeyType>> EventRegistration<KeyType, EventType>
    register(Class<? super EventType> eventClass, KeyType key, Consumer<EventType> handler)

<KeyType, EventType extends IBaseEvent<KeyType>> EventRegistration<KeyType, EventType>
    register(EventPriority priority, Class<? super EventType> eventClass, KeyType key, Consumer<EventType> handler)
```

### Async Registration
For async event handlers (return CompletableFuture):
```java
<EventType extends IAsyncEvent<Void>> EventRegistration<Void, EventType>
    registerAsync(Class<? super EventType> eventClass,
                  Function<CompletableFuture<EventType>, CompletableFuture<EventType>> handler)

<EventType extends IAsyncEvent<Void>> EventRegistration<Void, EventType>
    registerAsync(EventPriority priority, Class<? super EventType> eventClass,
                  Function<CompletableFuture<EventType>, CompletableFuture<EventType>> handler)
```

### Global Registration
Listens to events regardless of key:
```java
<KeyType, EventType extends IBaseEvent<KeyType>> EventRegistration<KeyType, EventType>
    registerGlobal(Class<? super EventType> eventClass, Consumer<EventType> handler)

<KeyType, EventType extends IBaseEvent<KeyType>> EventRegistration<KeyType, EventType>
    registerGlobal(EventPriority priority, Class<? super EventType> eventClass, Consumer<EventType> handler)

// Async global
<KeyType, EventType extends IAsyncEvent<KeyType>> EventRegistration<KeyType, EventType>
    registerAsyncGlobal(Class<? super EventType> eventClass,
                        Function<CompletableFuture<EventType>, CompletableFuture<EventType>> handler)
```

### Unhandled Registration
Listens only if no other handler processed the event:
```java
<KeyType, EventType extends IBaseEvent<KeyType>> EventRegistration<KeyType, EventType>
    registerUnhandled(Class<? super EventType> eventClass, Consumer<EventType> handler)

<KeyType, EventType extends IAsyncEvent<KeyType>> EventRegistration<KeyType, EventType>
    registerAsyncUnhandled(Class<? super EventType> eventClass,
                           Function<CompletableFuture<EventType>, CompletableFuture<EventType>> handler)
```

## EventPriority
**Package:** `com.hypixel.hytale.server.core.event`

Use to control handler execution order. Lower priority number executes first.

### Enum Values
```java
public enum EventPriority {
    FIRST,   // Executes first (lowest priority number)
    EARLY,   // Early execution
    NORMAL,  // Default priority
    LATE,    // Late execution
    LAST     // Executes last (highest priority number)

    short getValue()  // Get numeric priority value
}
```

### Usage
```java
// Using enum
getEventRegistry().register(EventPriority.EARLY, PlayerConnectEvent.class, event -> {
    // Handle early
});

// Using raw short (lower = earlier)
getEventRegistry().register((short) 100, PlayerConnectEvent.class, event -> {
    // Custom priority
});
```

## Available Event Classes

### Player Events
**Package:** `com.hypixel.hytale.server.core.event.events.player`

| Class | Description |
|-------|-------------|
| `PlayerConnectEvent` | Player connects to server |
| `PlayerDisconnectEvent` | Player disconnects from server |
| `PlayerReadyEvent` | Player is ready (fully loaded) |
| `PlayerChatEvent` | Player sends a chat message |
| `PlayerInteractEvent` | Player interacts with something |
| `PlayerCraftEvent` | Player crafts an item |
| `PlayerMouseButtonEvent` | Player mouse button input |
| `PlayerMouseMotionEvent` | Player mouse movement |
| `AddPlayerToWorldEvent` | Player added to a world |
| `DrainPlayerFromWorldEvent` | Player removed from a world |
| `PlayerSetupConnectEvent` | Player setup phase connect |
| `PlayerSetupDisconnectEvent` | Player setup phase disconnect |

### ECS/Gameplay Events
**Package:** `com.hypixel.hytale.server.core.event.events.ecs`

| Class | Description |
|-------|-------------|
| `UseBlockEvent` | Block is used (has `Pre` and `Post` variants) |
| `BreakBlockEvent` | Block is broken |
| `PlaceBlockEvent` | Block is placed |
| `DamageBlockEvent` | Block takes damage |
| `DropItemEvent` | Item is dropped (has `Drop` and `PlayerRequest` variants) |
| `InteractivelyPickupItemEvent` | Item is picked up interactively |
| `SwitchActiveSlotEvent` | Active inventory slot changes |
| `ChangeGameModeEvent` | Game mode changes |
| `CraftRecipeEvent` | Recipe is crafted (has `Pre` and `Post` variants) |
| `DiscoverZoneEvent` | Zone is discovered |

### Entity Events
**Package:** `com.hypixel.hytale.server.core.event.events.entity`

| Class | Description |
|-------|-------------|
| `EntityEvent` | Base entity event |
| `EntityRemoveEvent` | Entity is removed |
| `LivingEntityInventoryChangeEvent` | Living entity inventory changes |
| `LivingEntityUseBlockEvent` | ~~Living entity uses a block~~ **DEPRECATED** - use `UseBlockEvent` instead |

### Permission Events
**Package:** `com.hypixel.hytale.server.core.event.events.permissions`

| Class | Description |
|-------|-------------|
| `PlayerGroupEvent` | Player group changes (`Added`/`Removed`) |
| `PlayerPermissionChangeEvent` | Player permissions change |
| `GroupPermissionChangeEvent` | Group permissions change |

### Server Lifecycle Events
**Package:** `com.hypixel.hytale.server.core.event.events`

| Class | Description |
|-------|-------------|
| `BootEvent` | Server boot |
| `ShutdownEvent` | Server shutdown |
| `PrepareUniverseEvent` | Universe preparation |

## Keyed vs Non-Keyed Events

Some events are "keyed" (filtered by a key type like String or item type). Use:
- `register()` for non-keyed events (e.g., `PlayerConnectEvent`)
- `registerGlobal()` for keyed events when you want ALL events regardless of key (e.g., `PlayerInteractEvent`)
- `register(EventClass, key, handler)` for keyed events filtered to a specific key

**Keyed events** (use `registerGlobal` or provide a key):
- `PlayerInteractEvent` (keyed by String)
- `PlayerChatEvent` (keyed by String)

**Non-keyed events** (use `register`):
- `PlayerConnectEvent`
- `PlayerDisconnectEvent`

## Usage Example
```java
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerInteractEvent;

@Override
protected void setup() {
    // Non-keyed event: use register()
    getEventRegistry().register(PlayerConnectEvent.class, event -> {
        event.getPlayerRef().sendMessage(Message.raw("Welcome!"));
    });

    // Keyed event: use registerGlobal() to catch ALL interactions
    getEventRegistry().registerGlobal(PlayerInteractEvent.class, event -> {
        event.getPlayer().sendMessage(Message.raw("You interacted!"));
    });
}
```

## ECS Events (EntityEventSystem)

ECS events like `PlaceBlockEvent` and `BreakBlockEvent` don't have direct player access.
To handle them with entity context, create an `EntityEventSystem` and register it with `getEntityStoreRegistry()`.

### Creating an EntityEventSystem

```java
package inkthorne.experiment.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class PlaceBlockEventSystem extends EntityEventSystem<EntityStore, PlaceBlockEvent> {

    public PlaceBlockEventSystem() {
        super(PlaceBlockEvent.class);
    }

    @Override
    public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                       Store<EntityStore> store, CommandBuffer<EntityStore> buffer,
                       PlaceBlockEvent event) {
        // Get player component using chunk and index
        Player player = chunk.getComponent(index, Player.getComponentType());
        if (player != null) {
            player.sendMessage(Message.raw("You placed a block!"));
        }
    }

    @Override
    public Query<EntityStore> getQuery() {
        // ComponentType implements Query, so return it directly
        return Player.getComponentType();
    }
}
```

### Registering the System

```java
@Override
protected void setup() {
    // Register ECS event system
    getEntityStoreRegistry().registerSystem(new PlaceBlockEventSystem());
}
```
