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
    public static List<Block> blockList = new ArrayList<Block>();

    public static Block CATCHALL = register("catchall", new BlockCatchall());

    public static Block STORAGE_BOX_WOOD = register("storage_box_wood", new BlockStorageBox(StorageBoxType.WOOD));
    public static Block STORAGE_BOX_IRON = register("storage_box_iron", new BlockStorageBox(StorageBoxType.IRON));
    public static Block STORAGE_BOX_GOLD = register("storage_box_gold", new BlockStorageBox(StorageBoxType.GOLD));
    public static Block STORAGE_BOX_DIAMOND = register("storage_box_diamond", new BlockStorageBox(StorageBoxType.DIAMOND));
    public static Block STORAGE_BOX_EMERALD = register("storage_box_emerald", new BlockStorageBox(StorageBoxType.EMERALD));

    public static Block STORAGE_BOX_COPPER = register("storage_box_copper", new BlockStorageBox(StorageBoxType.COPPER));
    public static Block STORAGE_BOX_TIN = register("storage_box_tin", new BlockStorageBox(StorageBoxType.TIN));
    public static Block STORAGE_BOX_BRONZE = register("storage_box_bronze", new BlockStorageBox(StorageBoxType.BRONZE));
    public static Block STORAGE_BOX_SILVER = register("storage_box_silver", new BlockStorageBox(StorageBoxType.SILVER));

    public static Block STORAGE_BOX_GLASS = register("storage_box_glass", new BlockStorageBox(StorageBoxType.GLASS));

    public static Block IMPORTER = register("importer", new BlockTransportManager(true));
    public static Block EXPORTER = register("exporter", new BlockTransportManager(false));

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
