package moreinventory.container;

import moreinventory.core.MoreInventoryMOD;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Containers {
    public static List<ContainerType<?>> containerList = new ArrayList<>();

    public static ContainerType<CatchallContainer> CATCHALL_CONTAINER_TYPE = (ContainerType<CatchallContainer>) register("catchall_container",
            IForgeContainerType.create(CatchallContainer::createContainerClientSide));
    public static ContainerType<TransportContainer> TRANSPORT_MANAGER_CONTAINER_TYPE = (ContainerType<TransportContainer>) register("importer_container",
            IForgeContainerType.create(TransportContainer::createContainerClientSide));

    private static <T extends Container> ForgeRegistryEntry<ContainerType<?>> register(String key, ContainerType<T> itemIn) {
        containerList.add(itemIn);
        return itemIn.setRegistryName(MoreInventoryMOD.MOD_ID, key);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<ContainerType<?>> event) {
        for (ContainerType<?> item : containerList) {
            event.getRegistry().register(item);
        }
    }
}