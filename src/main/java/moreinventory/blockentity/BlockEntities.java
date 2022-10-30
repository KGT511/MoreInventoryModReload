package moreinventory.blockentity;

import java.util.function.Supplier;

import moreinventory.block.Blocks;
import moreinventory.blockentity.storagebox.BronzeStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.CopperStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.DiamondStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.EmeraldStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.GlassStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.GoldStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.IronStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.SilverStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.SteelStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.TinStorageBoxBlockEntity;
import moreinventory.blockentity.storagebox.WoodStorageBoxBlockEntity;
import moreinventory.core.MoreInventoryMOD;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MoreInventoryMOD.MOD_ID);

    public static final RegistryObject<BlockEntityType<CatchallBlockEntity>> CATCHALL_BLOCK_ENTITY_TYPE = register(
            "catchall_tile",
            () -> BlockEntityType.Builder.of(CatchallBlockEntity::new, Blocks.CATCHALL.get()).build(null));

    public static final RegistryObject<BlockEntityType<WoodStorageBoxBlockEntity>> WOOD_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_wood",
            () -> BlockEntityType.Builder.of(WoodStorageBoxBlockEntity::new, Blocks.WOOD_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<BlockEntityType<IronStorageBoxBlockEntity>> IRON_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_iron",
            () -> BlockEntityType.Builder.of(IronStorageBoxBlockEntity::new, Blocks.IRON_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<BlockEntityType<GoldStorageBoxBlockEntity>> GOLD_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_gold",
            () -> BlockEntityType.Builder.of(GoldStorageBoxBlockEntity::new, Blocks.GOLD_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<BlockEntityType<DiamondStorageBoxBlockEntity>> DIAMOND_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_diamond",
            () -> BlockEntityType.Builder.of(DiamondStorageBoxBlockEntity::new, Blocks.DIAMOND_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<BlockEntityType<EmeraldStorageBoxBlockEntity>> EMERALD_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_emerald",
            () -> BlockEntityType.Builder.of(EmeraldStorageBoxBlockEntity::new, Blocks.EMERALD_STORAGE_BOX.get()).build(null));

    public static final RegistryObject<BlockEntityType<CopperStorageBoxBlockEntity>> COPPER_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_copper",
            () -> BlockEntityType.Builder.of(CopperStorageBoxBlockEntity::new, Blocks.COPPER_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<BlockEntityType<TinStorageBoxBlockEntity>> TIN_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_tin",
            () -> BlockEntityType.Builder.of(TinStorageBoxBlockEntity::new, Blocks.TIN_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<BlockEntityType<BronzeStorageBoxBlockEntity>> BRONZE_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_bronze",
            () -> BlockEntityType.Builder.of(BronzeStorageBoxBlockEntity::new, Blocks.BRONZE_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<BlockEntityType<SilverStorageBoxBlockEntity>> SILVER_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_silver",
            () -> BlockEntityType.Builder.of(SilverStorageBoxBlockEntity::new, Blocks.SILVER_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<BlockEntityType<SteelStorageBoxBlockEntity>> STEEL_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_steel",
            () -> BlockEntityType.Builder.of(SteelStorageBoxBlockEntity::new, Blocks.STEEL_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<BlockEntityType<GlassStorageBoxBlockEntity>> GLASS_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_glass",
            () -> BlockEntityType.Builder.of(GlassStorageBoxBlockEntity::new, Blocks.GLASS_STORAGE_BOX.get()).build(null));

    public static final RegistryObject<BlockEntityType<? extends BaseTransportBlockEntity>> IMPORTER_BLOCK_ENTITY_TYPE = register(
            "importer_tile",
            () -> BlockEntityType.Builder.of(ImporterBlockEntity::new, Blocks.IMPORTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<? extends BaseTransportBlockEntity>> EXPORTER_BLOCK_ENTITY_TYPE = register(
            "exporter_tile",
            () -> BlockEntityType.Builder.of(ExporterBlockEntity::new, Blocks.EXPORTER.get()).build(null));

    public static <T extends BlockEntityType<?>> RegistryObject<T> register(String name, Supplier<T> blockEntity) {
        var ret = BLOCK_ENTITIES.register(name, blockEntity);
        return ret;
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
