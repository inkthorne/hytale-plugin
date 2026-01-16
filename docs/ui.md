# UI API

## Overview
**Package:** `com.hypixel.hytale.server.core.ui`

Hytale provides UI building APIs for creating custom interfaces. UI is managed through XML-based definitions sent to clients via command builders.

---

## Positioning Classes

### Anchor
**Package:** `com.hypixel.hytale.server.core.ui`

UI element anchoring for positioning.

```java
// Setters (fluent API, return Anchor)
Anchor left(Value<Integer> value)
Anchor right(Value<Integer> value)
Anchor top(Value<Integer> value)
Anchor bottom(Value<Integer> value)
Anchor height(Value<Integer> value)
Anchor width(Value<Integer> value)
Anchor minWidth(Value<Integer> value)
Anchor maxWidth(Value<Integer> value)

// Presets
Anchor full()        // Fill parent completely
Anchor horizontal()  // Fill horizontally
Anchor vertical()    // Fill vertically

// Codec for serialization
static final Codec<Anchor> CODEC
```

### Area
**Package:** `com.hypixel.hytale.server.core.ui`

UI area/bounds definition.

```java
Area setX(int x)
Area setY(int y)
Area setWidth(int width)
Area setHeight(int height)

int getX()
int getY()
int getWidth()
int getHeight()
```

### Value<T>
**Package:** `com.hypixel.hytale.server.core.ui`

Generic value container supporting literal values or document references for dynamic binding.

```java
// Get value
T getValue()
String getDocumentPath()
String getValueName()

// Static factories
static <T> Value<T> ref(String documentPath, String valueName)  // Reference to UI document value
static <T> Value<T> of(T literal)                                // Literal value
```

---

## UI Command Builder

### UICommandBuilder
**Package:** `com.hypixel.hytale.server.core.ui`

Build UI commands for updating client UI elements.

#### Element Operations
```java
UICommandBuilder clear(String elementId)            // Clear element contents
UICommandBuilder remove(String elementId)           // Remove element from DOM
```

#### Append Operations
```java
UICommandBuilder append(String xml)                          // Append to root
UICommandBuilder append(String elementId, String xml)        // Append to element
UICommandBuilder appendInline(String elementId, String xml)  // Append inline
```

#### Insert Operations
```java
UICommandBuilder insertBefore(String elementId, String xml)
UICommandBuilder insertBeforeInline(String elementId, String xml)
```

#### Set Property Operations
```java
// By Value<T>
<T> UICommandBuilder set(String path, Value<T> value)

// By type
UICommandBuilder set(String path, String value)
UICommandBuilder set(String path, Message message)
UICommandBuilder set(String path, boolean value)
UICommandBuilder set(String path, float value)
UICommandBuilder set(String path, int value)
UICommandBuilder set(String path, double value)

// Object and collections
UICommandBuilder setObject(String path, Object value)
<T> UICommandBuilder set(String path, T[] array)
<T> UICommandBuilder set(String path, List<T> list)
UICommandBuilder setNull(String path)
```

#### Get Commands
```java
CustomUICommand[] getCommands()
```

---

## UI Event Builder

### UIEventBuilder
**Package:** `com.hypixel.hytale.server.core.ui`

Build UI event handlers for client interaction.

```java
// Add event bindings
UIEventBuilder addEventBinding(CustomUIEventBindingType type, String elementId)
UIEventBuilder addEventBinding(CustomUIEventBindingType type, String elementId, boolean capture)
UIEventBuilder addEventBinding(CustomUIEventBindingType type, String elementId, EventData data)
UIEventBuilder addEventBinding(CustomUIEventBindingType type, String elementId, EventData data, boolean capture)

// Get events
CustomUIEventBinding[] getEvents()
```

### EventData
**Package:** `com.hypixel.hytale.server.core.ui`

Container for UI event data passed to handlers.

---

## Player UI Managers

Access via `Player`:

```java
Player player = store.getComponent(ref, Player.getComponentType());
WindowManager windows = player.getWindowManager();
PageManager pages = player.getPageManager();
HudManager hud = player.getHudManager();
HotbarManager hotbar = player.getHotbarManager();
```

---

### WindowManager
**Package:** `com.hypixel.hytale.server.core.ui`

Manage player windows (inventory, crafting, etc.).

