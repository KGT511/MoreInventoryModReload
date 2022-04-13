package moreinventory.block;

import java.util.ArrayList;
import java.util.List;

import moreinventory.blockentity.storagebox.StorageBoxType;
import moreinventory.core.MoreInventoryMOD;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Blocks {
    public static List<Block> blockList = new ArrayList<Block>();

    public static Block CATCHALL = register("catchall", new CatchallBlock());

    public static Block WOOD_STORAGE_BOX = register("storage_box_wood", new StorageBoxBlock(StorageBoxType.WOOD));
    public static Block IRON_STORAGE_BOX = register("storage_box_iron", new StorageBoxBlock(StorageBoxType.IRON));
    public static Block GOLD_STORAGE_BOX = register("storage_box_gold", new StorageBoxBlock(StorageBoxType.GOLD));
    public static Block DIAMOND_STORAGE_BOX = register("storage_box_diamond", new StorageBoxBlock(StorageBoxType.DIAMOND));
    public static Block EMERALD_STORAGE_BOX = register("storage_box_emerald", new StorageBoxBlock(StorageBoxType.EMERALD));

    public static Block COPPER_STORAGE_BOX = register("storage_box_copper", new StorageBoxBlock(StorageBoxType.COPPER));
    public static Block TIN_STORAGE_BOX = register("storage_box_tin", new StorageBoxBlock(StorageBoxType.TIN));
    public static Block BRONZE_STORAGE_BOX = register("storage_box_bronze", new StorageBoxBlock(StorageBoxType.BRONZE));
    public static Block SILVER_STORAGE_BOX = register("storage_box_silver", new StorageBoxBlock(StorageBoxType.SILVER));

    public static Block GLASS_STORAGE_BOX = register("storage_box_glass", new StorageBoxBlock(StorageBoxType.GLASS));

    public static Block IMPORTER = register("importer", new TransportBlock(true));
    public static Block EXPORTER = register("exporter", new TransportBlock(false));

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
