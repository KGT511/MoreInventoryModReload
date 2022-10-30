package moreinventory.block;

import java.util.function.Supplier;

import moreinventory.core.MoreInventoryMOD;
import moreinventory.item.Items;
import moreinventory.storagebox.StorageBoxType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Blocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MoreInventoryMOD.MOD_ID);

    public static final RegistryObject<Block> CATCHALL = register("catchall", () -> new CatchallBlock());

    public static final RegistryObject<Block> WOOD_STORAGE_BOX = register("storage_box_wood", () -> new StorageBoxBlock(StorageBoxType.WOOD));
    public static final RegistryObject<Block> IRON_STORAGE_BOX = register("storage_box_iron", () -> new StorageBoxBlock(StorageBoxType.IRON));
    public static final RegistryObject<Block> GOLD_STORAGE_BOX = register("storage_box_gold", () -> new StorageBoxBlock(StorageBoxType.GOLD));
    public static final RegistryObject<Block> DIAMOND_STORAGE_BOX = register("storage_box_diamond", () -> new StorageBoxBlock(StorageBoxType.DIAMOND));

    public static final RegistryObject<Block> EMERALD_STORAGE_BOX = register("storage_box_emerald", () -> new StorageBoxBlock(StorageBoxType.EMERALD));
    public static final RegistryObject<Block> COPPER_STORAGE_BOX = register("storage_box_copper", () -> new StorageBoxBlock(StorageBoxType.COPPER));
    public static final RegistryObject<Block> TIN_STORAGE_BOX = register("storage_box_tin", () -> new StorageBoxBlock(StorageBoxType.TIN));
    public static final RegistryObject<Block> BRONZE_STORAGE_BOX = register("storage_box_bronze", () -> new StorageBoxBlock(StorageBoxType.BRONZE));
    public static final RegistryObject<Block> SILVER_STORAGE_BOX = register("storage_box_silver", () -> new StorageBoxBlock(StorageBoxType.SILVER));
    public static final RegistryObject<Block> STEEL_STORAGE_BOX = register("storage_box_steel", () -> new StorageBoxBlock(StorageBoxType.STEEL));

    public static final RegistryObject<Block> GLASS_STORAGE_BOX = register("storage_box_glass", () -> new StorageBoxBlock(StorageBoxType.GLASS));

    public static final RegistryObject<Block> IMPORTER = register("importer", () -> new TransportBlock(true));
    public static final RegistryObject<Block> EXPORTER = register("exporter", () -> new TransportBlock(false));

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        var ret = BLOCKS.register(name, block);
        registerBlockItem(name, ret, MoreInventoryMOD.creativeModeTab);
        return ret;
    }

    private static <T extends Block> RegistryObject<BlockItem> registerBlockItem(String name, Supplier<T> block, CreativeModeTab tab) {
        var ret = Items.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
        return ret;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}