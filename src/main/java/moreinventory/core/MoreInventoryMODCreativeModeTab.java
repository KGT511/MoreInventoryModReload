package moreinventory.core;

import moreinventory.item.Items;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MoreInventoryMODCreativeModeTab extends CreativeModeTab {
    public MoreInventoryMODCreativeModeTab() {
        super(MoreInventoryMOD.MOD_ID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.CATCHALL);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillItemList(NonNullList<ItemStack> items) {
        for (var item : Items.itemList) {
            item.fillItemCategory(this, items);
        }
    }
}
