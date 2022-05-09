package moreinventory.block;

import java.util.ArrayList;
import java.util.List;

import moreinventory.core.MoreInventoryMOD;
import moreinventory.tileentity.storagebox.StorageBoxType;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Blocks {
    public static final List<Block> blockList = new ArrayList<Block>();

    public static final Block CATCHALL = register("catchall", new CatchallBlock());

    public static final Block WOOD_STORAGE_BOX = register("storage_box_wood", new StorageBoxBlock(StorageBoxType.WOOD));
    public static final Block IRON_STORAGE_BOX = register("storage_box_iron", new StorageBoxBlock(StorageBoxType.IRON));
    public static final Block GOLD_STORAGE_BOX = register("storage_box_gold", new StorageBoxBlock(StorageBoxType.GOLD));
    public static final Block DIAMOND_STORAGE_BOX = register("storage_box_diamond", new StorageBoxBlock(StorageBoxType.DIAMOND));
    public static final Block EMERALD_STORAGE_BOX = register("storage_box_emerald", new StorageBoxBlock(StorageBoxType.EMERALD));

    public static final Block COPPER_STORAGE_BOX = register("storage_box_copper", new StorageBoxBlock(StorageBoxType.COPPER));
    public static final Block TIN_STORAGE_BOX = register("storage_box_tin", new StorageBoxBlock(StorageBoxType.TIN));
    public static final Block BRONZE_STORAGE_BOX = register("storage_box_bronze", new StorageBoxBlock(StorageBoxType.BRONZE));
    public static final Block SILVER_STORAGE_BOX = register("storage_box_silver", new StorageBoxBlock(StorageBoxType.SILVER));

    public static final Block GLASS_STORAGE_BOX = register("storage_box_glass", new StorageBoxBlock(StorageBoxType.GLASS));

    public static final Block IMPORTER = register("importer", new TransportBlock(true));
    public static final Block EXPORTER = register("exporter", new TransportBlock(false));

    private static Block register(String key, Block blockIn) {
        blockList.add(blockIn);
        return blockIn.setRegistryName(MoreInventoryMOD.MOD_ID, key);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (Block block : blockList) {
            event.getRegistry().register(block);
        }
    }
}
