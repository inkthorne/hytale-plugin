# Prefabs API

Prefabs are pre-defined entity templates that can be spawned into the world. They allow consistent entity creation with predefined components and properties.

## Overview
**Package:** `com.hypixel.hytale.server.core.prefab`

Prefabs define entity blueprints that can be instantiated at runtime.

---

## PrefabStore
**Package:** `com.hypixel.hytale.server.core.prefab`

Central storage for prefab definitions.

```java
// Get prefab by ID
Prefab getPrefab(String prefabId)

// Check if prefab exists
boolean hasPrefab(String prefabId)

// Get all prefab IDs
Set<String> getPrefabIds()
```

---

## Prefab Class

Represents an entity template.

```java
public class Prefab {
    // Get prefab identifier
    String getId()

    // Get the holder (entity blueprint)
    Holder<EntityStore> getHolder()

    // Get components defined in prefab
    <T extends Component<EntityStore>> T getComponent(ComponentType<EntityStore, T> type)
}
```

---

## Prefab Selection

### PrefabSelector
**Package:** `com.hypixel.hytale.server.core.prefab.selection`

Select prefabs based on criteria.

```java
// Direct selection by ID
PrefabSelector.byId(String prefabId)

// Random selection from list
PrefabSelector.random(List<String> prefabIds)

// Weighted random selection
PrefabSelector.weighted(Map<String, Float> weightedPrefabs)
```

---

## Spawning Entities from Prefabs

### Via World
```java
// Spawn prefab at position
Prefab prefab = prefabStore.getPrefab("hytale:zombie");
Entity entity = world.spawnEntity(
    prefab.getHolder(),
    new Vector3d(x, y, z),
    new Vector3f(0, yaw, 0)
);
```

### Via Store
```java
// Get prefab holder and add to store
Prefab prefab = prefabStore.getPrefab("my_prefab");
Holder<EntityStore> holder = prefab.getHolder().clone();

// Optionally modify holder
holder.putComponent(SomeComponent.getComponentType(), new SomeComponent());

// Add to store
Ref<EntityStore> entityRef = store.addEntity(holder, AddReason.SPAWN);
```

---

## Custom Prefab Registration

Register custom prefabs during plugin setup:

```java
@Override
protected void setup() {
    // Create prefab holder
    Holder<EntityStore> holder = new Holder<>();
    holder.addComponent(MyComponent.getComponentType(), new MyComponent());
    holder.addComponent(TransformComponent.getComponentType(), new TransformComponent());

    // Register prefab
    // Note: Actual registration API may vary based on server version
}
```

---

## Prefab JSON Format

Prefabs are typically defined in JSON:

```json
{
    "id": "myplugin:custom_entity",
    "components": {
        "transform": {
            "scale": [1.0, 1.0, 1.0]
        },
        "health": {
            "maxHealth": 100,
            "currentHealth": 100
        },
        "custom_data": {
            "value": 42
        }
    }
}
```

---

## Usage Examples

### Spawn NPC at Location
```java
@Override
protected void execute(CommandContext ctx, Store<EntityStore> store,
                      Ref<EntityStore> ref, PlayerRef playerRef, World world) {
    // Get player position
    Transform playerTransform = playerRef.getTransform();
    Vector3d spawnPos = playerTransform.getPosition().add(2, 0, 0);

    // Get prefab
    Prefab npcPrefab = prefabStore.getPrefab("hytale:villager");

    // Spawn entity
    Entity npc = world.spawnEntity(
        npcPrefab.getHolder(),
        spawnPos,
        new Vector3f(0, 0, 0)
    );

    playerRef.sendMessage(Message.raw("Spawned NPC at " + spawnPos));
}
```

### Clone and Modify Prefab
```java
// Get base prefab
Prefab basePrefab = prefabStore.getPrefab("base_entity");
Holder<EntityStore> holder = basePrefab.getHolder().clone();

// Add custom component
holder.putComponent(MyMarkerComponent.getComponentType(), new MyMarkerComponent());

// Modify existing component if present
HealthComponent health = holder.getComponent(HealthComponent.getComponentType());
if (health != null) {
    holder.putComponent(HealthComponent.getComponentType(),
        new HealthComponent(health.getMaxHealth() * 2));
}

// Spawn modified entity
Ref<EntityStore> entityRef = store.addEntity(holder, AddReason.SPAWN);
```

