package moreinventory.recipe;

import moreinventory.item.PouchItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class PouchColoringRecipe extends SpecialRecipe {

    public PouchColoringRecipe(ResourceLocation p_i48169_1_) {
        super(p_i48169_1_);
    }

    //染料と色変え可能なアイテムが1つずつあるかチェック
    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        int pouchCnt = 0;
        int dyeCnt = 0;

        for (int k = 0; k < inventory.getContainerSize(); ++k) {
            ItemStack itemStack = inventory.getItem(k);
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() instanceof PouchItem) {
                    ++pouchCnt;
                } else {
                    if (!itemStack.getItem().is(net.minecraftforge.common.Tags.Items.DYES)) {
                        return false;
                    }

                    ++dyeCnt;
                }

                if (dyeCnt > 1 || pouchCnt > 1) {
                    return false;
                }
            }
        }

        return pouchCnt == 1 && dyeCnt == 1;
    }

    //完成品を返す
    @Override
    public ItemStack assemble(CraftingInventory inventory) {
        ItemStack pouch = ItemStack.EMPTY;
        DyeColor dyeColor = null;
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack itemStack = inventory.getItem(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() instanceof PouchItem) {
                    pouch = itemStack.copy();
                } else {
                    DyeColor tmp = DyeColor.getColor(itemStack);
                    if (tmp != null)
                        dyeColor = tmp;
                }
            }
        }

        pouch = PouchItem.setColor(pouch, dyeColor);

        return pouch;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return Recipes.POUCH_COLORING.get();
    }

}
