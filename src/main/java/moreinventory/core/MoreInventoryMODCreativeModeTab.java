package moreinventory.core;

import moreinventory.block.Blocks;
import moreinventory.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MoreInventoryMODCreativeModeTab {
    public static CreativeModeTab MULTI_ENDER_CHEST_CREATIVE_TAB;

    @SubscribeEvent
    public static void registerCreaticeModeTabs(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MoreInventoryMOD.MOD_ID, "creative_tab"),
                (builder) -> {
                    MoreInventoryMODCreativeModeTab.MULTI_ENDER_CHEST_CREATIVE_TAB = builder
                            .title(Component.translatable("itemGroup." + MoreInventoryMOD.MOD_ID))
                            .icon(() -> new ItemStack(Blocks.CATCHALL.get()))
                            .displayItems((fearture, output, hasPermissions) -> {
                                for (var block : Blocks.BLOCKS.getEntries()) {
                                    output.accept(block.get());
                                }
                                for (var item : Items.ITEMS.getEntries()) {
                                    output.accept(item.get());
                                }
                            })
                            .build();
                });
    }
}
