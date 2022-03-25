package moreinventory.tileentity.storagebox;

import java.util.HashMap;

import moreinventory.tileentity.BaseTileEntityStorageBox;
import moreinventory.tileentity.TileEntities;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityStorageBoxType {
    public static HashMap<StorageBoxType, TileEntityType<BaseTileEntityStorageBox>> map = new HashMap<>();
    public static HashMap<StorageBoxType, Class<? extends BaseTileEntityStorageBox>> classMap = new HashMap<>();

    public static void init() {
        map.put(StorageBoxType.WOOD, TileEntities.STORAGE_BOX_WOOD_TILE_TYPE);
        map.put(StorageBoxType.IRON, TileEntities.STORAGE_BOX_IRON_TILE_TYPE);
        map.put(StorageBoxType.GOLD, TileEntities.STORAGE_BOX_GOLD_TILE_TYPE);
        map.put(StorageBoxType.DIAMOND, TileEntities.STORAGE_BOX_DIAMOND_TILE_TYPE);
        map.put(StorageBoxType.EMERALD, TileEntities.STORAGE_BOX_EMERALD_TILE_TYPE);

        map.put(StorageBoxType.COPPER, TileEntities.STORAGE_BOX_COPPER_TILE_TYPE);
        map.put(StorageBoxType.TIN, TileEntities.STORAGE_BOX_TIN_TILE_TYPE);
        map.put(StorageBoxType.BRONZE, TileEntities.STORAGE_BOX_BRONZE_TILE_TYPE);
        map.put(StorageBoxType.SILVER, TileEntities.STORAGE_BOX_SILVER_TILE_TYPE);

        map.put(StorageBoxType.GLASS, TileEntities.STORAGE_BOX_GLASS_TILE_TYPE);

        classMap.put(StorageBoxType.WOOD, TileEntityStorageBoxWood.class);
        classMap.put(StorageBoxType.IRON, TileEntityStorageBoxIron.class);
        classMap.put(StorageBoxType.GOLD, TileEntityStorageBoxGold.class);
        classMap.put(StorageBoxType.DIAMOND, TileEntityStorageBoxDiamond.class);
        classMap.put(StorageBoxType.EMERALD, TileEntityStorageBoxEmerald.class);

        classMap.put(StorageBoxType.COPPER, TileEntityStorageBoxCopper.class);
        classMap.put(StorageBoxType.TIN, TileEntityStorageBoxTin.class);
        classMap.put(StorageBoxType.BRONZE, TileEntityStorageBoxBronze.class);
        classMap.put(StorageBoxType.SILVER, TileEntityStorageBoxSilver.class);

        classMap.put(StorageBoxType.GLASS, TileEntityStorageBoxGlass.class);

    }
}
