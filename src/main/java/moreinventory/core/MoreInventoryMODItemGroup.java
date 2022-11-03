package moreinventory.core;

import moreinventory.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MoreInventoryMODItemGroup extends ItemGroup {
    public MoreInventoryMODItemGroup() {
        super(MoreInventoryMOD.MOD_ID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Blocks.WOOD_STORAGE_BOX.get());
    }

}
