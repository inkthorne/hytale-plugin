# Projectiles API

## Class Hierarchy
```
ProjectileConfig (asset-based configuration)
  implements JsonAssetWithMap, NetworkSerializable, BallisticData

PhysicsConfig (interface)
  └── StandardPhysicsConfig (default implementation)

Projectile (ECS component)
  implements Component<EntityStore>

ProjectileInteraction
  extends SimpleInstantInteraction
  implements BallisticDataProvider
```

## ProjectileModule
**Package:** `com.hypixel.hytale.server.core.modules.projectile`

Main module for spawning and managing projectiles.

### Getting the Module
```java
ProjectileModule module = ProjectileModule.get();
```

### Key Methods
```java
// Spawn a projectile
Ref<EntityStore> spawnProjectile(
    Ref<EntityStore> shooter,
    CommandBuffer<EntityStore> commandBuffer,
    ProjectileConfig config,
    Vector3d position,
    Vector3d velocity
)

// Spawn with custom UUID
Ref<EntityStore> spawnProjectile(
    UUID uuid,
    Ref<EntityStore> shooter,
    CommandBuffer<EntityStore> commandBuffer,
    ProjectileConfig config,
    Vector3d position,
    Vector3d velocity
)

// Get component types
ComponentType<EntityStore, Projectile> getProjectileComponentType()
ComponentType<EntityStore, StandardPhysicsProvider> getStandardPhysicsProviderComponentType()
ComponentType<EntityStore, PredictedProjectile> getPredictedProjectileComponentType()
```

---

## ProjectileConfig
**Package:** `com.hypixel.hytale.server.core.modules.projectile.config`

Asset-based configuration for projectile behavior.

**Implements:** `JsonAssetWithMap`, `NetworkSerializable<ProjectileConfig>`, `BallisticData`

### Getting Configs from Assets
```java
// Get the asset store
AssetStore<String, ProjectileConfig, DefaultAssetMap<String, ProjectileConfig>> store =
    ProjectileConfig.getAssetStore();

// Get the asset map
DefaultAssetMap<String, ProjectileConfig> map = ProjectileConfig.getAssetMap();
```

### Key Methods
```java
// Identity
String getId()

// Ballistic properties
double getLaunchForce()
double getMuzzleVelocity()
double getGravity()
double getVerticalCenterShot()
double getDepthShot()
boolean isPitchAdjustShot()

// Spawn positioning
Vector3f getSpawnOffset()
Direction getSpawnRotationOffset()
Vector3d getCalculatedOffset(float yaw, float pitch)

// Physics behavior
PhysicsConfig getPhysicsConfig()

// Visuals
Model getModel()

// Sound events
int getLaunchWorldSoundEventIndex()
int getProjectileSoundEventIndex()

// Interactions
Map<InteractionType, String> getInteractions()
```

---

## BallisticData
**Package:** `com.hypixel.hytale.server.core.modules.projectile.config`

Interface for ballistic properties.

### Methods
```java
double getMuzzleVelocity()    // Initial projectile speed
double getGravity()           // Gravity multiplier
double getVerticalCenterShot() // Vertical offset for aiming
double getDepthShot()         // Forward offset for spawn
boolean isPitchAdjustShot()   // Whether to adjust pitch for trajectory
```

---

## PhysicsConfig
**Package:** `com.hypixel.hytale.server.core.modules.projectile.config`

Interface for projectile physics behavior.

### Methods
```java
// Apply physics to a projectile entity
void apply(
    Holder<EntityStore> holder,
    Ref<EntityStore> ref,
    Vector3d position,
    ComponentAccessor<EntityStore> accessor,
    boolean flag
)

// Get gravity (default implementation available)
double getGravity()
```

---

## StandardPhysicsConfig
**Package:** `com.hypixel.hytale.server.core.modules.projectile.config`

Default physics implementation for projectiles.

**Implements:** `PhysicsConfig`

### Constants
```java
static final StandardPhysicsConfig DEFAULT  // Default physics config
```

### Key Methods
```java
// Gravity
double getGravity()

// Bouncing
double getBounciness()        // How much velocity is retained on bounce (0-1)
int getBounceCount()          // Maximum number of bounces
double getBounceLimit()       // Minimum velocity to continue bouncing

// Surface behavior
boolean isSticksVertically()  // Whether projectile sticks to vertical surfaces
boolean isAllowRolling()      // Whether projectile can roll on surfaces
double getRollingFrictionFactor()  // Friction when rolling

// Water interaction
double getSwimmingDampingFactor()  // Velocity damping in water
double getHitWaterImpulseLoss()    // Velocity loss when entering water

// Physics application
void apply(
    Holder<EntityStore> holder,
    Ref<EntityStore> ref,
    Vector3d position,
    ComponentAccessor<EntityStore> accessor,
    boolean flag
)
```

---

## ImpactConsumer
**Package:** `com.hypixel.hytale.server.core.modules.projectile.config`

Callback interface for handling projectile impacts.

### Method
```java
void onImpact(
    Ref<EntityStore> projectileRef,    // The projectile entity
    Vector3d impactPosition,           // Where it hit
    Ref<EntityStore> targetRef,        // Entity that was hit (if any)
    String interactionId,              // Interaction identifier
    CommandBuffer<EntityStore> buffer  // Command buffer for responses
)
```

---

## BounceConsumer
**Package:** `com.hypixel.hytale.server.core.modules.projectile.config`

Callback interface for handling projectile bounces.

### Method
```java
void onBounce(
    Ref<EntityStore> projectileRef,    // The projectile entity
    Vector3d bouncePosition,           // Where it bounced
    CommandBuffer<EntityStore> buffer  // Command buffer for responses
)
```

---

## Projectile Component
**Package:** `com.hypixel.hytale.server.core.modules.projectile.component`

ECS component marking an entity as a projectile.

**Implements:** `Component<EntityStore>`

### Getting the Component
```java
ComponentType<EntityStore, Projectile> type = Projectile.getComponentType();
Projectile projectile = store.getComponent(ref, type);
```

---

## ProjectileInteraction
**Package:** `com.hypixel.hytale.server.core.modules.projectile.interaction`

Interaction that launches projectiles when triggered.

**Extends:** `SimpleInstantInteraction`
**Implements:** `BallisticDataProvider`

### Key Methods
```java
ProjectileConfig getConfig()           // Get the projectile config
BallisticData getBallisticData()       // Get ballistic properties
WaitForDataFrom getWaitForDataFrom()   // Client/server data sync mode
boolean needsRemoteSync()              // Whether to sync across network
```

---

## Usage Examples

### Spawning a Projectile
```java
ProjectileModule module = ProjectileModule.get();

// Get projectile config from assets
ProjectileConfig config = ProjectileConfig.getAssetMap().get("arrow");

// Calculate spawn position and velocity
Vector3d spawnPos = new Vector3d(x, y, z);
Vector3d velocity = new Vector3d(dirX * config.getMuzzleVelocity(),
                                  dirY * config.getMuzzleVelocity(),
                                  dirZ * config.getMuzzleVelocity());

// Spawn the projectile
Ref<EntityStore> projectileRef = module.spawnProjectile(
    shooterRef,
    commandBuffer,
    config,
    spawnPos,
    velocity
);
```

### Checking if Entity is a Projectile
```java
Projectile projectileComp = store.getComponent(ref, Projectile.getComponentType());
if (projectileComp != null) {
    // Entity is a projectile
}
```
