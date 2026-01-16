# Blocks API

## BlockStateRegistry
**Package:** `com.hypixel.hytale.server.core.universe.world.meta`

Register custom block states. Access via `getBlockStateRegistry()` in your plugin.

### Methods
```java
// Register a simple block state
<T extends BlockState> BlockStateRegistration registerBlockState(
    Class<T> stateClass,
    String name,
    Codec<T> codec
)

// Register block state with associated data
<T extends BlockState, D extends StateData> BlockStateRegistration registerBlockState(
    Class<T> stateClass,
    String name,
    Codec<T> stateCodec,
    Class<D> dataClass,
    Codec<D> dataCodec
)
```

---

## BlockState Interface

Custom block states must implement the `BlockState` interface.

```java
public interface BlockState {
    // Implement your state logic
}
```

---

## StateData Interface

For blocks that need additional persistent data beyond basic state.

```java
public interface StateData {
    // Implement your data storage
}
```

---

## Related Packages

### Block Types
**Package:** `com.hypixel.hytale.server.core.blocktype`

Block type definitions and configurations.

### Block Materials
**Package:** `com.hypixel.hytale.protocol`

`BlockMaterial` - Material properties for blocks (hardness, sounds, etc.).

---

## World Block Access

### Via World and Chunks
```java
// Get chunk key from block coordinates
long chunkKey = ...; // Calculate from world position

// Get chunk if loaded (returns null if not loaded)
WorldChunk chunk = world.getChunkIfLoaded(chunkKey);

// Get chunk if in memory (non-ticking)
WorldChunk chunk = world.getChunkIfInMemory(chunkKey);

// Get chunk asynchronously
CompletableFuture<WorldChunk> futureChunk = world.getChunkAsync(chunkKey);
futureChunk.thenAccept(chunk -> {
    // Work with chunk
});
```

### Chunk Access Methods
```java
// Synchronous access
WorldChunk loadChunkIfInMemory(long chunkKey)
WorldChunk getChunkIfInMemory(long chunkKey)
WorldChunk getChunkIfLoaded(long chunkKey)
WorldChunk getChunkIfNonTicking(long chunkKey)

// Asynchronous access
CompletableFuture<WorldChunk> getChunkAsync(long chunkKey)
CompletableFuture<WorldChunk> getNonTickingChunkAsync(long chunkKey)
```

---

## Block Events

Handle block interactions through the event system. All block events are ECS events and should be handled using `EntityEventSystem`.

**Package:** `com.hypixel.hytale.server.core.event.events.ecs`

### Event Summary

| Class | Description | Cancellable |
|-------|-------------|-------------|
| `PlaceBlockEvent` | Block is placed | Yes |
| `BreakBlockEvent` | Block is broken | Yes |
| `DamageBlockEvent` | Block takes damage (mining progress) | Yes |
| `UseBlockEvent.Pre` | Before block is used/interacted with | Yes |
| `UseBlockEvent.Post` | After block is used/interacted with | No |

---

### PlaceBlockEvent

Fired when a block is placed.

```java
public class PlaceBlockEvent extends CancellableEcsEvent {
    ItemStack getItemInHand()
    Vector3i getTargetBlock()
    void setTargetBlock(Vector3i position)
    RotationTuple getRotation()
    void setRotation(RotationTuple rotation)
    boolean isCancelled()
    void setCancelled(boolean)
}
```

---

### BreakBlockEvent

Fired when a block is broken.

```java
public class BreakBlockEvent extends CancellableEcsEvent {
    ItemStack getItemInHand()
    Vector3i getTargetBlock()
    BlockType getBlockType()
    void setTargetBlock(Vector3i position)
    boolean isCancelled()
    void setCancelled(boolean)
}
```

---

### DamageBlockEvent

Fired when a block takes damage (mining progress). This fires during the mining process before the block is actually broken.

```java
public class DamageBlockEvent extends CancellableEcsEvent {
    ItemStack getItemInHand()
    Vector3i getTargetBlock()
    BlockType getBlockType()
    boolean isCancelled()
    void setCancelled(boolean)
}
```

#### DamageBlockEvent Usage

