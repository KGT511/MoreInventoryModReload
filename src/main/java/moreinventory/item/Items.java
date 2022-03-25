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

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Items {
    public static List<Item> itemList = new ArrayList<Item>();
    public static Item CATCHALL = register("catchall", Blocks.CATCHALL, MoreInventoryMOD.itemGroup);
    public static ItemTransporter TRANSPORTER = (ItemTransporter) register("transporter", new ItemTransporter());
    public static ItemSpanner SPANNER = (ItemSpanner) register("spanner", new ItemSpanner());

    public static Item STORAGE_BOX_WOOD = register("storage_box_wood", Blocks.STORAGE_BOX_WOOD, MoreInventoryMOD.itemGroup);
    public static Item STORAGE_BOX_IRON = register("storage_box_iron", Blocks.STORAGE_BOX_IRON, MoreInventoryMOD.itemGroup);
    public static Item STORAGE_BOX_GOLD = register("storage_box_gold", Blocks.STORAGE_BOX_GOLD, MoreInventoryMOD.itemGroup);
    public static Item STORAGE_BOX_DIAMOND = register("storage_box_diamond", Blocks.STORAGE_BOX_DIAMOND, MoreInventoryMOD.itemGroup);
    public static Item STORAGE_BOX_EMERALD = register("storage_box_emerald", Blocks.STORAGE_BOX_EMERALD, MoreInventoryMOD.itemGroup);

    public static Item STORAGE_BOX_COPPER = register("storage_box_copper", Blocks.STORAGE_BOX_COPPER, MoreInventoryMOD.itemGroup);
    public static Item STORAGE_BOX_TIN = register("storage_box_tin", Blocks.STORAGE_BOX_TIN, MoreInventoryMOD.itemGroup);
    public static Item STORAGE_BOX_BRONZE = register("storage_box_bronze", Blocks.STORAGE_BOX_BRONZE, MoreInventoryMOD.itemGroup);
    public static Item STORAGE_BOX_SILVER = register("storage_box_silver", Blocks.STORAGE_BOX_SILVER, MoreInventoryMOD.itemGroup);

    public static Item STORAGE_BOX_GLASS = register("storage_box_glass", Blocks.STORAGE_BOX_GLASS, MoreInventoryMOD.itemGroup);

    public static Item IMPORTER = register("importer", Blocks.IMPORTER);
    public static Item EXPORTER = register("exporter", Blocks.EXPORTER);

    private static Item register(String key, Item itemIn) {
        itemList.add(itemIn);
        return itemIn.setRegistryName(MoreInventoryMOD.MOD_ID, key);
    }

    private static Item register(String key, Block blockIn) {
        return register(key, blockIn, MoreInventoryMOD.itemGroup);
    }

    private static Item register(String key, Block blockIn, ItemGroup itemGroupIn) {
        return register(key, new BlockItem(blockIn, (new Item.Properties()).group(itemGroupIn)));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Item item : itemList) {
            event.getRegistry().register(item);
        }
    }
}