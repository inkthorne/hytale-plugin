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

## Related Packages

### Block Types
**Package:** `com.hypixel.hytale.server.core.blocktype`

Block type definitions and configurations.

### Block Materials
**Package:** `com.hypixel.hytale.protocol`

`BlockMaterial` - Material properties for blocks.

## World Block Access

Via `World` chunk accessors:
```java
WorldChunk chunk = world.getChunkIfLoaded(chunkKey);
// Access block data through chunk
```

## Usage Example

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

## Notes
- Block manipulation typically goes through chunk accessors
- Block states persist additional data per-block
- See chunk/world APIs for reading/writing blocks
