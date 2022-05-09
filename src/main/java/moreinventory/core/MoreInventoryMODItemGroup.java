package moreinventory.core;

import moreinventory.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MoreInventoryMODItemGroup extends ItemGroup {
    public MoreInventoryMODItemGroup() {
        super(MoreInventoryMOD.MOD_ID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.WOOD_STORAGE_BOX);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillItemList(NonNullList<ItemStack> items) {
        for (Item item : Items.itemList) {
            item.fillItemCategory(this, items);
        }
    }
}
