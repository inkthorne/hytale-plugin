# Components (ECS) API

Hytale uses an Entity Component System (ECS) architecture. Entities are composed of components stored in typed stores.

## Core Types

### Store<ECS_TYPE>
**Package:** `com.hypixel.hytale.component`

Container for entities and their components. Implements `ComponentAccessor`.

```java
// Get component from entity
<T extends Component<ECS_TYPE>> T getComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)

// Add/ensure component exists
<T extends Component<ECS_TYPE>> T addComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)
<T extends Component<ECS_TYPE>> void addComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type, T component)
<T extends Component<ECS_TYPE>> T ensureAndGetComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)

// Replace/put component
<T extends Component<ECS_TYPE>> void putComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type, T component)
<T extends Component<ECS_TYPE>> void replaceComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type, T component)

// Remove component
<T extends Component<ECS_TYPE>> void removeComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)
<T extends Component<ECS_TYPE>> void tryRemoveComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)
<T extends Component<ECS_TYPE>> boolean removeComponentIfExists(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)

// Entity management
Ref<ECS_TYPE> addEntity(Holder<ECS_TYPE> holder, AddReason reason)
Holder<ECS_TYPE> removeEntity(Ref<ECS_TYPE> ref, RemoveReason reason)
Holder<ECS_TYPE> copyEntity(Ref<ECS_TYPE> ref)

// Query entities
int getEntityCount()
int getEntityCountFor(Query<ECS_TYPE> query)
Archetype<ECS_TYPE> getArchetype(Ref<ECS_TYPE> ref)

// Iterate entities
void forEachChunk(BiConsumer<ArchetypeChunk<ECS_TYPE>, CommandBuffer<ECS_TYPE>> consumer)
void forEachChunk(Query<ECS_TYPE> query, BiConsumer<ArchetypeChunk<ECS_TYPE>, CommandBuffer<ECS_TYPE>> consumer)
void forEachEntityParallel(IntBiObjectConsumer<ArchetypeChunk<ECS_TYPE>, CommandBuffer<ECS_TYPE>> consumer)

// Resources (world-level singletons)
<T extends Resource<ECS_TYPE>> T getResource(ResourceType<ECS_TYPE, T> type)
<T extends Resource<ECS_TYPE>> void replaceResource(ResourceType<ECS_TYPE, T> type, T resource)

// Events
<Event extends EcsEvent> void invoke(Ref<ECS_TYPE> ref, Event event)
<Event extends EcsEvent> void invoke(Event event)

// Utility
ECS_TYPE getExternalData()
ComponentRegistry<ECS_TYPE> getRegistry()
boolean isProcessing()
void assertThread()
```

### Ref<ECS_TYPE>
**Package:** `com.hypixel.hytale.component`

Reference to an entity in a store. Lightweight pointer.

```java
Ref(Store<ECS_TYPE> store)
Ref(Store<ECS_TYPE> store, int index)

Store<ECS_TYPE> getStore()
int getIndex()
boolean isValid()
void validate()
```

### Component<ECS_TYPE>
**Package:** `com.hypixel.hytale.component`

Interface for all components. Must be cloneable.

```java
Component<ECS_TYPE> clone()
default Component<ECS_TYPE> cloneSerializable()
```

### ComponentType<ECS_TYPE, T>
**Package:** `com.hypixel.hytale.component`

Type descriptor for a component. Used to get/set components.

```java
ComponentRegistry<ECS_TYPE> getRegistry()
Class<? super T> getTypeClass()
int getIndex()
boolean test(Archetype<ECS_TYPE> archetype)
```

### ComponentAccessor<ECS_TYPE>
**Package:** `com.hypixel.hytale.component`

Interface for accessing components. Store implements this.

```java
<T extends Component<ECS_TYPE>> T getComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)
<T extends Component<ECS_TYPE>> T ensureAndGetComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)
<T extends Component<ECS_TYPE>> void putComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type, T component)
<T extends Component<ECS_TYPE>> void addComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type, T component)
<T extends Component<ECS_TYPE>> T addComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)
<T extends Component<ECS_TYPE>> void removeComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)
Archetype<ECS_TYPE> getArchetype(Ref<ECS_TYPE> ref)
<T extends Resource<ECS_TYPE>> T getResource(ResourceType<ECS_TYPE, T> type)
ECS_TYPE getExternalData()
```

## Common Store Types

- `Store<EntityStore>` - Entity components (Player, PlayerRef, etc.)
- `Store<ChunkStore>` - Chunk components

## Usage in Commands
```java
@Override
protected void execute(CommandContext ctx, Store<EntityStore> store,
                      Ref<EntityStore> ref, PlayerRef playerRef, World world) {
    // Get Player component
    Player player = store.getComponent(ref, Player.getComponentType());

    // Get PlayerRef component (alternative)
    PlayerRef pref = store.getComponent(ref, PlayerRef.getComponentType());

    // Check if component exists
    TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
    if (transform != null) {
        // Use transform
    }
}
```
