# Inventory API

## Inventory
**Package:** `com.hypixel.hytale.server.core.inventory`

Player inventory with multiple sections.

### Section IDs
```java
static final int HOTBAR_SECTION_ID
static final int STORAGE_SECTION_ID
static final int ARMOR_SECTION_ID
static final int UTILITY_SECTION_ID
static final int TOOLS_SECTION_ID
static final int BACKPACK_SECTION_ID
```

### Default Capacities
```java
static final int DEFAULT_HOTBAR_CAPACITY
static final int DEFAULT_UTILITY_CAPACITY
static final int DEFAULT_TOOLS_CAPACITY
static final int DEFAULT_ARMOR_CAPACITY
static final int DEFAULT_STORAGE_ROWS
static final int DEFAULT_STORAGE_COLUMNS
static final int DEFAULT_STORAGE_CAPACITY
static final int INACTIVE_SLOT_INDEX
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
CombinedItemContainer getCombinedArmorHotbarUtilityStorage()
CombinedItemContainer getCombinedHotbarUtilityConsumableStorage()
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
boolean containsBrokenItem()
```

---

## ItemStack
**Package:** `com.hypixel.hytale.server.core.inventory`

Represents an item with quantity and metadata. **Immutable** - modification methods return new instances.

### Constants
```java
static final ItemStack EMPTY          // Empty stack constant
static final ItemStack[] EMPTY_ARRAY  // Empty array constant
```

### Constructors
```java
ItemStack(String itemId)
ItemStack(String itemId, int quantity)
ItemStack(String itemId, int quantity, BsonDocument metadata)
ItemStack(String itemId, int quantity, double durability, double maxDurability, BsonDocument metadata)
```

### Getters
```java
String getItemId()
int getQuantity()
BsonDocument getMetadata()
double getDurability()
double getMaxDurability()
boolean isUnbreakable()
boolean isBroken()
boolean isEmpty()
boolean isValid()
Item getItem()
String getBlockKey()
boolean getOverrideDroppedItemAnimation()
```

### Modification Methods (Return New ItemStack)
```java
// Durability
ItemStack withDurability(double durability)
ItemStack withMaxDurability(double maxDurability)
ItemStack withIncreasedDurability(double amount)
ItemStack withRestoredDurability(double amount)

// State & Quantity
ItemStack withState(String state)
ItemStack withQuantity(int quantity)

// Metadata
ItemStack withMetadata(BsonDocument metadata)
<T> ItemStack withMetadata(KeyedCodec<T> codec, T value)
<T> ItemStack withMetadata(String key, Codec<T> codec, T value)
ItemStack withMetadata(String key, BsonValue value)
```

### Metadata Access
```java
<T> T getFromMetadataOrNull(KeyedCodec<T> codec)
<T> T getFromMetadataOrNull(String key, Codec<T> codec)
<T> T getFromMetadataOrDefault(String key, BuilderCodec<T> codec)
```

### Comparison Methods
```java
boolean isStackableWith(ItemStack other)
boolean isEquivalentType(ItemStack other)

// Static versions
static boolean isEmpty(ItemStack stack)
static boolean isStackableWith(ItemStack a, ItemStack b)
static boolean isEquivalentType(ItemStack a, ItemStack b)
static boolean isSameItemType(ItemStack a, ItemStack b)
```

### Static Factory
```java
static ItemStack fromPacket(ItemQuantity packet)
```

---

## ItemContainer
**Package:** `com.hypixel.hytale.server.core.inventory.container`

Abstract base class for inventory containers with filtering support.

### Capacity
```java
abstract short getCapacity()
ItemStack getItemStack(short slotIndex)
```

### Filtering
```java
abstract void setGlobalFilter(FilterType filterType)
abstract void setSlotFilter(FilterActionType actionType, short slotIndex, SlotFilter filter)
```

### Adding Items
```java
boolean canAddItemStack(ItemStack item)
boolean canAddItemStack(ItemStack item, boolean addAllOrNothing, boolean fullStacks)

ItemStackTransaction addItemStack(ItemStack item)
ItemStackTransaction addItemStack(ItemStack item, boolean addAllOrNothing, boolean fullStacks, boolean filter)
ItemStackTransaction addItemStacks(List<ItemStack> items)
ItemStackTransaction addItemStacksOrdered(List<ItemStack> items)
ItemStackTransaction addItemStacksOrdered(List<ItemStack> items, short startSlot)
ItemStackTransaction addItemStacksOrdered(List<ItemStack> items, short startSlot, boolean addAllOrNothing, boolean fullStacks)
```

