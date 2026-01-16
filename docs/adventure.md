# Adventure API

This document covers adventure gameplay features like instance discovery and treasure chests.

## Adventure Events

Events related to adventure gameplay features.

### Event Summary

| Class | Package | Description |
|-------|---------|-------------|
| `DiscoverInstanceEvent` | `com.hypixel.hytale.builtin.instances.event` | Base class for instance discovery (ECS) |
| `DiscoverInstanceEvent.Display` | `com.hypixel.hytale.builtin.instances.event` | Instance discovery UI display (ECS, cancellable) |
| `TreasureChestOpeningEvent` | `com.hypixel.hytale.builtin.adventure.objectives.events` | Player opens treasure chest (keyed by String) |

---

## DiscoverInstanceEvent (Base Class)

**Package:** `com.hypixel.hytale.builtin.instances.event`

Abstract base class for instance discovery events. Extends `EcsEvent`.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getInstanceWorldUuid()` | `UUID` | UUID of the discovered instance world |
| `getDiscoveryConfig()` | `InstanceDiscoveryConfig` | Configuration for this discovery |

---

## DiscoverInstanceEvent.Display

**Package:** `com.hypixel.hytale.builtin.instances.event`

ECS event fired to display instance discovery in the UI. Extends `DiscoverInstanceEvent`, implements `ICancellableEcsEvent`.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getInstanceWorldUuid()` | `UUID` | UUID of the discovered instance world |
| `getDiscoveryConfig()` | `InstanceDiscoveryConfig` | Configuration for this discovery |
| `shouldDisplay()` | `boolean` | Whether the discovery should be displayed |
| `setDisplay(boolean)` | `void` | Control whether to display the discovery |
| `isCancelled()` | `boolean` | Whether the event is cancelled |
| `setCancelled(boolean)` | `void` | Cancel or uncancel the event |

### Usage Example

Handle instance discovery using an `EntityEventSystem`:

```java
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.builtin.instances.event.DiscoverInstanceEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class InstanceDiscoverySystem extends EntityEventSystem<EntityStore, DiscoverInstanceEvent.Display> {

    public InstanceDiscoverySystem() {
        super(DiscoverInstanceEvent.Display.class);
    }

    @Override
    public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                       Store<EntityStore> store, CommandBuffer<EntityStore> buffer,
                       DiscoverInstanceEvent.Display event) {
        System.out.println("Instance discovered: " + event.getInstanceWorldUuid());

        // Optionally suppress the discovery display
        // event.setDisplay(false);

        // Or cancel entirely
        // event.setCancelled(true);
    }

    @Override
    public Query<EntityStore> getQuery() {
        return null; // Or a specific component type
    }
}
```

### Registration

```java
@Override
protected void setup() {
    getEntityStoreRegistry().registerSystem(new InstanceDiscoverySystem());
}
```

---

## TreasureChestOpeningEvent

**Package:** `com.hypixel.hytale.builtin.adventure.objectives.events`

Fired when a player opens a treasure chest. Implements `IEvent<String>` (keyed by String).

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getObjectiveUUID()` | `UUID` | UUID of the adventure objective |
| `getChestUUID()` | `UUID` | UUID of the treasure chest being opened |
| `getPlayerRef()` | `Ref<EntityStore>` | Reference to the player opening the chest |
| `getStore()` | `Store<EntityStore>` | Entity store for accessing components |

### Usage Example

Since this is a keyed event (keyed by String), use `registerGlobal()` to catch all chest openings:

```java
import com.hypixel.hytale.builtin.adventure.objectives.events.TreasureChestOpeningEvent;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;

@Override
protected void setup() {
    // Listen for all treasure chest openings
    getEventRegistry().registerGlobal(TreasureChestOpeningEvent.class, event -> {
        var store = event.getStore();
        var playerRef = event.getPlayerRef();

        Player player = store.getComponent(playerRef, Player.getComponentType());
        if (player != null) {
            player.sendMessage(Message.raw("You opened a treasure chest!"));
        }

        System.out.println("Chest " + event.getChestUUID() +
                           " opened for objective " + event.getObjectiveUUID());
    });
}
```

---

## Complete Adventure System Example

```java
import com.hypixel.hytale.builtin.adventure.objectives.events.TreasureChestOpeningEvent;
import com.hypixel.hytale.builtin.instances.event.DiscoverInstanceEvent;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class AdventurePlugin extends JavaPlugin {

    public AdventurePlugin(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        // Register ECS system for instance discovery
        getEntityStoreRegistry().registerSystem(new InstanceDiscoverySystem());

        // Register event listener for treasure chests
        getEventRegistry().registerGlobal(TreasureChestOpeningEvent.class, this::onChestOpen);
    }

    private void onChestOpen(TreasureChestOpeningEvent event) {
        var store = event.getStore();
        var playerRef = event.getPlayerRef();

        Player player = store.getComponent(playerRef, Player.getComponentType());
        if (player != null) {
            player.sendMessage(
                Message.raw("Treasure found!")
                    .bold(true)
                    .color("#FFD700")
            );
        }
    }

    // Inner class for instance discovery handling
    public static class InstanceDiscoverySystem
            extends EntityEventSystem<EntityStore, DiscoverInstanceEvent.Display> {

        public InstanceDiscoverySystem() {
            super(DiscoverInstanceEvent.Display.class);
        }

        @Override
        public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                           Store<EntityStore> store, CommandBuffer<EntityStore> buffer,
                           DiscoverInstanceEvent.Display event) {
            // Log the discovery
            System.out.println("Player discovered instance: " + event.getInstanceWorldUuid());

            // Could customize display behavior here
            // event.setDisplay(false); // Suppress default UI
        }

        @Override
        public Query<EntityStore> getQuery() {
            return Player.getComponentType();
        }
    }
}
```
