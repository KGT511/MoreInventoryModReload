package moreinventory.container;

import java.util.function.Supplier;

import moreinventory.core.MoreInventoryMOD;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Containers {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MoreInventoryMOD.MOD_ID);

    public static final RegistryObject<MenuType<CatchallContainer>> CATCHALL_CONTAINER_TYPE = register(
            "catchall_container", () -> IForgeMenuType.create(CatchallContainer::createContainerClientSide));
    public static final RegistryObject<MenuType<TransportContainer>> TRANSPORT_CONTAINER_TYPE = register(
            "importer_container",
            () -> IForgeMenuType.create(TransportContainer::createContainerClientSide));
    public static final RegistryObject<MenuType<PouchContainer>> POUCH_CONTAINER_TYPE = register(
            "pouch_container",
            () -> IForgeMenuType.create(PouchContainer::createContainerClientSide));

    public static <T extends MenuType<?>> RegistryObject<T> register(String name, Supplier<T> sup) {
        var ret = MENU_TYPES.register(name, sup);
        return ret;
    }

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
