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

Handle block interactions through the event system.

### PlaceBlockEvent
**Package:** `com.hypixel.hytale.server.core.event.events.ecs`

Fired when a block is placed.

```java
public class PlaceBlockEvent extends CancellableEcsEvent {
    ItemStack getItemInHand()
    Vector3i getTargetBlock()
    void setTargetBlock(Vector3i position)
    RotationTuple getRotation()
    void setRotation(RotationTuple rotation)
}
```

### BreakBlockEvent
**Package:** `com.hypixel.hytale.server.core.event.events.ecs`

Fired when a block is broken.

```java
public class BreakBlockEvent extends CancellableEcsEvent {
    ItemStack getItemInHand()
    Vector3i getTargetBlock()
    BlockType getBlockType()
    void setTargetBlock(Vector3i position)
}
```

### DamageBlockEvent
**Package:** `com.hypixel.hytale.server.core.event.events.ecs`

Fired when a block takes damage (mining progress).

### UseBlockEvent
**Package:** `com.hypixel.hytale.server.core.event.events.ecs`

Fired when a block is used/interacted with. Has `Pre` and `Post` variants.

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
