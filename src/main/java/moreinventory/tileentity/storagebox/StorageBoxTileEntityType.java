package moreinventory.tileentity.storagebox;

import java.util.HashMap;

import moreinventory.tileentity.BaseStorageBoxTileEntity;
import moreinventory.tileentity.TileEntities;
import net.minecraft.tileentity.TileEntityType;

public class StorageBoxTileEntityType {
    public static HashMap<StorageBoxType, TileEntityType<BaseStorageBoxTileEntity>> map = new HashMap<>();
    public static HashMap<StorageBoxType, Class<? extends BaseStorageBoxTileEntity>> classMap = new HashMap<>();

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

        classMap.put(StorageBoxType.WOOD, WoodStorageBoxTileEntity.class);
        classMap.put(StorageBoxType.IRON, IronStorageBoxTileEntity.class);
        classMap.put(StorageBoxType.GOLD, GoldStorageBoxTileEntity.class);
        classMap.put(StorageBoxType.DIAMOND, DiamondStorageBoxTileEntity.class);
        classMap.put(StorageBoxType.EMERALD, EmeraldStorageBoxTileEntity.class);

        classMap.put(StorageBoxType.COPPER, CopperStorageBoxTileEntity.class);
        classMap.put(StorageBoxType.TIN, TinStorageBoxTileEntity.class);
        classMap.put(StorageBoxType.BRONZE, BronzeStorageBoxTileEntity.class);
        classMap.put(StorageBoxType.SILVER, SilverStorageBoxTileEntity.class);

        classMap.put(StorageBoxType.GLASS, GlassStorageBoxTileEntity.class);

    }
}
