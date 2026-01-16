# Assets API

## AssetRegistry
**Package:** `com.hypixel.hytale.server.core.plugin.registry`

Register custom assets. Access via `getAssetRegistry()` in your plugin.

### Methods
```java
// Register an asset store
<K, T extends JsonAssetWithMap<K, M>, M extends AssetMap<K, T>, S extends AssetStore<K, T, M>>
AssetRegistry register(S store)

// Shutdown (called automatically)
void shutdown()
```

---

## Related Registries

From `PluginBase`, you also have access to codec registries:

```java
// Asset registry
AssetRegistry getAssetRegistry()

// String-keyed codec registry
<T, C extends Codec<? extends T>> CodecMapRegistry<T, C>
    getCodecRegistry(StringCodecMapCodec<T, C> codec)

// Asset-keyed codec registry
<K, T extends JsonAsset<K>> CodecMapRegistry.Assets<T, ?>
    getCodecRegistry(AssetCodecMapCodec<K, T> codec)

// Map-keyed codec registry
<V> MapKeyMapRegistry<V> getCodecRegistry(MapKeyMapCodec<V> codec)
```

---

## Asset Store
**Package:** `com.hypixel.hytale.server.core.asset`

`HytaleAssetStore` - Central asset storage for the server.

```java
// Access registered assets
<K, T extends JsonAssetWithMap<K, M>, M extends AssetMap<K, T>>
T getAsset(Class<T> assetClass, K key)
```

---

## Prefab Store
**Package:** `com.hypixel.hytale.server.core.prefab`

`PrefabStore` - Store and manage entity prefabs.

See [Prefabs Documentation](prefabs.md) for detailed usage.

---

## Asset Types
**Package:** `com.hypixel.hytale.server.core.asset.type`

Common asset type configurations:

| Subpackage | Description |
|------------|-------------|
| `item/` | Item definitions and properties |
| `blocktype/` | Block type configurations |
| `model/` | 3D model definitions |
| `particle/` | Particle effect configurations |
| `gameplay/` | Gameplay configuration assets |

---

## JSON Asset Pattern

Assets in Hytale typically follow a JSON-based pattern with codec serialization:

```java
public class MyAsset implements JsonAsset<String> {
    private String id;
    private String name;
    private int value;

    // Codec for serialization
    public static final Codec<MyAsset> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.STRING.fieldOf("id").forGetter(MyAsset::getId),
            Codec.STRING.fieldOf("name").forGetter(MyAsset::getName),
            Codec.INT.fieldOf("value").forGetter(MyAsset::getValue)
        ).apply(instance, MyAsset::new)
    );

    public MyAsset(String id, String name, int value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    @Override
    public String getKey() {
        return id;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getValue() { return value; }
}
```

---

## Asset Store Pattern

Create a custom asset store:

```java
public class MyAssetStore extends AssetStore<String, MyAsset, MyAssetMap> {
    public MyAssetStore() {
        super(MyAsset.class, MyAssetMap.class, MyAsset.CODEC);
    }
}

public class MyAssetMap extends AssetMap<String, MyAsset> {
    // Map implementation
}
```

---

## Usage Examples

### Register Custom Assets
```java
@Override
protected void setup() {
    // Register your asset store
    MyAssetStore assetStore = new MyAssetStore();
    getAssetRegistry().register(assetStore);
}
```

### Access Registered Assets
```java
// Get asset by key
MyAsset asset = assetStore.get("my_asset_id");

// Check if asset exists
if (assetStore.contains("my_asset_id")) {
    // Use asset
}
```

### Using Codec Registries
```java
@Override
protected void setup() {
    // Register a string-keyed codec
    CodecMapRegistry<MyConfig, Codec<MyConfig>> registry =
        getCodecRegistry(MyConfig.STRING_CODEC_MAP);

    // Register configurations
    registry.register("my_config", myConfigInstance);
}
```

---

## Built-in Asset Access

Access built-in Hytale assets through the server's asset store:

```java
// Get item definition
ItemDefinition itemDef = HytaleAssetStore.getItemDefinition("hytale:wooden_sword");

// Get block type
BlockType blockType = HytaleAssetStore.getBlockType("hytale:stone");
```

---

## Asset Loading

Assets are loaded during server startup:
1. Built-in assets are loaded first
2. Plugin assets are loaded during plugin `setup()` phase
3. Assets can be accessed after all plugins are set up

---

## Notes
- Assets are typically JSON-based configurations
- Register custom assets during plugin `setup()`
- Asset loading happens through codec serialization
- Use the appropriate codec type for your asset structure
- Assets persist across server restarts (stored in data files)
- Explore specific asset type packages for detailed APIs
