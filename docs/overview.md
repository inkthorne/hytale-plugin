# Hytale API Documentation Overview

Quick reference for finding the right documentation file.

## Quick Reference Table

| File | Description | Key Classes |
|------|-------------|-------------|
| plugin-lifecycle.md | Plugin setup, lifecycle, and server events | JavaPlugin, PluginBase, BootEvent, ShutdownEvent |
| commands.md | Command creation and arguments | AbstractPlayerCommand, CommandContext |
| entities.md | Players, entities, stats, velocity, entity events | Player, PlayerRef, Velocity, EntityStatMap |
| player.md | Player events and messaging | PlayerConnectEvent, PlayerInteractEvent, Message |
| world.md | World access, world events, chunk events | World, WorldEvent, ChunkEvent |
| events.md | Core event system and patterns | EventRegistry, EventPriority, EntityEventSystem |
| components.md | ECS architecture | Store, Ref, Component |
| permissions.md | Permission checks and events | PermissionHolder, PlayerGroupEvent |
| inventory.md | Items and inventory | Inventory, ItemStack |
| tasks.md | Async task scheduling | TaskRegistry |
| ui.md | Player UI management | UICommandBuilder, WindowManager |
| blocks.md | Block states and block events | BlockStateRegistry, PlaceBlockEvent, BreakBlockEvent |
| assets.md | Asset registration | AssetRegistry |
| codecs.md | Serialization | Codec, RecordCodecBuilder |
| prefabs.md | Entity templates and prefab events | Prefab, PrefabStore, PrefabPasteEvent |
| math.md | Math library | Vector3d, Matrix4d, Quatf, Box |
| projectiles.md | Projectile spawning and physics | ProjectileModule, ProjectileConfig |
| collision.md | Collision detection and queries | CollisionModule, CollisionResult |
| combat.md | Damage system and kill feed | Damage, DamageEventSystem, KillFeedEvent |
| npc.md | NPC loading and AI sensors | AllNPCsLoadedEvent, SensorEvent |
| adventure.md | Adventure features | DiscoverInstanceEvent, TreasureChestOpeningEvent |
| asset-editor.md | Asset editor events | EditorClientEvent, AssetEditorSelectAssetEvent |
| i18n.md | Localization events | GenerateDefaultLanguageEvent |
| singleplayer.md | Singleplayer events | SingleplayerRequestAccessEvent |

## Class Lookup Index

