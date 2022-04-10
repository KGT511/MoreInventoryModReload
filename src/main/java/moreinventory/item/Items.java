package moreinventory.item;

import java.util.ArrayList;
import java.util.List;

import moreinventory.block.Blocks;
import moreinventory.core.MoreInventoryMOD;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

    public static Item GLASS_STORAGE_BOX = register("storage_box_glass", Blocks.GLASS_STORAGE_BOX);

    public static Item IMPORTER = register("importer", Blocks.IMPORTER);
    public static Item EXPORTER = register("exporter", Blocks.EXPORTER);

    private static Item register(String key, Item itemIn) {
        itemList.add(itemIn);
        return itemIn.setRegistryName(MoreInventoryMOD.MODID, key);
    }

    private static Item register(String key, Block blockIn) {
        return register(key, blockIn, MoreInventoryMOD.itemGroup);
    }

    private static Item register(String key, Block blockIn, ItemGroup itemGroupIn) {
        return register(key, new BlockItem(blockIn, (new Item.Properties()).tab(itemGroupIn)));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Item item : itemList) {
            event.getRegistry().register(item);
        }
    }
}