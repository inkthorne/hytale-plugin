# Entities API

## Class Hierarchy
```
Entity (abstract, implements Component<EntityStore>)
  └── LivingEntity (abstract)
        └── Player (implements CommandSender, PermissionHolder)
```

## PlayerRef
**Package:** `com.hypixel.hytale.server.core.universe`

Lightweight reference to a player, passed to commands. Use this for sending messages.

**Implements:** `Component<EntityStore>`, `MetricProvider`, `IMessageReceiver`

### Key Methods
```java
// Constructor
PlayerRef(Holder<EntityStore> holder, UUID uuid, String username, String language,
          PacketHandler handler, ChunkTracker tracker)

// Messaging
void sendMessage(Message msg)

// Identity
UUID getUuid()
String getUsername()
String getLanguage()
void setLanguage(String lang)

// Position
Transform getTransform()
UUID getWorldUuid()
Vector3f getHeadRotation()
void updatePosition(World world, Transform transform, Vector3f headRotation)

// References
boolean isValid()
Ref<EntityStore> getReference()
Holder<EntityStore> getHolder()
<T extends Component<EntityStore>> T getComponent(ComponentType<EntityStore, T> type)

// Network
PacketHandler getPacketHandler()
ChunkTracker getChunkTracker()
void referToServer(String host, int port)
void referToServer(String host, int port, byte[] data)

// Player Management
HiddenPlayersManager getHiddenPlayersManager()

// Lifecycle
void addToStore()
void removeFromStore()

// Component type for ECS access
static ComponentType<EntityStore, PlayerRef> getComponentType()
```

## Player
**Package:** `com.hypixel.hytale.server.core.entity.entities.player`

Full player entity with all game state.

**Extends:** `LivingEntity`
**Implements:** `CommandSender`, `PermissionHolder`, `MetricProvider`

### Key Methods
```java
// Messaging & Permissions
void sendMessage(Message msg)
boolean hasPermission(String permission)
boolean hasPermission(String permission, boolean defaultValue)

// Identity
String getDisplayName()
PlayerRef getPlayerRef()
PacketHandler getPlayerConnection()

// Game State
GameMode getGameMode()
static void setGameMode(Ref<EntityStore> ref, GameMode mode, ComponentAccessor<EntityStore> accessor)
static void initGameMode(Ref<EntityStore> ref, ComponentAccessor<EntityStore> accessor)
boolean isFirstSpawn()
void setFirstSpawn(boolean firstSpawn)

// Inventory
Inventory getInventory()
Inventory setInventory(Inventory inventory)
void sendInventory()

// Position & Movement
void moveTo(Ref<EntityStore> ref, double x, double y, double z, ComponentAccessor<EntityStore> accessor)
void addLocationChange(Ref<EntityStore> ref, double x, double y, double z, ComponentAccessor<EntityStore> accessor)
static Transform getRespawnPosition(Ref<EntityStore> ref, String spawnPoint, ComponentAccessor<EntityStore> accessor)
void applyMovementStates(Ref<EntityStore> ref, SavedMovementStates saved, MovementStates current, ComponentAccessor<EntityStore> accessor)
void resetVelocity(Velocity velocity)
void processVelocitySample()

// Managers
WindowManager getWindowManager()
PageManager getPageManager()
HudManager getHudManager()
HotbarManager getHotbarManager()
WorldMapTracker getWorldMapTracker()

// View Distance
int getViewRadius()
int getClientViewRadius()
void setClientViewRadius(int radius)

// Spawn Protection
boolean hasSpawnProtection()
void setLastSpawnTimeNanos(long nanos)
long getSinceLastSpawnNanos()

// Mounting
int getMountEntityId()
void setMountEntityId(int id)

// Item Durability
boolean canDecreaseItemStackDurability()
boolean canApplyItemStackPenalties()
void updateItemStackDurability()

// Block Processing
void configTriggerBlockProcessing(boolean trigger, boolean process, CollisionResultComponent result)

// Persistence
void saveConfig(World world, Holder<EntityStore> holder)

// Connection State
boolean isWaitingForClientReady()

// Component type for ECS access
static ComponentType<EntityStore, Player> getComponentType()
```

## LivingEntity
**Package:** `com.hypixel.hytale.server.core.entity`

Base class for entities with health, inventory, etc.

### Key Methods
```java
// Inventory
Inventory getInventory()
Inventory setInventory(Inventory inventory)
Inventory setInventory(Inventory inventory, boolean notify)

// Movement
void moveTo(Ref<EntityStore> ref, double x, double y, double z, ComponentAccessor<EntityStore> accessor)
double getCurrentFallDistance()
void setCurrentFallDistance(double distance)

// Stats
StatModifiersManager getStatModifiersManager()

// Environment
boolean canBreathe(Ref<EntityStore> ref, BlockMaterial material, int fluidLevel, ComponentAccessor<EntityStore> accessor)
```

## Entity
**Package:** `com.hypixel.hytale.server.core.entity`

Base class for all entities.

### Key Methods
```java
// Lifecycle
boolean remove()
boolean wasRemoved()
void loadIntoWorld(World world)
void unloadFromWorld()
void markNeedsSave()

// Identity
int getNetworkId()
UUID getUuid()
void setLegacyUUID(UUID uuid)
String getLegacyDisplayName()

// Position
TransformComponent getTransformComponent()
void setTransformComponent(TransformComponent transform)
void moveTo(Ref<EntityStore> ref, double x, double y, double z, ComponentAccessor<EntityStore> accessor)
World getWorld()

// Collision
boolean isCollidable()

// ECS
void setReference(Ref<EntityStore> ref)
Ref<EntityStore> getReference()
void clearReference()
Holder<EntityStore> toHolder()
```

## Usage in Commands
```java
@Override
protected void execute(CommandContext ctx, Store<EntityStore> store,
                      Ref<EntityStore> ref, PlayerRef playerRef, World world) {
    // Use playerRef for simple messaging
    playerRef.sendMessage(Message.raw("Hello!"));

    // Get full Player for more operations
    Player player = store.getComponent(ref, Player.getComponentType());
    player.hasPermission("myplugin.admin");

    // Get player position
    Transform transform = playerRef.getTransform();
}
```