```
# Core Plugin
JavaPlugin, PluginBase           → plugin-lifecycle.md
BootEvent, ShutdownEvent         → plugin-lifecycle.md
PluginSetupEvent                 → plugin-lifecycle.md

# Commands
AbstractPlayerCommand            → commands.md
CommandContext                   → commands.md

# Entities
PlayerRef, Player                → entities.md
Velocity, EntityStatMap          → entities.md
EntityEvent, EntityRemoveEvent   → entities.md
DropItemEvent, SwitchActiveSlotEvent → entities.md

# Player Events & Messaging
PlayerConnectEvent               → player.md
PlayerDisconnectEvent            → player.md
PlayerInteractEvent              → player.md
InteractionType                  → player.md
Message                          → player.md

# World & Chunks
World                            → world.md
WorldEvent, AddWorldEvent        → world.md
ChunkEvent, ChunkSaveEvent       → world.md
MoonPhaseChangeEvent             → world.md

# Event System
EventRegistry                    → events.md
EventPriority                    → events.md
EntityEventSystem                → events.md
IBaseEvent, IEvent               → events.md
ICancellable                     → events.md
EcsEvent, CancellableEcsEvent    → events.md
ICancellableEcsEvent             → events.md

# ECS
Store, Ref, Component            → components.md

# Permissions
PermissionHolder                 → permissions.md
PlayerGroupEvent                 → permissions.md
PlayerPermissionChangeEvent      → permissions.md

# Inventory
Inventory, ItemStack             → inventory.md

# Tasks
TaskRegistry                     → tasks.md

# UI
UICommandBuilder                 → ui.md
WindowManager                    → ui.md

# Blocks
BlockStateRegistry               → blocks.md
PlaceBlockEvent, BreakBlockEvent → blocks.md
DamageBlockEvent, UseBlockEvent  → blocks.md

# Assets
AssetRegistry                    → assets.md

# Prefabs
Prefab, PrefabStore              → prefabs.md
PrefabPasteEvent                 → prefabs.md
PrefabPlaceEntityEvent           → prefabs.md

# Math
Vector3d, Matrix4d, Quatf        → math.md
Box                              → math.md

# Codecs
Codec, RecordCodecBuilder        → codecs.md

# Projectiles
ProjectileModule, ProjectileConfig → projectiles.md
PhysicsConfig, StandardPhysicsConfig → projectiles.md
ImpactConsumer, BounceConsumer   → projectiles.md

# Collision
CollisionModule, CollisionResult → collision.md
BlockCollisionData, CollisionConfig → collision.md
CollisionFilter, CollisionMaterial → collision.md

# Combat
Damage, DamageEventSystem        → combat.md
Damage.Source, Damage.EntitySource → combat.md
DamageDataComponent              → combat.md
KillFeedEvent                    → combat.md

# NPCs
AllNPCsLoadedEvent               → npc.md
LoadedNPCEvent                   → npc.md
SensorEvent, SensorEntityEvent   → npc.md
EventSearchType                  → npc.md

# Adventure
DiscoverInstanceEvent            → adventure.md
DiscoverZoneEvent                → adventure.md
TreasureChestOpeningEvent        → adventure.md
InstanceDiscoveryConfig          → adventure.md
WorldMapTracker                  → adventure.md
ZoneDiscoveryInfo                → adventure.md

# Asset Events
AssetPackRegisterEvent           → assets.md
AssetPackUnregisterEvent         → assets.md
LoadAssetEvent                   → assets.md
GenerateSchemaEvent              → assets.md
CommonAssetMonitorEvent          → assets.md
SendCommonAssetsEvent            → assets.md
PathEvent                        → assets.md

# Asset Editor
EditorClientEvent                → asset-editor.md
AssetEditorActivateButtonEvent   → asset-editor.md
AssetEditorAssetCreatedEvent     → asset-editor.md
AssetEditorClientDisconnectEvent → asset-editor.md
AssetEditorSelectAssetEvent      → asset-editor.md
AssetEditorFetchAutoCompleteDataEvent → asset-editor.md
AssetEditorRequestDataSetEvent   → asset-editor.md

# Localization
GenerateDefaultLanguageEvent     → i18n.md

# Singleplayer
SingleplayerRequestAccessEvent   → singleplayer.md

# Chunk Events
ChunkPreLoadProcessEvent         → world.md
```

## Topic Groups

**Core** - Essential plugin development
- plugin-lifecycle.md - Plugin entry point, setup, and server lifecycle events
- commands.md - Slash commands
- entities.md - Players, entities, stats, velocity, and entity events
- player.md - Player events and messaging
- world.md - World access, world events, and chunk events
- events.md - Core event system patterns
- components.md - ECS system

**Systems** - Game systems integration
- permissions.md - Permission checks and permission events
- inventory.md - Items and inventory
- tasks.md - Async scheduling
- ui.md - Player UI
- blocks.md - Block manipulation and block events
- assets.md - Asset registry and asset events
- projectiles.md - Projectile spawning and physics
- collision.md - Collision detection and queries
- i18n.md - Localization events
- singleplayer.md - Singleplayer events

**Editor** - Development tools
- asset-editor.md - Asset editor events

**Combat & NPCs** - Combat and AI systems
- combat.md - Damage system, damage events, kill feed
- npc.md - NPC loading, AI sensors

**Adventure** - Adventure gameplay features
- adventure.md - Instance discovery, treasure chests

**Data** - Serialization
- codecs.md - Data encoding/decoding

**Utilities** - Helper systems
- prefabs.md - Entity templates and prefab events
- math.md - Vectors, matrices, quaternions, shapes
