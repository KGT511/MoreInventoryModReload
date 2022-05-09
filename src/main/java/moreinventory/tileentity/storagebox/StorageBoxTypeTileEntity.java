package moreinventory.tileentity.storagebox;

import java.util.TreeMap;

import moreinventory.block.Blocks;
import moreinventory.tileentity.BaseStorageBoxTileEntity;
import moreinventory.tileentity.TileEntities;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;

public class StorageBoxTypeTileEntity {
    public static final TreeMap<StorageBoxType, TileEntityType<BaseStorageBoxTileEntity>> map = new TreeMap<>();
    public static final TreeMap<StorageBoxType, Class<? extends BaseStorageBoxTileEntity>> classMap = new TreeMap<>();
    public static final TreeMap<StorageBoxType, Block> blockMap = new TreeMap<>();

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

        blockMap.put(StorageBoxType.WOOD, Blocks.WOOD_STORAGE_BOX);
        blockMap.put(StorageBoxType.IRON, Blocks.IRON_STORAGE_BOX);
        blockMap.put(StorageBoxType.GOLD, Blocks.GOLD_STORAGE_BOX);
        blockMap.put(StorageBoxType.DIAMOND, Blocks.DIAMOND_STORAGE_BOX);
        blockMap.put(StorageBoxType.EMERALD, Blocks.EMERALD_STORAGE_BOX);

        blockMap.put(StorageBoxType.COPPER, Blocks.COPPER_STORAGE_BOX);
        blockMap.put(StorageBoxType.TIN, Blocks.TIN_STORAGE_BOX);
        blockMap.put(StorageBoxType.BRONZE, Blocks.BRONZE_STORAGE_BOX);
        blockMap.put(StorageBoxType.SILVER, Blocks.SILVER_STORAGE_BOX);

        blockMap.put(StorageBoxType.GLASS, Blocks.GLASS_STORAGE_BOX);

    }
}