### Removing Items
```java
// By slot
ItemStack removeItemStackFromSlot(short slotIndex)
ItemStackSlotTransaction removeItemStackFromSlot(short slotIndex, int quantity)
ItemStackSlotTransaction removeItemStackFromSlot(short slotIndex, ItemStack item, int quantity)

// By item
ItemStackTransaction removeItemStack(ItemStack item)
ItemStackTransaction removeItemStacks(List<ItemStack> items)
```

### Removing Materials
```java
MaterialSlotTransaction removeMaterialFromSlot(short slotIndex, MaterialQuantity material)
MaterialTransaction removeMaterial(MaterialQuantity material)
MaterialTransaction removeMaterials(List<MaterialQuantity> materials)
```

### Removing Resources
```java
ResourceSlotTransaction removeResourceFromSlot(short slotIndex, ResourceQuantity resource)
ResourceTransaction removeResource(ResourceQuantity resource)
```

### Moving Items
```java
MoveTransaction<ItemStackTransaction> moveItemStackFromSlot(short slotIndex, ItemContainer destination)
MoveTransaction<SlotTransaction> moveItemStackFromSlotToSlot(short slotIndex, int quantity, ItemContainer destination, short destSlot)
ListTransaction<MoveTransaction<ItemStackTransaction>> moveAllItemStacksTo(ItemContainer... destinations)
ListTransaction<MoveTransaction<ItemStackTransaction>> quickStackTo(ItemContainer... destinations)
ListTransaction<MoveTransaction<SlotTransaction>> combineItemStacksIntoSlot(ItemContainer source, short slotIndex)
ListTransaction<MoveTransaction<SlotTransaction>> swapItems(short slot, ItemContainer container, short containerSlot, short targetSlot)
```

### Utility
```java
ClearTransaction clear()
List<ItemStack> removeAllItemStacks()
List<ItemStack> dropAllItemStacks()
boolean isEmpty()
int countItemStacks(Predicate<ItemStack> filter)
boolean containsItemStacksStackableWith(ItemStack item)
void forEach(ShortObjectConsumer<ItemStack> consumer)
ListTransaction<SlotTransaction> sortItems(SortType sortType)
```

### Events
```java
EventRegistration registerChangeEvent(Consumer<ItemContainerChangeEvent> handler)
EventRegistration registerChangeEvent(EventPriority priority, Consumer<ItemContainerChangeEvent> handler)
EventRegistration registerChangeEvent(short slotIndex, Consumer<ItemContainerChangeEvent> handler)
```

---

## SimpleItemContainer
**Package:** `com.hypixel.hytale.server.core.inventory.container`

Thread-safe concrete implementation of ItemContainer.

```java
// Constructor
SimpleItemContainer(short capacity)

// Static factory
static ItemContainer getNewContainer(short capacity)

// Utility methods with drop support
static boolean addOrDropItemStack(ComponentAccessor<EntityStore> accessor, Ref<EntityStore> ref, ItemContainer container, ItemStack item)
static boolean addOrDropItemStack(ComponentAccessor<EntityStore> accessor, Ref<EntityStore> ref, ItemContainer container, short startSlot, ItemStack item)
static boolean addOrDropItemStacks(ComponentAccessor<EntityStore> accessor, Ref<EntityStore> ref, ItemContainer container, List<ItemStack> items)
static boolean tryAddOrderedOrDropItemStacks(ComponentAccessor<EntityStore> accessor, Ref<EntityStore> ref, ItemContainer container, List<ItemStack> items)
```

---

## CombinedItemContainer
**Package:** `com.hypixel.hytale.server.core.inventory.container`

Combines multiple ItemContainers into a single logical container.

```java
// Constructor
CombinedItemContainer(ItemContainer... containers)

// Access
ItemContainer getContainer(int index)
int getContainersSize()
ItemContainer getContainerForSlot(short slotIndex)
short getCapacity()
CombinedItemContainer clone()
```

---

