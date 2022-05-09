package moreinventory.container;

import java.util.ArrayList;
import java.util.List;

import moreinventory.core.MoreInventoryMOD;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Containers {
    public static final List<ContainerType<?>> containerList = new ArrayList<>();

    public static final ContainerType<CatchallContainer> CATCHALL_CONTAINER_TYPE = (ContainerType<CatchallContainer>) register("catchall_container",
            IForgeContainerType.create(CatchallContainer::createContainerClientSide));
    public static final ContainerType<TransportContainer> TRANSPORT_MANAGER_CONTAINER_TYPE = (ContainerType<TransportContainer>) register("importer_container",
            IForgeContainerType.create(TransportContainer::createContainerClientSide));
    public static final ContainerType<PouchContainer> POUCH_CONTAINER_TYPE = (ContainerType<PouchContainer>) register("pouch_container",
            IForgeContainerType.create(PouchContainer::createContainerClientSide));

    private static <T extends Container> ForgeRegistryEntry<ContainerType<?>> register(String key, ContainerType<T> itemIn) {
        containerList.add(itemIn);
        return itemIn.setRegistryName(MoreInventoryMOD.MODID, key);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<ContainerType<?>> event) {
        for (ContainerType<?> item : containerList) {
            event.getRegistry().register(item);
        }
    }
}