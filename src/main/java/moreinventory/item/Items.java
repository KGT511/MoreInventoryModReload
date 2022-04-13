package moreinventory.item;

import java.util.ArrayList;
import java.util.List;

import moreinventory.block.Blocks;
import moreinventory.core.MoreInventoryMOD;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Items {
    public static List<Item> itemList = new ArrayList<Item>();

    public static Item CATCHALL = register("catchall", Blocks.CATCHALL);
    public static TransporterItem TRANSPORTER = (TransporterItem) register("transporter", new TransporterItem());
    public static SpannerItem SPANNER = (SpannerItem) register("spanner", new SpannerItem());

    public static Item WOOD_STORAGE_BOX = register("storage_box_wood", Blocks.WOOD_STORAGE_BOX);
    public static Item IRON_STORAGE_BOX = register("storage_box_iron", Blocks.IRON_STORAGE_BOX);
    public static Item GOLD_STORAGE_BOX = register("storage_box_gold", Blocks.GOLD_STORAGE_BOX);
    public static Item DIAMOND_STORAGE_BOX = register("storage_box_diamond", Blocks.DIAMOND_STORAGE_BOX);
    public static Item EMERALD_STORAGE_BOX = register("storage_box_emerald", Blocks.EMERALD_STORAGE_BOX);
    public static Item COPPER_STORAGE_BOX = register("storage_box_copper", Blocks.COPPER_STORAGE_BOX);
    public static Item TIN_STORAGE_BOX = register("storage_box_tin", Blocks.TIN_STORAGE_BOX);
    public static Item BRONZE_STORAGE_BOX = register("storage_box_bronze", Blocks.BRONZE_STORAGE_BOX);
    public static Item SILVER_STORAGE_BOX = register("storage_box_silver", Blocks.SILVER_STORAGE_BOX);

    public static Item STORAGE_BOX_GLASS = register("storage_box_glass", Blocks.GLASS_STORAGE_BOX);

    public static Item IMPORTER = register("importer", Blocks.IMPORTER);
    public static Item EXPORTER = register("exporter", Blocks.EXPORTER);

    private static Item register(String key, Item itemIn) {
        itemList.add(itemIn);
        return itemIn.setRegistryName(MoreInventoryMOD.MOD_ID, key);
    }

    private static Item register(String key, Block blockIn) {
        return register(key, blockIn, MoreInventoryMOD.creativeModeTab);
    }

    private static Item register(String key, Block blockIn, CreativeModeTab itemGroupIn) {
        return register(key, new BlockItem(blockIn, (new Item.Properties()).tab(itemGroupIn)));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Item item : itemList) {
            event.getRegistry().register(item);
        }
    }
}