```java
// Open windows
OpenWindow openWindow(Window window)
List<OpenWindow> openWindows(Window... windows)

// Set/Get by slot
void setWindow(int slotId, Window window)
Window getWindow(int slotId)
List<Window> getWindows()

// Update
void updateWindow(Window window)
void updateWindows()
void markWindowChanged(int slotId)

// Close
Window closeWindow(int slotId)
void closeAllWindows()

// Validation
void validateWindows()
```

---

### PageManager
**Package:** `com.hypixel.hytale.server.core.ui`

Manage player pages (full-screen UIs).

```java
// Initialization
void init(PlayerRef playerRef, WindowManager windowManager)

// Custom page access
CustomUIPage getCustomPage()

// Set pages
void setPage(Ref<EntityStore> ref, Store<EntityStore> store, Page page)
void setPage(Ref<EntityStore> ref, Store<EntityStore> store, Page page, boolean animate)

// Open custom pages
void openCustomPage(Ref<EntityStore> ref, Store<EntityStore> store, CustomUIPage page)

// With windows
boolean setPageWithWindows(Ref<EntityStore> ref, Store<EntityStore> store, Page page, boolean animate, Window... windows)
boolean openCustomPageWithWindows(Ref<EntityStore> ref, Store<EntityStore> store, CustomUIPage page, Window... windows)

// Update
void updateCustomPage(CustomPage page)

// Event handling
void handleEvent(Ref<EntityStore> ref, Store<EntityStore> store, CustomPageEvent event)
```

---

### HudManager
**Package:** `com.hypixel.hytale.server.core.ui`

Manage HUD visibility and custom HUD elements.

```java
// Custom HUD
CustomUIHud getCustomHud()

// Visibility control
Set<HudComponent> getVisibleHudComponents()
void setVisibleHudComponents(PlayerRef playerRef, HudComponent... components)
void setVisibleHudComponents(PlayerRef playerRef, Set<HudComponent> components)

// Show/Hide specific components
void showHudComponents(PlayerRef playerRef, HudComponent... components)
void showHudComponents(PlayerRef playerRef, Set<HudComponent> components)
void hideHudComponents(PlayerRef playerRef, HudComponent... components)

// Custom HUD
void setCustomHud(PlayerRef playerRef, CustomUIHud hud)

// Reset
void resetHud(PlayerRef playerRef)
void resetUserInterface(PlayerRef playerRef)

// Network
void sendVisibleHudComponents(PacketHandler handler)
```

---

### HotbarManager
**Package:** `com.hypixel.hytale.server.core.ui`

Manage player hotbar configurations.

```java
// Constants
static final int HOTBARS_MAX

// Save/Load hotbar configurations
void saveHotbar(Ref<EntityStore> ref, short index, ComponentAccessor<EntityStore> accessor)
void loadHotbar(Ref<EntityStore> ref, short index, ComponentAccessor<EntityStore> accessor)

// State
int getCurrentHotbarIndex()
boolean getIsCurrentlyLoadingHotbar()
```

---

## File Browser System

### FileBrowserConfig
**Package:** `com.hypixel.hytale.server.core.ui`

Configuration record for file browser UI.

```java
// Builder pattern
static FileBrowserConfig.Builder builder()

// Accessor methods
String listElementId()
String rootSelectorId()
String searchInputId()
String currentPathId()
List<RootEntry> roots()
Set<String> allowedExtensions()
boolean enableRootSelector()
boolean enableSearch()
boolean enableDirectoryNav()
boolean enableMultiSelect()
int maxResults()
FileListProvider customProvider()
```

### FileBrowserConfig.Builder

```java
Builder listElementId(String id)
Builder rootSelectorId(String id)
Builder searchInputId(String id)
Builder currentPathId(String id)
Builder roots(List<RootEntry> roots)
Builder allowedExtensions(Set<String> extensions)
Builder enableRootSelector(boolean enable)
Builder enableSearch(boolean enable)
Builder enableDirectoryNav(boolean enable)
Builder enableMultiSelect(boolean enable)
Builder maxResults(int max)
Builder customProvider(FileListProvider provider)
FileBrowserConfig build()
```

---

### ServerFileBrowser
**Package:** `com.hypixel.hytale.server.core.ui`

Server-side file browser implementation.

#### Constructors
```java
ServerFileBrowser(FileBrowserConfig config)
ServerFileBrowser(FileBrowserConfig config, Path rootPath, Path currentPath)
```

