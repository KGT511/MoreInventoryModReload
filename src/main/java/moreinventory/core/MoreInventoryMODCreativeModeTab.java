package moreinventory.core;

import moreinventory.block.Blocks;
import moreinventory.item.Items;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MoreInventoryMODCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> MIM_CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoreInventoryMOD.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MULTI_ENDER_CHEST_CREATIVE_TAB = MIM_CREATIVE_TABS.register("", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MoreInventoryMOD.MOD_ID))
            .icon(() -> new ItemStack(Blocks.CATCHALL.get()))
            .displayItems((fearture, output) -> {
                for (var block : Blocks.BLOCKS.getEntries()) {
                    output.accept(block.get());
                }
                for (var item : Items.ITEMS.getEntries()) {
                    output.accept(item.get());
                }
            })
            .build());

    public static void register(IEventBus eventBus) {
        MIM_CREATIVE_TABS.register(eventBus);
    }
}
