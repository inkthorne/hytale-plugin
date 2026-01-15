# Assets API

## AssetRegistry
**Package:** `com.hypixel.hytale.server.core.plugin.registry`

Register custom assets. Access via `getAssetRegistry()` in your plugin.

## Related Registries

From `PluginBase`, you also have access to:

```java
// Asset registry
AssetRegistry getAssetRegistry()

// Codec-based registries
<T, C extends Codec<? extends T>> CodecMapRegistry<T, C>
    getCodecRegistry(StringCodecMapCodec<T, C> codec)

<K, T extends JsonAsset<K>> CodecMapRegistry.Assets<T, ?>
    getCodecRegistry(AssetCodecMapCodec<K, T> codec)

<V> MapKeyMapRegistry<V> getCodecRegistry(MapKeyMapCodec<V> codec)
```

## Asset Store
**Package:** `com.hypixel.hytale.server.core.asset`

`HytaleAssetStore` - Central asset storage.

## Prefabs
**Package:** `com.hypixel.hytale.server.core.prefab`

`PrefabStore` - Store and manage prefabs.

## Asset Types
**Package:** `com.hypixel.hytale.server.core.asset.type`

Various asset type configurations:
- `item/` - Item definitions
- `blocktype/` - Block type configs
- `model/` - Model definitions
- `particle/` - Particle effects
- `gameplay/` - Gameplay configs

## Usage Notes
- Assets are typically JSON-based configurations
- Register custom assets during plugin `setup()`
- Asset loading happens through codec serialization
- Explore specific asset type packages for detailed APIs
