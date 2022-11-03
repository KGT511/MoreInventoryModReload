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
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockEntities {
    public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MoreInventoryMOD.MOD_ID);

    public static final RegistryObject<TileEntityType<CatchallBlockEntity>> CATCHALL_BLOCK_ENTITY_TYPE = register(
            "catchall_tile",
            () -> TileEntityType.Builder.of(CatchallBlockEntity::new, Blocks.CATCHALL.get()).build(null));

    public static final RegistryObject<TileEntityType<WoodStorageBoxBlockEntity>> WOOD_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_wood",
            () -> TileEntityType.Builder.of(WoodStorageBoxBlockEntity::new, Blocks.WOOD_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<TileEntityType<IronStorageBoxBlockEntity>> IRON_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_iron",
            () -> TileEntityType.Builder.of(IronStorageBoxBlockEntity::new, Blocks.IRON_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<TileEntityType<GoldStorageBoxBlockEntity>> GOLD_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_gold",
            () -> TileEntityType.Builder.of(GoldStorageBoxBlockEntity::new, Blocks.GOLD_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<TileEntityType<DiamondStorageBoxBlockEntity>> DIAMOND_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_diamond",
            () -> TileEntityType.Builder.of(DiamondStorageBoxBlockEntity::new, Blocks.DIAMOND_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<TileEntityType<EmeraldStorageBoxBlockEntity>> EMERALD_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_emerald",
            () -> TileEntityType.Builder.of(EmeraldStorageBoxBlockEntity::new, Blocks.EMERALD_STORAGE_BOX.get()).build(null));

    public static final RegistryObject<TileEntityType<CopperStorageBoxBlockEntity>> COPPER_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_copper",
            () -> TileEntityType.Builder.of(CopperStorageBoxBlockEntity::new, Blocks.COPPER_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<TileEntityType<TinStorageBoxBlockEntity>> TIN_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_tin",
            () -> TileEntityType.Builder.of(TinStorageBoxBlockEntity::new, Blocks.TIN_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<TileEntityType<BronzeStorageBoxBlockEntity>> BRONZE_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_bronze",
            () -> TileEntityType.Builder.of(BronzeStorageBoxBlockEntity::new, Blocks.BRONZE_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<TileEntityType<SilverStorageBoxBlockEntity>> SILVER_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_silver",
            () -> TileEntityType.Builder.of(SilverStorageBoxBlockEntity::new, Blocks.SILVER_STORAGE_BOX.get()).build(null));
    public static final RegistryObject<TileEntityType<SteelStorageBoxBlockEntity>> STEEL_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_steel",
            () -> TileEntityType.Builder.of(SteelStorageBoxBlockEntity::new, Blocks.STEEL_STORAGE_BOX.get()).build(null));

    public static final RegistryObject<TileEntityType<GlassStorageBoxBlockEntity>> GLASS_STORAGE_BOX_BLOCK_ENTITY_TYPE = register(
            "storage_box_tile_glass",
            () -> TileEntityType.Builder.of(GlassStorageBoxBlockEntity::new, Blocks.GLASS_STORAGE_BOX.get()).build(null));

    public static final RegistryObject<TileEntityType<? extends BaseTransportBlockEntity>> IMPORTER_BLOCK_ENTITY_TYPE = register(
            "importer_tile",
            () -> TileEntityType.Builder.of(ImporterBlockEntity::new, Blocks.IMPORTER.get()).build(null));
    public static final RegistryObject<TileEntityType<? extends BaseTransportBlockEntity>> EXPORTER_BLOCK_ENTITY_TYPE = register(
            "exporter_tile",
            () -> TileEntityType.Builder.of(ExporterBlockEntity::new, Blocks.EXPORTER.get()).build(null));

    public static <T extends TileEntityType<?>> RegistryObject<T> register(String name, Supplier<T> blockEntity) {
        RegistryObject<T> ret = BLOCK_ENTITIES.register(name, blockEntity);
        return ret;
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
