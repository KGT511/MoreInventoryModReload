package moreinventory.tileentity;

import java.util.ArrayList;
import java.util.List;

import moreinventory.block.Blocks;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.tileentity.storagebox.TileEntityStorageBoxBronze;
import moreinventory.tileentity.storagebox.TileEntityStorageBoxCopper;
import moreinventory.tileentity.storagebox.TileEntityStorageBoxDiamond;
import moreinventory.tileentity.storagebox.TileEntityStorageBoxEmerald;
import moreinventory.tileentity.storagebox.TileEntityStorageBoxGlass;
import moreinventory.tileentity.storagebox.TileEntityStorageBoxGold;
import moreinventory.tileentity.storagebox.TileEntityStorageBoxIron;
import moreinventory.tileentity.storagebox.TileEntityStorageBoxSilver;
import moreinventory.tileentity.storagebox.TileEntityStorageBoxTin;
import moreinventory.tileentity.storagebox.TileEntityStorageBoxWood;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TileEntities {
    public static List<TileEntityType<?>> tileList = new ArrayList<>();

    public static TileEntityType<TileEntityCatchall> CATCHALL_TILE_TYPE = (TileEntityType<TileEntityCatchall>) register("catchall_tile",
            TileEntityType.Builder.create(TileEntityCatchall::new, Blocks.CATCHALL).build(null));

    public static TileEntityType<BaseTileEntityStorageBox> STORAGE_BOX_WOOD_TILE_TYPE = (TileEntityType<BaseTileEntityStorageBox>) register("storage_box_tile_wood",
            TileEntityType.Builder.create(TileEntityStorageBoxWood::new, Blocks.STORAGE_BOX_WOOD).build(null));
    public static TileEntityType<BaseTileEntityStorageBox> STORAGE_BOX_IRON_TILE_TYPE = (TileEntityType<BaseTileEntityStorageBox>) register("storage_box_tile_iron",
            TileEntityType.Builder.create(TileEntityStorageBoxIron::new, Blocks.STORAGE_BOX_IRON).build(null));
    public static TileEntityType<BaseTileEntityStorageBox> STORAGE_BOX_GOLD_TILE_TYPE = (TileEntityType<BaseTileEntityStorageBox>) register("storage_box_tile_gold",
            TileEntityType.Builder.create(TileEntityStorageBoxGold::new, Blocks.STORAGE_BOX_GOLD).build(null));
    public static TileEntityType<BaseTileEntityStorageBox> STORAGE_BOX_DIAMOND_TILE_TYPE = (TileEntityType<BaseTileEntityStorageBox>) register("storage_box_tile_diamond",
            TileEntityType.Builder.create(TileEntityStorageBoxDiamond::new, Blocks.STORAGE_BOX_DIAMOND).build(null));
    public static TileEntityType<BaseTileEntityStorageBox> STORAGE_BOX_EMERALD_TILE_TYPE = (TileEntityType<BaseTileEntityStorageBox>) register("storage_box_tile_emerald",
            TileEntityType.Builder.create(TileEntityStorageBoxEmerald::new, Blocks.STORAGE_BOX_EMERALD).build(null));

    public static TileEntityType<BaseTileEntityStorageBox> STORAGE_BOX_COPPER_TILE_TYPE = (TileEntityType<BaseTileEntityStorageBox>) register("storage_box_tile_copper",
            TileEntityType.Builder.create(TileEntityStorageBoxCopper::new, Blocks.STORAGE_BOX_COPPER).build(null));
    public static TileEntityType<BaseTileEntityStorageBox> STORAGE_BOX_TIN_TILE_TYPE = (TileEntityType<BaseTileEntityStorageBox>) register("storage_box_tile_tin",
            TileEntityType.Builder.create(TileEntityStorageBoxTin::new, Blocks.STORAGE_BOX_TIN).build(null));
    public static TileEntityType<BaseTileEntityStorageBox> STORAGE_BOX_BRONZE_TILE_TYPE = (TileEntityType<BaseTileEntityStorageBox>) register("storage_box_tile_bronze",
            TileEntityType.Builder.create(TileEntityStorageBoxBronze::new, Blocks.STORAGE_BOX_BRONZE).build(null));
    public static TileEntityType<BaseTileEntityStorageBox> STORAGE_BOX_SILVER_TILE_TYPE = (TileEntityType<BaseTileEntityStorageBox>) register("storage_box_tile_silver",
            TileEntityType.Builder.create(TileEntityStorageBoxSilver::new, Blocks.STORAGE_BOX_SILVER).build(null));

    public static TileEntityType<BaseTileEntityStorageBox> STORAGE_BOX_GLASS_TILE_TYPE = (TileEntityType<BaseTileEntityStorageBox>) register("storage_box_tile_glass",
            TileEntityType.Builder.create(TileEntityStorageBoxGlass::new, Blocks.STORAGE_BOX_GLASS).build(null));

    public static TileEntityType<TileEntityImporter> IMPORTER_TILE_TYPE = (TileEntityType<TileEntityImporter>) register("importer_tile",
            TileEntityType.Builder.create(TileEntityImporter::new, Blocks.IMPORTER).build(null));
    public static TileEntityType<TileEntityExporter> EXPORTER_TILE_TYPE = (TileEntityType<TileEntityExporter>) register("exporter_tile",
            TileEntityType.Builder.create(TileEntityExporter::new, Blocks.EXPORTER).build(null));

    private static <T extends TileEntity> ForgeRegistryEntry<TileEntityType<?>> register(String key, TileEntityType<?> itemIn) {
        tileList.add(itemIn);
        return itemIn.setRegistryName(MoreInventoryMOD.MOD_ID, key);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<TileEntityType<?>> event) {
        for (TileEntityType<?> item : tileList) {
            event.getRegistry().register(item);
        }
    }
}