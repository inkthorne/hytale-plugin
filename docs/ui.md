# UI API

## Overview
**Package:** `com.hypixel.hytale.server.core.ui`

Hytale provides UI building APIs for creating custom interfaces.

## Key Classes

### Positioning
- `Anchor` - UI element anchoring
- `Area` - UI area/bounds definition

### Builders
- `UICommandBuilder` - Build UI commands
- `UIEventBuilder` - Build UI event handlers
- `EventData` - UI event data container

### Components
- `DropdownEntryInfo` - Dropdown menu entries
- `ItemGridSlot` - Inventory grid slots

### File Browser
- `FileBrowserConfig` - Configure file browser UI
- `FileBrowserConfig.Builder` - Build file browser configs
- `ServerFileBrowser` - Server-side file browser
- `FileListProvider` - Provide file listings

## Player UI Managers

Access via `Player`:

```java
Player player = store.getComponent(ref, Player.getComponentType());

// Window management
WindowManager windows = player.getWindowManager();

// Page management
PageManager pages = player.getPageManager();

// HUD management
HudManager hud = player.getHudManager();

// Hotbar management
HotbarManager hotbar = player.getHotbarManager();
```

## Notes
- UI systems are complex; explore specific class APIs as needed
- Most UI interaction requires understanding the client protocol
- Window/Page/HUD managers handle sending UI updates to clients
