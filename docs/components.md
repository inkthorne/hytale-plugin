# Components (ECS) API

Hytale uses an Entity Component System (ECS) architecture. Entities are composed of components stored in typed stores.

## Core Types

### Store<ECS_TYPE>
**Package:** `com.hypixel.hytale.component`

Container for entities and their components. Implements `ComponentAccessor`.

#### Component Operations
```java
// Get component from entity
<T extends Component<ECS_TYPE>> T getComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)

// Add component (creates if not exists)
<T extends Component<ECS_TYPE>> T addComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)
<T extends Component<ECS_TYPE>> void addComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type, T component)

// Ensure component exists and get it
<T extends Component<ECS_TYPE>> T ensureAndGetComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)

// Replace/put component
<T extends Component<ECS_TYPE>> void putComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type, T component)
<T extends Component<ECS_TYPE>> void replaceComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type, T component)

// Remove component
<T extends Component<ECS_TYPE>> void removeComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)
<T extends Component<ECS_TYPE>> void tryRemoveComponent(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)
<T extends Component<ECS_TYPE>> boolean removeComponentIfExists(Ref<ECS_TYPE> ref, ComponentType<ECS_TYPE, T> type)
```

#### Entity Management
```java
// Add entity from holder (blueprint)
Ref<ECS_TYPE> addEntity(Holder<ECS_TYPE> holder, AddReason reason)

// Remove entity
Holder<ECS_TYPE> removeEntity(Ref<ECS_TYPE> ref, RemoveReason reason)

// Copy entity
Holder<ECS_TYPE> copyEntity(Ref<ECS_TYPE> ref)
```

#### Query Entities
```java
int getEntityCount()
int getEntityCountFor(Query<ECS_TYPE> query)
Archetype<ECS_TYPE> getArchetype(Ref<ECS_TYPE> ref)
```

#### Iterate Entities
```java
void forEachChunk(BiConsumer<ArchetypeChunk<ECS_TYPE>, CommandBuffer<ECS_TYPE>> consumer)
void forEachChunk(Query<ECS_TYPE> query, BiConsumer<ArchetypeChunk<ECS_TYPE>, CommandBuffer<ECS_TYPE>> consumer)
void forEachEntityParallel(IntBiObjectConsumer<ArchetypeChunk<ECS_TYPE>, CommandBuffer<ECS_TYPE>> consumer)
```

#### Resources (World-Level Singletons)
```java
<T extends Resource<ECS_TYPE>> T getResource(ResourceType<ECS_TYPE, T> type)
<T extends Resource<ECS_TYPE>> void replaceResource(ResourceType<ECS_TYPE, T> type, T resource)
```

#### Events
```java
<Event extends EcsEvent> void invoke(Ref<ECS_TYPE> ref, Event event)
<Event extends EcsEvent> void invoke(Event event)
```

#### Utility
```java
ECS_TYPE getExternalData()
ComponentRegistry<ECS_TYPE> getRegistry()
boolean isProcessing()
boolean isInThread()
void assertThread()
```

---

### Ref<ECS_TYPE>
**Package:** `com.hypixel.hytale.component`

Lightweight reference to an entity in a store. Used as a pointer to access entity data.

#### Constructors
```java
Ref(Store<ECS_TYPE> store)
Ref(Store<ECS_TYPE> store, int index)
```

#### Methods
```java
Store<ECS_TYPE> getStore()
int getIndex()
boolean isValid()
void validate()
void invalidate()
```

---

### Component<ECS_TYPE>
**Package:** `com.hypixel.hytale.component`

Interface for all components. Must be cloneable.

```java
Component<ECS_TYPE> clone()
default Component<ECS_TYPE> cloneSerializable()
```

---

### ComponentType<ECS_TYPE, T>
**Package:** `com.hypixel.hytale.component`

Type descriptor for a component. Used to get/set components.

**Note:** `ComponentType` implements `Query<ECS_TYPE>`, so it can be used directly where a Query is required.

```java
ComponentRegistry<ECS_TYPE> getRegistry()
Class<? super T> getTypeClass()
int getIndex()
boolean test(Archetype<ECS_TYPE> archetype)
boolean requiresComponentType()
```

---

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

---

### ArchetypeChunk<ECS_TYPE>
**Package:** `com.hypixel.hytale.component`

Used in `EntityEventSystem` handlers and iteration to access components by entity index.

```java
// Get component for entity at index
<T extends Component<ECS_TYPE>> T getComponent(int index, ComponentType<ECS_TYPE, T> type)
```