#### Build UI
```java
void buildRootSelector(UICommandBuilder cmdBuilder, UIEventBuilder eventBuilder)
void buildSearchInput(UICommandBuilder cmdBuilder, UIEventBuilder eventBuilder)
void buildCurrentPath(UICommandBuilder cmdBuilder)
void buildFileList(UICommandBuilder cmdBuilder, UIEventBuilder eventBuilder)
void buildUI(UICommandBuilder cmdBuilder, UIEventBuilder eventBuilder)
```

#### Event Handling
```java
boolean handleEvent(FileBrowserEventData eventData)
```

#### Navigation
```java
Path getRoot()
void setRoot(Path path)
Path getCurrentDir()
void setCurrentDir(Path path)
String getSearchQuery()
void setSearchQuery(String query)
void navigateUp()
void navigateTo(Path path)
```

#### Selection
```java
Set<String> getSelectedItems()
void addSelection(String item)
void clearSelection()
```

#### Utility
```java
FileBrowserConfig getConfig()
Path resolveSecure(String path)      // Secure path resolution
Path resolveFromCurrent(String path)
```

---

## Supporting Classes

### LocalizableString
Translatable UI strings with parameter support.

### DropdownEntryInfo
Dropdown menu entry configuration.

### ItemGridSlot
Inventory grid slot definition for UI display.

### PatchStyle
UI styling/theming support.

---

## Usage Examples

### Create and Update Custom UI
```java
@Override
protected void execute(CommandContext ctx, Store<EntityStore> store,
                      Ref<EntityStore> ref, PlayerRef playerRef, World world) {
    Player player = store.getComponent(ref, Player.getComponentType());

    // Build UI commands
    UICommandBuilder builder = new UICommandBuilder();
    builder.set("score.value", 100)
           .set("player.name", playerRef.getUsername())
           .append("container", "<text id='greeting'>Hello!</text>");

    // Build event handlers
    UIEventBuilder events = new UIEventBuilder();
    events.addEventBinding(CustomUIEventBindingType.CLICK, "button");

    // Send to client via managers
    // Implementation depends on specific UI type (Page, Window, HUD)
}
```

### Show/Hide HUD Components
```java
Player player = store.getComponent(ref, Player.getComponentType());
HudManager hud = player.getHudManager();

// Hide all HUD
hud.setVisibleHudComponents(playerRef);

// Show specific components
hud.showHudComponents(playerRef, HudComponent.HEALTH, HudComponent.HOTBAR);
```

### Open Custom Page
```java
Player player = store.getComponent(ref, Player.getComponentType());
PageManager pages = player.getPageManager();

CustomUIPage customPage = ...; // Create custom page
pages.openCustomPage(ref, store, customPage);
```

### Window Management
```java
Player player = store.getComponent(ref, Player.getComponentType());
WindowManager windows = player.getWindowManager();

Window inventoryWindow = ...; // Create window
OpenWindow opened = windows.openWindow(inventoryWindow);

// Later, close it
windows.closeWindow(opened.getSlotId());
```

### File Browser Setup
```java
FileBrowserConfig config = FileBrowserConfig.builder()
    .listElementId("file-list")
    .enableSearch(true)
    .enableDirectoryNav(true)
    .allowedExtensions(Set.of("json", "txt"))
    .maxResults(50)
    .build();

ServerFileBrowser browser = new ServerFileBrowser(config);

UICommandBuilder cmd = new UICommandBuilder();
UIEventBuilder events = new UIEventBuilder();
browser.buildUI(cmd, events);
```

---

## UI Events

### WindowCloseEvent

**Package:** `com.hypixel.hytale.server.core.entity.entities.player.windows`

Event fired when a player window is closed. Implements `IEvent<Void>`.

This is a minimal event that signals window closure. It does not provide additional methods beyond the basic event interface.

### Usage Example

```java
import com.hypixel.hytale.server.core.entity.entities.player.windows.WindowCloseEvent;

@Override
protected void setup() {
    getEventRegistry().register(WindowCloseEvent.class, event -> {
        System.out.println("A window was closed");
    });
}
```

---

## Notes
- UI systems are XML-based; understand the client protocol for full usage
- Window/Page/HUD managers handle network synchronization automatically
- Use `Value.ref()` for dynamic bindings that update when document values change
- File browser provides secure path resolution to prevent directory traversal
