package moreinventory.tileentity;

import java.util.ArrayList;
import java.util.List;

import moreinventory.block.Blocks;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.tileentity.storagebox.BronzeStorageBoxTileEntity;
import moreinventory.tileentity.storagebox.CopperStorageBoxTileEntity;
import moreinventory.tileentity.storagebox.DiamondStorageBoxTileEntity;
import moreinventory.tileentity.storagebox.EmeraldStorageBoxTileEntity;
import moreinventory.tileentity.storagebox.GlassStorageBoxTileEntity;
import moreinventory.tileentity.storagebox.GoldStorageBoxTileEntity;
import moreinventory.tileentity.storagebox.IronStorageBoxTileEntity;
import moreinventory.tileentity.storagebox.SilverStorageBoxTileEntity;
import moreinventory.tileentity.storagebox.TinStorageBoxTileEntity;
import moreinventory.tileentity.storagebox.WoodStorageBoxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TileEntities {
    public static List<TileEntityType<?>> tileList = new ArrayList<>();

    public static TileEntityType<CatchallTileEntity> CATCHALL_TILE_TYPE = (TileEntityType<CatchallTileEntity>) register("catchall_tile",
            TileEntityType.Builder.of(CatchallTileEntity::new, Blocks.CATCHALL).build(null));

    public static TileEntityType<BaseStorageBoxTileEntity> STORAGE_BOX_WOOD_TILE_TYPE = (TileEntityType<BaseStorageBoxTileEntity>) register("storage_box_tile_wood",
            TileEntityType.Builder.of(WoodStorageBoxTileEntity::new, Blocks.WOOD_STORAGE_BOX).build(null));
    public static TileEntityType<BaseStorageBoxTileEntity> STORAGE_BOX_IRON_TILE_TYPE = (TileEntityType<BaseStorageBoxTileEntity>) register("storage_box_tile_iron",
            TileEntityType.Builder.of(IronStorageBoxTileEntity::new, Blocks.IRON_STORAGE_BOX).build(null));
    public static TileEntityType<BaseStorageBoxTileEntity> STORAGE_BOX_GOLD_TILE_TYPE = (TileEntityType<BaseStorageBoxTileEntity>) register("storage_box_tile_gold",
            TileEntityType.Builder.of(GoldStorageBoxTileEntity::new, Blocks.GOLD_STORAGE_BOX).build(null));
    public static TileEntityType<BaseStorageBoxTileEntity> STORAGE_BOX_DIAMOND_TILE_TYPE = (TileEntityType<BaseStorageBoxTileEntity>) register("storage_box_tile_diamond",
            TileEntityType.Builder.of(DiamondStorageBoxTileEntity::new, Blocks.DIAMOND_STORAGE_BOX).build(null));
    public static TileEntityType<BaseStorageBoxTileEntity> STORAGE_BOX_EMERALD_TILE_TYPE = (TileEntityType<BaseStorageBoxTileEntity>) register("storage_box_tile_emerald",
            TileEntityType.Builder.of(EmeraldStorageBoxTileEntity::new, Blocks.EMERALD_STORAGE_BOX).build(null));

    public static TileEntityType<BaseStorageBoxTileEntity> STORAGE_BOX_COPPER_TILE_TYPE = (TileEntityType<BaseStorageBoxTileEntity>) register("storage_box_tile_copper",
            TileEntityType.Builder.of(CopperStorageBoxTileEntity::new, Blocks.COPPER_STORAGE_BOX).build(null));
    public static TileEntityType<BaseStorageBoxTileEntity> STORAGE_BOX_TIN_TILE_TYPE = (TileEntityType<BaseStorageBoxTileEntity>) register("storage_box_tile_tin",
            TileEntityType.Builder.of(TinStorageBoxTileEntity::new, Blocks.TIN_STORAGE_BOX).build(null));
    public static TileEntityType<BaseStorageBoxTileEntity> STORAGE_BOX_BRONZE_TILE_TYPE = (TileEntityType<BaseStorageBoxTileEntity>) register("storage_box_tile_bronze",
            TileEntityType.Builder.of(BronzeStorageBoxTileEntity::new, Blocks.BRONZE_STORAGE_BOX).build(null));
    public static TileEntityType<BaseStorageBoxTileEntity> STORAGE_BOX_SILVER_TILE_TYPE = (TileEntityType<BaseStorageBoxTileEntity>) register("storage_box_tile_silver",
            TileEntityType.Builder.of(SilverStorageBoxTileEntity::new, Blocks.SILVER_STORAGE_BOX).build(null));

    public static TileEntityType<BaseStorageBoxTileEntity> STORAGE_BOX_GLASS_TILE_TYPE = (TileEntityType<BaseStorageBoxTileEntity>) register("storage_box_tile_glass",
            TileEntityType.Builder.of(GlassStorageBoxTileEntity::new, Blocks.GLASS_STORAGE_BOX).build(null));

    public static TileEntityType<ImporterTileEntity> IMPORTER_TILE_TYPE = (TileEntityType<ImporterTileEntity>) register("importer_tile",
            TileEntityType.Builder.of(ImporterTileEntity::new, Blocks.IMPORTER).build(null));
    public static TileEntityType<ExporterTileEntity> EXPORTER_TILE_TYPE = (TileEntityType<ExporterTileEntity>) register("exporter_tile",
            TileEntityType.Builder.of(ExporterTileEntity::new, Blocks.EXPORTER).build(null));

    private static <T extends TileEntity> ForgeRegistryEntry<TileEntityType<?>> register(String key, TileEntityType<?> itemIn) {
        tileList.add(itemIn);
        return itemIn.setRegistryName(MoreInventoryMOD.MODID, key);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<TileEntityType<?>> event) {
        for (TileEntityType<?> item : tileList) {
            event.getRegistry().register(item);
        }
    }
}