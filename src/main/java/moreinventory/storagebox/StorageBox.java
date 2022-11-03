package moreinventory.storagebox;

import java.util.TreeMap;

import moreinventory.block.Blocks;
import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import moreinventory.blockentity.BlockEntities;
import moreinventory.blockentity.storagebox.BronzeStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.CopperStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.DiamondStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.EmeraldStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.GlassStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.GoldStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.IronStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.SilverStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.TinStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.WoodStorageBoxBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class StorageBox {
    public static final TreeMap<StorageBoxType, StorageBox> storageBoxMap = new TreeMap<>();

    public int inventorySize;
    public BlockEntityType<? extends BaseStorageBoxBlockEntity> blockEntity;
    public Class<? extends BaseStorageBoxBlockEntity> entityClass;
    public Block block;
    public int tier;

    public StorageBox(int size,
            BlockEntityType<? extends BaseStorageBoxBlockEntity> blockEntity,
            Class<? extends BaseStorageBoxBlockEntity> entityClass,
            Block block,
            int tier) {
        this.inventorySize = size;
        this.blockEntity = blockEntity;
        this.entityClass = entityClass;
        this.block = block;
        this.tier = tier;
    }

    public static void init() {
        storageBoxMap.put(StorageBoxType.WOOD,
                new StorageBox(0, BlockEntities.WOOD_STORAGE_BOX_BLOCK_ENTITY_TYPE.get(), WoodStorageBoxBlockEntity.class, Blocks.WOOD_STORAGE_BOX.get(), 0));
        storageBoxMap.put(StorageBoxType.IRON,
                new StorageBox(0, BlockEntities.IRON_STORAGE_BOX_BLOCK_ENTITY_TYPE.get(), IronStorageBoxBlockEntity.class, Blocks.IRON_STORAGE_BOX.get(), 0));
        storageBoxMap.put(StorageBoxType.GOLD,
                new StorageBox(0, BlockEntities.GOLD_STORAGE_BOX_BLOCK_ENTITY_TYPE.get(), GoldStorageBoxBlockEntity.class, Blocks.GOLD_STORAGE_BOX.get(), 0));
        storageBoxMap.put(StorageBoxType.DIAMOND,
                new StorageBox(0, BlockEntities.DIAMOND_STORAGE_BOX_BLOCK_ENTITY_TYPE.get(), DiamondStorageBoxBlockEntity.class, Blocks.DIAMOND_STORAGE_BOX.get(), 0));
        storageBoxMap.put(StorageBoxType.EMERALD,
                new StorageBox(0, BlockEntities.EMERALD_STORAGE_BOX_BLOCK_ENTITY_TYPE.get(), EmeraldStorageBoxBlockEntity.class, Blocks.EMERALD_STORAGE_BOX.get(), 0));

        storageBoxMap.put(StorageBoxType.COPPER,
                new StorageBox(0, BlockEntities.COPPER_STORAGE_BOX_BLOCK_ENTITY_TYPE.get(), CopperStorageBoxBlockEntity.class, Blocks.COPPER_STORAGE_BOX.get(), 0));
        storageBoxMap.put(StorageBoxType.TIN,
                new StorageBox(0, BlockEntities.TIN_STORAGE_BOX_BLOCK_ENTITY_TYPE.get(), TinStorageBoxBlockEntity.class, Blocks.TIN_STORAGE_BOX.get(), 0));
        storageBoxMap.put(StorageBoxType.BRONZE,
                new StorageBox(0, BlockEntities.BRONZE_STORAGE_BOX_BLOCK_ENTITY_TYPE.get(), BronzeStorageBoxBlockEntity.class, Blocks.BRONZE_STORAGE_BOX.get(), 0));
        storageBoxMap.put(StorageBoxType.SILVER,
                new StorageBox(0, BlockEntities.SILVER_STORAGE_BOX_BLOCK_ENTITY_TYPE.get(), SilverStorageBoxBlockEntity.class, Blocks.SILVER_STORAGE_BOX.get(), 0));

        storageBoxMap.put(StorageBoxType.GLASS,
                new StorageBox(0, BlockEntities.GLASS_STORAGE_BOX_BLOCK_ENTITY_TYPE.get(), GlassStorageBoxBlockEntity.class, Blocks.GLASS_STORAGE_BOX.get(), 0));

    }

}
