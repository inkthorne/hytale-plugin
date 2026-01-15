# Inventory API

## Inventory
**Package:** `com.hypixel.hytale.server.core.inventory`

Player inventory with multiple sections.

### Section IDs
```java
int HOTBAR_SECTION_ID
int STORAGE_SECTION_ID
int ARMOR_SECTION_ID
int UTILITY_SECTION_ID
int TOOLS_SECTION_ID
int BACKPACK_SECTION_ID
```

### Get Sections
```java
ItemContainer getHotbar()
ItemContainer getStorage()
ItemContainer getArmor()
ItemContainer getUtility()
ItemContainer getTools()
ItemContainer getBackpack()
ItemContainer getSectionById(int sectionId)
```

### Combined Containers
```java
CombinedItemContainer getCombinedHotbarFirst()
CombinedItemContainer getCombinedStorageFirst()
CombinedItemContainer getCombinedEverything()
CombinedItemContainer getCombinedArmorHotbarStorage()
CombinedItemContainer getCombinedBackpackStorageHotbar()
```

### Active Slots
```java
// Hotbar
byte getActiveHotbarSlot()
void setActiveHotbarSlot(byte slot)
ItemStack getActiveHotbarItem()

// Tools
byte getActiveToolsSlot()
void setActiveToolsSlot(byte slot)
ItemStack getToolsItem()
ItemStack getActiveToolItem()
boolean usingToolsItem()
void setUsingToolsItem(boolean using)

// Utility
byte getActiveUtilitySlot()
void setActiveUtilitySlot(byte slot)
ItemStack getUtilityItem()

// General
byte getActiveSlot(int sectionId)
void setActiveSlot(int sectionId, byte slot)
ItemStack getItemInHand()
```

### Item Operations
```java
void moveItem(int fromSection, int fromSlot, int toSection, int toSlot, int count)
void smartMoveItem(int section, int slot, int count, SmartMoveType type)
ListTransaction<MoveTransaction<ItemStackTransaction>> takeAll(int section)
ListTransaction<MoveTransaction<ItemStackTransaction>> putAll(int section)
ListTransaction<MoveTransaction<ItemStackTransaction>> quickStack(int section)
List<ItemStack> dropAllItemStacks()
void clear()
```

### Sorting & Management
```java
void sortStorage(SortType sortType)
void setSortType(SortType sortType)
void resizeBackpack(short size, List<ItemStack> overflow)
void markChanged()
boolean consumeIsDirty()
boolean consumeNeedsSaving()
```

## ItemStack
**Package:** `com.hypixel.hytale.server.core.inventory`

Represents an item with count and metadata.

```java
static final ItemStack EMPTY  // Empty stack constant
```

## Usage Examples

### Get Player Inventory
```java
Player player = store.getComponent(ref, Player.getComponentType());
Inventory inventory = player.getInventory();
```

### Check Held Item
```java
ItemStack heldItem = inventory.getItemInHand();
if (heldItem != ItemStack.EMPTY) {
    // Player is holding something
}
```

### Get Hotbar Contents
```java
ItemContainer hotbar = inventory.getHotbar();
// Iterate or access slots
```

### Access via LivingEntity
```java
// LivingEntity also has inventory access
LivingEntity entity = ...;
Inventory inv = entity.getInventory();
entity.setInventory(newInventory);
```
