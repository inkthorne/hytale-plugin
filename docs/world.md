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
