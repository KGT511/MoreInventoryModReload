package moreinventory.item;

import java.util.function.Supplier;

import moreinventory.core.MoreInventoryMOD;
import moreinventory.storagebox.StorageBoxType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            MoreInventoryMOD.MOD_ID);

    public static final RegistryObject<TransporterItem> TRANSPORTER = register("transporter", () -> new TransporterItem());
    public static final RegistryObject<SpannerItem> SPANNER = register("spanner", () -> new SpannerItem());
    public static final RegistryObject<PouchItem> POUCH = register("pouch", () -> new PouchItem(null));
    public static final RegistryObject<PouchItem> POUCH_WHITE = register("pouch_white", () -> new PouchItem(DyeColor.WHITE));
    public static final RegistryObject<PouchItem> POUCH_ORANGE = register("pouch_orange", () -> new PouchItem(DyeColor.ORANGE));
    public static final RegistryObject<PouchItem> POUCH_MAGENTA = register("pouch_magenta", () -> new PouchItem(DyeColor.MAGENTA));
    public static final RegistryObject<PouchItem> POUCH_LIGHT_BLUE = register("pouch_light_blue", () -> new PouchItem(DyeColor.LIGHT_BLUE));
    public static final RegistryObject<PouchItem> POUCH_YELLOW = register("pouch_yellow", () -> new PouchItem(DyeColor.YELLOW));
    public static final RegistryObject<PouchItem> POUCH_LIME = register("pouch_lime", () -> new PouchItem(DyeColor.LIME));
    public static final RegistryObject<PouchItem> POUCH_PINK = register("pouch_pink", () -> new PouchItem(DyeColor.PINK));
    public static final RegistryObject<PouchItem> POUCH_GRAY = register("pouch_gray", () -> new PouchItem(DyeColor.GRAY));
    public static final RegistryObject<PouchItem> POUCH_LIGHT_GRAY = register("pouch_light_gray", () -> new PouchItem(DyeColor.LIGHT_GRAY));
    public static final RegistryObject<PouchItem> POUCH_CYAN = register("pouch_cyan", () -> new PouchItem(DyeColor.CYAN));
    public static final RegistryObject<PouchItem> POUCH_PURPLE = register("pouch_purple", () -> new PouchItem(DyeColor.PURPLE));
    public static final RegistryObject<PouchItem> POUCH_BLUE = register("pouch_blue", () -> new PouchItem(DyeColor.BLUE));
    public static final RegistryObject<PouchItem> POUCH_BROWN = register("pouch_brown", () -> new PouchItem(DyeColor.BROWN));
    public static final RegistryObject<PouchItem> POUCH_GREEN = register("pouch_green", () -> new PouchItem(DyeColor.GREEN));
    public static final RegistryObject<PouchItem> POUCH_RED = register("pouch_red", () -> new PouchItem(DyeColor.RED));
    public static final RegistryObject<PouchItem> POUCH_BLACK = register("pouch_black", () -> new PouchItem(DyeColor.BLACK));
    public static final RegistryObject<Item> LEATHER_PACK = register("leather_pack", () -> new Item(new Item.Properties().tab(MoreInventoryMOD.itemGroup)));

    public static final RegistryObject<Item> BRUSH = register("brush", () -> new Item(new Item.Properties().tab(MoreInventoryMOD.itemGroup)));
    public static final RegistryObject<Item> IRON_PLATING = register("plating_iron", () -> new PlatingItem(StorageBoxType.IRON));
    public static final RegistryObject<Item> GOLD_PLATING = register("plating_gold", () -> new PlatingItem(StorageBoxType.GOLD));
    public static final RegistryObject<Item> DIAMOND_PLATING = register("plating_diamond", () -> new PlatingItem(StorageBoxType.DIAMOND));
    public static final RegistryObject<Item> EMERALD_PLATING = register("plating_emerald", () -> new PlatingItem(StorageBoxType.EMERALD));

    public static final RegistryObject<Item> COPPER_PLATING = register("plating_copper", () -> new PlatingItem(StorageBoxType.COPPER));
    public static final RegistryObject<Item> TIN_PLATING = register("plating_tin", () -> new PlatingItem(StorageBoxType.TIN));
    public static final RegistryObject<Item> BRONZE_PLATING = register("plating_bronze", () -> new PlatingItem(StorageBoxType.BRONZE));
    public static final RegistryObject<Item> SILVER_PLATING = register("plating_silver", () -> new PlatingItem(StorageBoxType.SILVER));
    public static final RegistryObject<Item> STEEL_PLATING = register("plating_steel", () -> new PlatingItem(StorageBoxType.STEEL));

    public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {
        RegistryObject<T> ret = ITEMS.register(name, item);
        return ret;
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}