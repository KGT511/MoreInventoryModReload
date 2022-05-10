package moreinventory.container;

import java.util.ArrayList;
import java.util.List;

import moreinventory.core.MoreInventoryMOD;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Containers {
    public static List<MenuType<?>> containerList = new ArrayList<>();

    public static MenuType<CatchallContainer> CATCHALL_CONTAINER_TYPE = (MenuType<CatchallContainer>) register(
            "catchall_container", IForgeContainerType.create(CatchallContainer::createContainerClientSide));
    public static MenuType<TransportContainer> TRANSPORT_CONTAINER_TYPE = (MenuType<TransportContainer>) register("importer_container",
            IForgeContainerType.create(TransportContainer::createContainerClientSide));
    public static final MenuType<PouchContainer> POUCH_CONTAINER_TYPE = (MenuType<PouchContainer>) register("pouch_container",
            IForgeContainerType.create(PouchContainer::createContainerClientSide));

    private static <T extends AbstractContainerMenu> ForgeRegistryEntry<MenuType<?>> register(String key, MenuType<T> itemIn) {
        containerList.add(itemIn);
        return itemIn.setRegistryName(MoreInventoryMOD.MOD_ID, key);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<MenuType<?>> event) {
        for (var item : containerList) {
            event.getRegistry().register(item);
        }
    }
}
