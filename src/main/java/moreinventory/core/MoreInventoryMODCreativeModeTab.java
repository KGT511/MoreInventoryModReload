package moreinventory.core;

import moreinventory.block.Blocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class MoreInventoryMODCreativeModeTab extends CreativeModeTab {
    public MoreInventoryMODCreativeModeTab() {
        super(MoreInventoryMOD.MOD_ID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Blocks.CATCHALL.get());
    }

}