### Random Mob Spawner
```java
// Create weighted selector
Map<String, Float> mobWeights = Map.of(
    "hytale:zombie", 0.5f,
    "hytale:skeleton", 0.3f,
    "hytale:spider", 0.2f
);
PrefabSelector selector = PrefabSelector.weighted(mobWeights);

// Spawn random mob
String selectedPrefabId = selector.select();
Prefab prefab = prefabStore.getPrefab(selectedPrefabId);
world.spawnEntity(prefab.getHolder(), spawnPosition, Vector3f.ZERO);
```

---

## Prefab Inheritance

Some prefab systems support inheritance:

```json
{
    "id": "myplugin:strong_zombie",
    "extends": "hytale:zombie",
    "components": {
        "health": {
            "maxHealth": 200
        }
    }
}
```

The child prefab inherits all components from the parent and overrides specified values.

---

## Working with Entity References

After spawning, you can interact with the entity via its reference:

```java
// Spawn and get reference
Ref<EntityStore> entityRef = store.addEntity(prefab.getHolder(), AddReason.SPAWN);

// Get components
Entity entity = store.getComponent(entityRef, Entity.getComponentType());
MyComponent myComp = store.getComponent(entityRef, MyComponent.getComponentType());

// Modify components
store.putComponent(entityRef, MyComponent.getComponentType(), new MyComponent(newValue));

// Remove entity later
store.removeEntity(entityRef, RemoveReason.DESPAWN);
```

---

## Notes
- Prefabs are immutable templates; clone before modifying
- Use `AddReason.SPAWN` for programmatic spawning
- Prefab IDs typically follow `namespace:name` format
- Access PrefabStore through server or world context
- Consider performance when spawning many entities; batch if possible

---

## Prefab Events

Events related to prefab pasting and entity placement from prefabs.

**Package:** `com.hypixel.hytale.server.core.prefab.event`

### Event Summary

| Class | Description | Cancellable |
|-------|-------------|-------------|
| `PrefabPasteEvent` | Prefab is being pasted into world (ECS) | Yes |
| `PrefabPlaceEntityEvent` | Entity placed from prefab (ECS) | No |

---

### PrefabPasteEvent

ECS event fired when a prefab is being pasted into the world. Extends `CancellableEcsEvent`.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getPrefabId()` | `int` | Internal ID of the prefab being pasted |
| `isPasteStart()` | `boolean` | True if this is the start of pasting, false if end |
| `isCancelled()` | `boolean` | Whether the paste is cancelled |
| `setCancelled(boolean)` | `void` | Cancel or uncancel the paste |

> **Note:** `getPrefabId()` in events returns an internal integer ID, not the string identifier (e.g., `"hytale:zombie"`) used with `PrefabStore.getPrefab()`.

---

### PrefabPlaceEntityEvent

ECS event fired when an entity is placed as part of a prefab. Extends `EcsEvent` (not cancellable).

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getPrefabId()` | `int` | ID of the prefab containing this entity |
| `getHolder()` | `Holder<EntityStore>` | Entity holder for the placed entity |

---

### Prefab Events Usage

Prefab events are ECS events, so handle them using an `EntityEventSystem`:

```java
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.prefab.event.PrefabPasteEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class PrefabPasteSystem extends EntityEventSystem<EntityStore, PrefabPasteEvent> {

    public PrefabPasteSystem() {
        super(PrefabPasteEvent.class);
    }

    @Override
    public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                       Store<EntityStore> store, CommandBuffer<EntityStore> buffer,
                       PrefabPasteEvent event) {
        if (event.isPasteStart()) {
            System.out.println("Starting to paste prefab: " + event.getPrefabId());
        } else {
            System.out.println("Finished pasting prefab: " + event.getPrefabId());
        }

        // Optionally cancel the paste
        // event.setCancelled(true);
    }

    @Override
    public Query<EntityStore> getQuery() {
        return null; // Or a specific component type
    }
}
```

```java
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.prefab.event.PrefabPlaceEntityEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class PrefabPlaceEntitySystem extends EntityEventSystem<EntityStore, PrefabPlaceEntityEvent> {

    public PrefabPlaceEntitySystem() {
        super(PrefabPlaceEntityEvent.class);
    }

    @Override
    public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                       Store<EntityStore> store, CommandBuffer<EntityStore> buffer,
                       PrefabPlaceEntityEvent event) {
        var holder = event.getHolder();
        System.out.println("Entity placed from prefab " + event.getPrefabId() + ": " + holder);
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
    getEntityStoreRegistry().registerSystem(new PrefabPasteSystem());
    getEntityStoreRegistry().registerSystem(new PrefabPlaceEntitySystem());
}
