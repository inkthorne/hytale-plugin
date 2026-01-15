# Events API

## EventRegistry
**Package:** `com.hypixel.hytale.event`

Register event listeners. Access via `getEventRegistry()` in your plugin.

### Basic Registration
```java
// Simple event listener (no key)
<EventType extends IBaseEvent<Void>> EventRegistration<Void, EventType>
    register(Class<? super EventType> eventClass, Consumer<EventType> handler)

// With priority
<EventType extends IBaseEvent<Void>> EventRegistration<Void, EventType>
    register(EventPriority priority, Class<? super EventType> eventClass, Consumer<EventType> handler)

// With numeric priority (lower = earlier)
<EventType extends IBaseEvent<Void>> EventRegistration<Void, EventType>
    register(short priority, Class<? super EventType> eventClass, Consumer<EventType> handler)
```

### Keyed Registration
For events filtered by a key (e.g., specific block type):
```java
<KeyType, EventType extends IBaseEvent<KeyType>> EventRegistration<KeyType, EventType>
    register(Class<? super EventType> eventClass, KeyType key, Consumer<EventType> handler)

<KeyType, EventType extends IBaseEvent<KeyType>> EventRegistration<KeyType, EventType>
    register(EventPriority priority, Class<? super EventType> eventClass, KeyType key, Consumer<EventType> handler)
```

### Async Registration
For async event handlers (return CompletableFuture):
```java
<EventType extends IAsyncEvent<Void>> EventRegistration<Void, EventType>
    registerAsync(Class<? super EventType> eventClass,
                  Function<CompletableFuture<EventType>, CompletableFuture<EventType>> handler)

<EventType extends IAsyncEvent<Void>> EventRegistration<Void, EventType>
    registerAsync(EventPriority priority, Class<? super EventType> eventClass,
                  Function<CompletableFuture<EventType>, CompletableFuture<EventType>> handler)
```

### Global Registration
Listens to events regardless of key:
```java
<KeyType, EventType extends IBaseEvent<KeyType>> EventRegistration<KeyType, EventType>
    registerGlobal(Class<? super EventType> eventClass, Consumer<EventType> handler)

<KeyType, EventType extends IBaseEvent<KeyType>> EventRegistration<KeyType, EventType>
    registerGlobal(EventPriority priority, Class<? super EventType> eventClass, Consumer<EventType> handler)

// Async global
<KeyType, EventType extends IAsyncEvent<KeyType>> EventRegistration<KeyType, EventType>
    registerAsyncGlobal(Class<? super EventType> eventClass,
                        Function<CompletableFuture<EventType>, CompletableFuture<EventType>> handler)
```

### Unhandled Registration
Listens only if no other handler processed the event:
```java
<KeyType, EventType extends IBaseEvent<KeyType>> EventRegistration<KeyType, EventType>
    registerUnhandled(Class<? super EventType> eventClass, Consumer<EventType> handler)

<KeyType, EventType extends IAsyncEvent<KeyType>> EventRegistration<KeyType, EventType>
    registerAsyncUnhandled(Class<? super EventType> eventClass,
                           Function<CompletableFuture<EventType>, CompletableFuture<EventType>> handler)
```

## EventPriority
Use to control handler execution order:
- Lower priority number = executes first
- Use `EventPriority` enum or raw `short` value

## Usage Example
```java
@Override
protected void setup() {
    // Register for player join events
    getEventRegistry().register(PlayerJoinEvent.class, event -> {
        event.getPlayer().sendMessage(Message.raw("Welcome!"));
    });

    // High priority handler
    getEventRegistry().register(EventPriority.HIGH, PlayerChatEvent.class, event -> {
        // Process chat before other handlers
    });

    // Keyed event (specific item type)
    getEventRegistry().register(ItemUseEvent.class, myItemKey, event -> {
        // Only fires for this specific item
    });
}
```