## SortType
**Package:** `com.hypixel.hytale.server.core.inventory.container`

Enum for sorting options.

```java
public enum SortType {
    NAME,   // Sort alphabetically by item name
    TYPE,   // Sort by item type/category
    RARITY  // Sort by item rarity

    Comparator<ItemStack> getComparator()
}
```

---

## FilterType
**Package:** `com.hypixel.hytale.server.core.inventory.container.filter`

Enum for container-level filtering.

```java
public enum FilterType {
    ALLOW_INPUT_ONLY,   // Only allow items in
    ALLOW_OUTPUT_ONLY,  // Only allow items out
    ALLOW_ALL,          // Allow input and output
    DENY_ALL            // Block all operations

    boolean allowInput()
    boolean allowOutput()
}
```

---

## MaterialQuantity
**Package:** `com.hypixel.hytale.server.core.inventory`

Represents a quantity of crafting material.

### Fields
```java
String itemId
String resourceTypeId
String tag
int tagIndex
int quantity
BsonDocument metadata
```

### Methods
```java
ItemStack toItemStack()
ResourceQuantity toResource()
MaterialQuantity clone(int newQuantity)
```

---

## ResourceQuantity
**Package:** `com.hypixel.hytale.server.core.inventory`

Represents a quantity of a resource.

### Fields
```java
String resourceId
int quantity
```

### Methods
```java
String getResourceId()
int getQuantity()
ResourceQuantity clone(int newQuantity)
ItemResourceType getResourceType(Item item)
```

---

## Transaction System

All item operations return Transaction objects indicating success/failure.

### ItemStackTransaction
```java
boolean succeeded()
boolean wasSlotModified(short slotIndex)
ActionType getAction()              // Operation type
ItemStack getQuery()                // Original item query
ItemStack getRemainder()            // Leftover items not processed
boolean isAllOrNothing()
boolean isFilter()                  // Whether filtering blocked operation
List<ItemStackSlotTransaction> getSlotTransactions()
```

### ListTransaction<T extends Transaction>
```java
boolean succeeded()
List<T> getList()
int size()
static <T> ListTransaction<T> getEmptyTransaction(boolean success)
```

### MoveTransaction<T extends Transaction>
Wraps source and destination transactions for move operations.

---

## Usage Examples

### Get Player Inventory
```java
Player player = store.getComponent(ref, Player.getComponentType());
Inventory inventory = player.getInventory();
```

### Check Held Item
```java
ItemStack heldItem = inventory.getItemInHand();
if (!heldItem.isEmpty()) {
    String itemId = heldItem.getItemId();
    int count = heldItem.getQuantity();
    playerRef.sendMessage(Message.raw("Holding: " + itemId + " x" + count));
}
```

### Add Item to Inventory
```java
ItemStack newItem = new ItemStack("my_item", 10);
ItemContainer hotbar = inventory.getHotbar();
ItemStackTransaction result = hotbar.addItemStack(newItem);
if (result.succeeded()) {
    playerRef.sendMessage(Message.raw("Item added!"));
} else {
    ItemStack remainder = result.getRemainder();
    // Handle overflow
}
```

### Move Items Between Containers
```java
ItemContainer hotbar = inventory.getHotbar();
ItemContainer storage = inventory.getStorage();

// Move all from hotbar to storage
ListTransaction<MoveTransaction<ItemStackTransaction>> result =
    hotbar.moveAllItemStacksTo(storage);

if (result.succeeded()) {
    playerRef.sendMessage(Message.raw("Items moved to storage"));
}
```

### Listen for Inventory Changes
```java
ItemContainer hotbar = inventory.getHotbar();
hotbar.registerChangeEvent(event -> {
    playerRef.sendMessage(Message.raw("Hotbar changed!"));
});
```

### Sort Storage
```java
inventory.sortStorage(SortType.TYPE);
```

### Create ItemStack with Metadata
```java
ItemStack item = new ItemStack("magic_sword", 1)
    .withDurability(100.0)
    .withMaxDurability(100.0)
    .withMetadata("enchantment", someCodec, enchantmentData);
```

### Access via LivingEntity
```java
LivingEntity entity = ...;
Inventory inv = entity.getInventory();
entity.setInventory(newInventory);
entity.setInventory(newInventory, true);  // with notification
```
