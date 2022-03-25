package moreinventory.tileentity.storagebox;

import java.util.HashMap;

public class StorageBoxInventorySize {
    public static HashMap<StorageBoxType, StorageBoxInventorySize> map = new HashMap<>();

    private int inventorySize;

    public StorageBoxInventorySize(int size) {
        this.inventorySize = size;
    }

    public int getInventorySize() {
        return this.inventorySize;
    }

    public static void init() {
        map.put(StorageBoxType.WOOD, new StorageBoxInventorySize(64));
        map.put(StorageBoxType.IRON, new StorageBoxInventorySize(128));
        map.put(StorageBoxType.GOLD, new StorageBoxInventorySize(256));
        map.put(StorageBoxType.DIAMOND, new StorageBoxInventorySize(512));
        map.put(StorageBoxType.EMERALD, new StorageBoxInventorySize(1024));

        map.put(StorageBoxType.COPPER, new StorageBoxInventorySize(96));
        map.put(StorageBoxType.TIN, new StorageBoxInventorySize(96));
        map.put(StorageBoxType.BRONZE, new StorageBoxInventorySize(128));
        map.put(StorageBoxType.SILVER, new StorageBoxInventorySize(192));

        map.put(StorageBoxType.GLASS, new StorageBoxInventorySize(0));

    }
}
