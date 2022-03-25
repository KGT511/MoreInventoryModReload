package moreinventory.core;

import moreinventory.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemGroupMoreInventoryMOD extends ItemGroup {
    public ItemGroupMoreInventoryMOD() {
        super(MoreInventoryMOD.MOD_ID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.STORAGE_BOX_WOOD);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fill(NonNullList<ItemStack> items) {
        for (Item item : Items.itemList) {
            item.fillItemGroup(this, items);
        }
    }
}
