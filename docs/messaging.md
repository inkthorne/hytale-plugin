# Messaging API

## Message
**Package:** `com.hypixel.hytale.server.core`

Create and format chat messages.

### Static Factory Methods
```java
Message.raw(String text)           // Plain text message
Message.translation(String key)    // Translated message (i18n key)
Message.parse(String text)         // Parse formatted text
Message.empty()                    // Empty message
Message.join(Message... messages)  // Concatenate messages
```

### Formatting (Fluent API)
All formatting methods return `Message` for chaining:
```java
Message bold(boolean bold)
Message italic(boolean italic)
Message monospace(boolean mono)
Message color(String hexColor)       // e.g., "#FF0000"
Message color(Color awtColor)
Message link(String url)
```

### Parameters (for translations)
```java
Message param(String key, String value)
Message param(String key, boolean value)
Message param(String key, int value)
Message param(String key, long value)
Message param(String key, float value)
Message param(String key, double value)
Message param(String key, Message value)
```

### Composition
```java
Message insert(Message child)
Message insert(String text)
Message insertAll(Message... children)
Message insertAll(List<Message> children)
```

### Getters
```java
String getRawText()
String getMessageId()
String getColor()
List<Message> getChildren()
String getAnsiMessage()
FormattedMessage getFormattedMessage()
```

## Usage Examples

### Simple Message
```java
playerRef.sendMessage(Message.raw("Hello, World!"));
```

### Formatted Message
```java
Message msg = Message.raw("Important: ")
    .bold(true)
    .color("#FF0000")
    .insert(Message.raw("You have mail!").italic(true));
playerRef.sendMessage(msg);
```

### Translation with Parameters
```java
Message msg = Message.translation("welcome.player")
    .param("name", playerRef.getUsername())
    .param("count", 5);
playerRef.sendMessage(msg);
```

### Joining Messages
```java
Message combined = Message.join(
    Message.raw("Score: ").bold(true),
    Message.raw("100").color("#00FF00"),
    Message.raw(" points")
);
```

### Broadcast to World
```java
world.sendMessage(Message.raw("Server announcement!"));
```
