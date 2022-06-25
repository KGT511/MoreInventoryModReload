package moreinventory.blockentity.storagebox;

import java.util.TreeMap;

import moreinventory.block.Blocks;
import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import moreinventory.blockentity.BlockEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class StorageBoxTypeBlockEntity {
    public static TreeMap<StorageBoxType, BlockEntityType<? extends BaseStorageBoxBlockEntity>> map = new TreeMap<>();
    public static TreeMap<StorageBoxType, Class<? extends BaseStorageBoxBlockEntity>> classMap = new TreeMap<>();
    public static TreeMap<StorageBoxType, Block> blockMap = new TreeMap<>();

    public static void init() {
        map.put(StorageBoxType.WOOD, BlockEntities.WOOD_STORAGE_BOX_BLOCK_ENTITY_TYPE.get());
        map.put(StorageBoxType.IRON, BlockEntities.IRON_STORAGE_BOX_BLOCK_ENTITY_TYPE.get());
        map.put(StorageBoxType.GOLD, BlockEntities.GOLD_STORAGE_BOX_BLOCK_ENTITY_TYPE.get());
        map.put(StorageBoxType.DIAMOND, BlockEntities.DIAMOND_STORAGE_BOX_BLOCK_ENTITY_TYPE.get());
        map.put(StorageBoxType.EMERALD, BlockEntities.EMERALD_STORAGE_BOX_BLOCK_ENTITY_TYPE.get());

        map.put(StorageBoxType.COPPER, BlockEntities.COPPER_STORAGE_BOX_BLOCK_ENTITY_TYPE.get());
        map.put(StorageBoxType.TIN, BlockEntities.TIN_STORAGE_BOX_BLOCK_ENTITY_TYPE.get());
        map.put(StorageBoxType.BRONZE, BlockEntities.BRONZE_STORAGE_BOX_BLOCK_ENTITY_TYPE.get());
        map.put(StorageBoxType.SILVER, BlockEntities.SILVER_STORAGE_BOX_BLOCK_ENTITY_TYPE.get());

        map.put(StorageBoxType.GLASS, BlockEntities.GLASS_STORAGE_BOX_BLOCK_ENTITY_TYPE.get());

        classMap.put(StorageBoxType.WOOD, WoodStorageBoxBlockEntity.class);
        classMap.put(StorageBoxType.IRON, IronStorageBoxBlockEntity.class);
        classMap.put(StorageBoxType.GOLD, GoldStorageBoxBlockEntity.class);
        classMap.put(StorageBoxType.DIAMOND, DiamondStorageBoxBlockEntity.class);
        classMap.put(StorageBoxType.EMERALD, EmeraldStorageBoxBlockEntity.class);

        classMap.put(StorageBoxType.COPPER, CopperStorageBoxBlockEntity.class);
        classMap.put(StorageBoxType.TIN, TinStorageBoxBlockEntity.class);
        classMap.put(StorageBoxType.BRONZE, BronzeStorageBoxBlockEntity.class);
        classMap.put(StorageBoxType.SILVER, SilverStorageBoxBlockEntity.class);

        classMap.put(StorageBoxType.GLASS, GlassStorageBoxBlockEntity.class);

        blockMap.put(StorageBoxType.WOOD, Blocks.WOOD_STORAGE_BOX.get());
        blockMap.put(StorageBoxType.IRON, Blocks.IRON_STORAGE_BOX.get());
        blockMap.put(StorageBoxType.GOLD, Blocks.GOLD_STORAGE_BOX.get());
        blockMap.put(StorageBoxType.DIAMOND, Blocks.DIAMOND_STORAGE_BOX.get());
        blockMap.put(StorageBoxType.EMERALD, Blocks.EMERALD_STORAGE_BOX.get());

        blockMap.put(StorageBoxType.COPPER, Blocks.COPPER_STORAGE_BOX.get());
        blockMap.put(StorageBoxType.TIN, Blocks.TIN_STORAGE_BOX.get());
        blockMap.put(StorageBoxType.BRONZE, Blocks.BRONZE_STORAGE_BOX.get());
        blockMap.put(StorageBoxType.SILVER, Blocks.SILVER_STORAGE_BOX.get());

        blockMap.put(StorageBoxType.GLASS, Blocks.GLASS_STORAGE_BOX.get());
    }
}