See [Events API - ECS Events](events.md#ecs-events-entityeventsystem) for usage example.

---

## Blueprint and Composition Types

### Holder<ECS_TYPE>
**Package:** `com.hypixel.hytale.component`

Blueprint/template for creating entities. Use to define entity composition before adding to store.

```java
// Get archetype (component composition)
Archetype<ECS_TYPE> getArchetype()

// Component management
<T extends Component<ECS_TYPE>> T ensureComponent(ComponentType<ECS_TYPE, T> type)
<T extends Component<ECS_TYPE>> void addComponent(ComponentType<ECS_TYPE, T> type, T component)
<T extends Component<ECS_TYPE>> void putComponent(ComponentType<ECS_TYPE, T> type, T component)
<T extends Component<ECS_TYPE>> T getComponent(ComponentType<ECS_TYPE, T> type)
<T extends Component<ECS_TYPE>> void removeComponent(ComponentType<ECS_TYPE, T> type)

// Cloning
Holder<ECS_TYPE> clone()
```

#### Usage
```java
// Create entity from holder
Holder<EntityStore> holder = new Holder<>();
holder.addComponent(MyComponent.getComponentType(), new MyComponent());
Ref<EntityStore> entityRef = store.addEntity(holder, AddReason.SPAWN);
```

---

### Archetype<ECS_TYPE>
**Package:** `com.hypixel.hytale.component`

Describes the composition of components for entities. Implements `Query<ECS_TYPE>`.

```java
// Check for component
boolean contains(ComponentType<ECS_TYPE, ?> type)

// Count components
int count()
int length()

// Query matching
boolean test(Archetype<ECS_TYPE> archetype)

// Factory methods
static <ECS_TYPE> Archetype<ECS_TYPE> of(ComponentType<ECS_TYPE, ?>... types)

// Modify archetype (returns new instance)
Archetype<ECS_TYPE> add(ComponentType<ECS_TYPE, ?> type)
Archetype<ECS_TYPE> remove(ComponentType<ECS_TYPE, ?> type)

// Serialization
Archetype<ECS_TYPE> getSerializableArchetype()
```

---

## Resource Types

### Resource<ECS_TYPE>
**Package:** `com.hypixel.hytale.component`

Interface for world-level singleton resources (not per-entity).

```java
// Marker interface - implement for your resource classes
```

### ResourceType<ECS_TYPE, T>
**Package:** `com.hypixel.hytale.component`

Type descriptor for resources.

```java
ComponentRegistry<ECS_TYPE> getRegistry()
Class<? super T> getTypeClass()
int getIndex()
```

---

## Enums

### AddReason
Reason for adding an entity to the store.

```java
public enum AddReason {
    SPAWN,
    LOAD,
    TRANSFER,
    // ... other values
}
```

### RemoveReason
Reason for removing an entity from the store.

```java
public enum RemoveReason {
    DESPAWN,
    UNLOAD,
    TRANSFER,
    DEATH,
    // ... other values
}
```

---

## Annotations

### @NonSerialized
Mark components or fields that should not be serialized.

### @NonTicking
Mark components that should not participate in ticking systems.

---

## Common Store Types

- `Store<EntityStore>` - Entity components (Player, PlayerRef, etc.)
- `Store<ChunkStore>` - Chunk components

---

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

    // Get entity archetype
    Archetype<EntityStore> archetype = store.getArchetype(ref);
    if (archetype.contains(Player.getComponentType())) {
        // Entity is a player
    }
}
```

---

## Creating Custom Components

```java
public class MyCustomComponent implements Component<EntityStore> {
    private int value;

    public MyCustomComponent() {
        this.value = 0;
    }

    public MyCustomComponent(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public Component<EntityStore> clone() {
        return new MyCustomComponent(this.value);
    }

    // Register with ComponentRegistryProxy
    private static ComponentType<EntityStore, MyCustomComponent> TYPE;

    public static ComponentType<EntityStore, MyCustomComponent> getComponentType() {
        return TYPE;
    }
}
```

---

## Entity Iteration Example

```java
// Iterate all entities with Player component
store.forEachChunk(Player.getComponentType(), (chunk, buffer) -> {
    for (int i = 0; i < chunk.getCount(); i++) {
        Player player = chunk.getComponent(i, Player.getComponentType());
        // Process player
    }
});

// Count entities matching query
int playerCount = store.getEntityCountFor(Player.getComponentType());
```

---

## Working with Holders

```java
// Get holder from entity (for copying or inspection)
Holder<EntityStore> holder = store.copyEntity(ref);

// Modify holder and create new entity
holder.putComponent(SomeComponent.getComponentType(), new SomeComponent());
Ref<EntityStore> newEntity = store.addEntity(holder, AddReason.SPAWN);
```

---

## Resource Example

```java
// Get world-level resource
MyWorldResource resource = store.getResource(MyWorldResource.getResourceType());

// Replace resource
store.replaceResource(MyWorldResource.getResourceType(), newResource);
```