```java
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.DamageBlockEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class DamageBlockEventSystem extends EntityEventSystem<EntityStore, DamageBlockEvent> {

    public DamageBlockEventSystem() {
        super(DamageBlockEvent.class);
    }

    @Override
    public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                       Store<EntityStore> store, CommandBuffer<EntityStore> buffer,
                       DamageBlockEvent event) {
        Player player = chunk.getComponent(index, Player.getComponentType());
        if (player != null) {
            // Could log mining progress or modify damage
            var blockType = event.getBlockType();
            var pos = event.getTargetBlock();
            System.out.println("Mining " + blockType + " at " + pos);
        }
    }

    @Override
    public Query<EntityStore> getQuery() {
        return Player.getComponentType();
    }
}
```

---

### UseBlockEvent

Fired when a block is used/interacted with. Has `Pre` and `Post` variants.

#### UseBlockEvent.Pre

Fired before the block interaction is processed. Can be cancelled.

```java
public class UseBlockEvent.Pre extends CancellableEcsEvent {
    Vector3i getTargetBlock()
    BlockType getBlockType()
    ItemStack getItemInHand()
    boolean isCancelled()
    void setCancelled(boolean)
}
```

#### UseBlockEvent.Post

Fired after the block interaction is processed. Cannot be cancelled.

```java
public class UseBlockEvent.Post extends EcsEvent {
    Vector3i getTargetBlock()
    BlockType getBlockType()
    ItemStack getItemInHand()
}
```

#### UseBlockEvent Usage

```java
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.UseBlockEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class UseBlockPreSystem extends EntityEventSystem<EntityStore, UseBlockEvent.Pre> {

    public UseBlockPreSystem() {
        super(UseBlockEvent.Pre.class);
    }

    @Override
    public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                       Store<EntityStore> store, CommandBuffer<EntityStore> buffer,
                       UseBlockEvent.Pre event) {
        Player player = chunk.getComponent(index, Player.getComponentType());
        if (player != null) {
            // Prevent using certain block types
            // event.setCancelled(true);
            player.sendMessage(Message.raw("You used a block!"));
        }
    }

    @Override
    public Query<EntityStore> getQuery() {
        return Player.getComponentType();
    }
}

---

## Usage Examples

### Register Block State
```java
@Override
protected void setup() {
    getBlockStateRegistry().registerBlockState(
        MyBlockState.class,
        "my_block_state",
        MyBlockState.CODEC
    );
}
```

### Register Block State with Data
```java
@Override
protected void setup() {
    getBlockStateRegistry().registerBlockState(
        MyBlockState.class,
        "my_block_state",
        MyBlockState.CODEC,
        MyBlockStateData.class,
        MyBlockStateData.CODEC
    );
}
```

### Handle Block Break Event
```java
// Using EntityEventSystem for ECS events
public class BlockBreakSystem extends EntityEventSystem<EntityStore, BreakBlockEvent> {
    public BlockBreakSystem() {
        super(BreakBlockEvent.class);
    }

    @Override
    public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                       Store<EntityStore> store, CommandBuffer<EntityStore> buffer,
                       BreakBlockEvent event) {
        Player player = chunk.getComponent(index, Player.getComponentType());
        if (player != null) {
            Vector3i pos = event.getTargetBlock();
            player.sendMessage(Message.raw("You broke a block at " + pos.x + ", " + pos.y + ", " + pos.z));
        }
    }

    @Override
    public Query<EntityStore> getQuery() {
        return Player.getComponentType();
    }
}

// Register in setup()
@Override
protected void setup() {
    getEntityStoreRegistry().registerSystem(new BlockBreakSystem());
}
```

### Cancel Block Placement
```java
public class BlockPlaceSystem extends EntityEventSystem<EntityStore, PlaceBlockEvent> {
    public BlockPlaceSystem() {
        super(PlaceBlockEvent.class);
    }

    @Override
    public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                       Store<EntityStore> store, CommandBuffer<EntityStore> buffer,
                       PlaceBlockEvent event) {
        // Cancel placement in certain conditions
        Vector3i target = event.getTargetBlock();
        if (target.y > 100) {
            event.setCancelled(true);
            Player player = chunk.getComponent(index, Player.getComponentType());
            if (player != null) {
                player.sendMessage(Message.raw("Cannot place blocks above y=100"));
            }
        }
    }

    @Override
    public Query<EntityStore> getQuery() {
        return Player.getComponentType();
    }
}
```

---

## Notes
- Block manipulation typically goes through chunk accessors
- Block states persist additional data per-block instance
- Always check if chunk is loaded before accessing blocks
- Use async chunk loading for non-critical operations to avoid blocking
- Block events are ECS events; use `EntityEventSystem` to handle them
