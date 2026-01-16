# Hytale API Documentation Overview

Quick reference for finding the right documentation file.

## Quick Reference Table

| File | Description | Key Classes |
|------|-------------|-------------|
| plugin-lifecycle.md | Plugin setup and lifecycle | JavaPlugin, PluginBase |
| commands.md | Command creation and arguments | AbstractPlayerCommand, CommandContext |
| entities.md | Players, entities, stats, velocity | Player, PlayerRef, Velocity, EntityStatMap |
| world.md | World access and management | World |
| events.md | Event system and handlers | EventRegistry, DamageEventSystem |
| components.md | ECS architecture | Store, Ref, Component |
| messaging.md | Chat formatting | Message |
| permissions.md | Permission checks | PermissionHolder |
| inventory.md | Items and inventory | Inventory, ItemStack |
| tasks.md | Async task scheduling | TaskRegistry |
| ui.md | Player UI management | UICommandBuilder, WindowManager |
| blocks.md | Block states and events | BlockStateRegistry |
| assets.md | Asset registration | AssetRegistry |
| codecs.md | Serialization | Codec, RecordCodecBuilder |
| prefabs.md | Entity templates | Prefab, PrefabStore |
| math.md | Math library | Vector3d, Matrix4d, Quatf, Box |

## Class Lookup Index

```
PlayerRef, Player             → entities.md
Velocity, EntityStatMap       → entities.md
AbstractPlayerCommand         → commands.md
CommandContext                → commands.md
Store, Ref, Component         → components.md
EventRegistry                 → events.md
Message                       → messaging.md
Inventory, ItemStack          → inventory.md
TaskRegistry                  → tasks.md
UICommandBuilder              → ui.md
WindowManager                 → ui.md
BlockStateRegistry            → blocks.md
AssetRegistry                 → assets.md
Prefab, PrefabStore           → prefabs.md
Vector3d, Matrix4d, Quatf     → math.md
Box                           → math.md
Codec, RecordCodecBuilder     → codecs.md
```

## Topic Groups

**Core** - Essential plugin development
- plugin-lifecycle.md - Plugin entry point and setup
- commands.md - Slash commands
- entities.md - Players and entities
- world.md - World access
- events.md - Event handling
- components.md - ECS system

**Systems** - Game systems integration
- messaging.md - Chat messages
- permissions.md - Permission checks
- inventory.md - Items and inventory
- tasks.md - Async scheduling
- ui.md - Player UI
- blocks.md - Block manipulation
- assets.md - Asset registry

**Data** - Serialization
- codecs.md - Data encoding/decoding

**Utilities** - Helper systems
- prefabs.md - Entity templates
- math.md - Vectors, matrices, quaternions, shapes
