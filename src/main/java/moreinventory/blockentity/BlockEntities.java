package moreinventory.blockentity;

import java.util.ArrayList;
import java.util.List;

import moreinventory.block.Blocks;
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
import moreinventory.core.MoreInventoryMOD;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockEntities {
    public static List<BlockEntityType<?>> blockEntityList = new ArrayList<>();

    public static BlockEntityType<CatchallBlockEntity> CATCHALL_BLOCK_ENTITY_TYPE = (BlockEntityType<CatchallBlockEntity>) register("catchall_tile",
            BlockEntityType.Builder.of(CatchallBlockEntity::new, Blocks.CATCHALL).build(null));

    public static BlockEntityType<BaseStorageBoxBlockEntity> WOOD_STORAGE_BOX_BLOCK_ENTITY_TYPE = (BlockEntityType<BaseStorageBoxBlockEntity>) register("storage_box_tile_wood",
            BlockEntityType.Builder.of(WoodStorageBoxBlockEntity::new, Blocks.WOOD_STORAGE_BOX).build(null));
    public static BlockEntityType<BaseStorageBoxBlockEntity> IRON_STORAGE_BOX_BLOCK_ENTITY_TYPE = (BlockEntityType<BaseStorageBoxBlockEntity>) register("storage_box_tile_iron",
            BlockEntityType.Builder.of(IronStorageBoxBlockEntity::new, Blocks.IRON_STORAGE_BOX).build(null));
    public static BlockEntityType<BaseStorageBoxBlockEntity> GOLD_STORAGE_BOX_BLOCK_ENTITY_TYPE = (BlockEntityType<BaseStorageBoxBlockEntity>) register("storage_box_tile_gold",
            BlockEntityType.Builder.of(GoldStorageBoxBlockEntity::new, Blocks.GOLD_STORAGE_BOX).build(null));
    public static BlockEntityType<BaseStorageBoxBlockEntity> DIAMOND_STORAGE_BOX_BLOCK_ENTITY_TYPE = (BlockEntityType<BaseStorageBoxBlockEntity>) register("storage_box_tile_diamond",
            BlockEntityType.Builder.of(DiamondStorageBoxBlockEntity::new, Blocks.DIAMOND_STORAGE_BOX).build(null));

    public static BlockEntityType<BaseStorageBoxBlockEntity> EMERALD_STORAGE_BOX_BLOCK_ENTITY_TYPE = (BlockEntityType<BaseStorageBoxBlockEntity>) register("storage_box_tile_emerald",
            BlockEntityType.Builder.of(EmeraldStorageBoxBlockEntity::new, Blocks.EMERALD_STORAGE_BOX).build(null));
    public static BlockEntityType<BaseStorageBoxBlockEntity> COPPER_STORAGE_BOX_BLOCK_ENTITY_TYPE = (BlockEntityType<BaseStorageBoxBlockEntity>) register("storage_box_tile_copper",
            BlockEntityType.Builder.of(CopperStorageBoxBlockEntity::new, Blocks.COPPER_STORAGE_BOX).build(null));
    public static BlockEntityType<BaseStorageBoxBlockEntity> TIN_STORAGE_BOX_BLOCK_ENTITY_TYPE = (BlockEntityType<BaseStorageBoxBlockEntity>) register("storage_box_tile_tin",
            BlockEntityType.Builder.of(TinStorageBoxBlockEntity::new, Blocks.TIN_STORAGE_BOX).build(null));
    public static BlockEntityType<BaseStorageBoxBlockEntity> BRONZE_STORAGE_BOX_BLOCK_ENTITY_TYPE = (BlockEntityType<BaseStorageBoxBlockEntity>) register("storage_box_tile_bronze",
            BlockEntityType.Builder.of(BronzeStorageBoxBlockEntity::new, Blocks.BRONZE_STORAGE_BOX).build(null));
    public static BlockEntityType<BaseStorageBoxBlockEntity> SILVER_STORAGE_BOX_BLOCK_ENTITY_TYPE = (BlockEntityType<BaseStorageBoxBlockEntity>) register("storage_box_tile_silver",
            BlockEntityType.Builder.of(SilverStorageBoxBlockEntity::new, Blocks.SILVER_STORAGE_BOX).build(null));

    public static BlockEntityType<BaseStorageBoxBlockEntity> GLASS_STORAGE_BOX_BLOCK_ENTITY_TYPE = (BlockEntityType<BaseStorageBoxBlockEntity>) register("storage_box_tile_glass",
            BlockEntityType.Builder.of(GlassStorageBoxBlockEntity::new, Blocks.GLASS_STORAGE_BOX).build(null));

    public static BlockEntityType<ImporterBlockEntity> IMPORTER_BLOCK_ENTITY_TYPE = (BlockEntityType<ImporterBlockEntity>) register("importer_tile",
            BlockEntityType.Builder.of(ImporterBlockEntity::new, Blocks.IMPORTER).build(null));
    public static BlockEntityType<ExporterBlockEntity> EXPORTER_BLOCK_ENTITY_TYPE = (BlockEntityType<ExporterBlockEntity>) register("exporter_tile",
            BlockEntityType.Builder.of(ExporterBlockEntity::new, Blocks.EXPORTER).build(null));

    private static <T extends BlockEntity> ForgeRegistryEntry<BlockEntityType<?>> register(String key, BlockEntityType<?> itemIn) {
        blockEntityList.add(itemIn);
        return itemIn.setRegistryName(MoreInventoryMOD.MOD_ID, key);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<BlockEntityType<?>> event) {
        for (BlockEntityType<?> item : blockEntityList) {
            event.getRegistry().register(item);
        }
    }
}
