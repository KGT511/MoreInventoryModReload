package moreinventory.recipe;

import moreinventory.inventory.PouchInventory;
import moreinventory.item.PouchItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class PouchRecipe extends SpecialRecipe {

    public PouchRecipe(ResourceLocation p_i48169_1_) {
        super(p_i48169_1_);
    }

    //染料と色変え可能なアイテムが1つずつあるかチェック
    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        int pouchCnt = 0;
        int dyeCnt = 0;
        int enderPearlCnt = 0;
        ItemStack pouch = ItemStack.EMPTY;

        for (int k = 0; k < inventory.getContainerSize(); ++k) {
            ItemStack itemStack = inventory.getItem(k);
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() instanceof PouchItem) {
                    pouch = itemStack;
                    ++pouchCnt;
                } else if (itemStack.getItem().is(net.minecraftforge.common.Tags.Items.DYES)) {
                    ++dyeCnt;
                } else if (itemStack.getItem() == Items.ENDER_PEARL) {
                    ++enderPearlCnt;
                } else {
                    return false;
                }
            }
        }
        if (0 < enderPearlCnt) {
            int grade = new PouchInventory(pouch).getGrade();
            if (PouchInventory.maxUpgradeNumCollectableSlot < grade + enderPearlCnt)
                return false;
        }

        return pouchCnt == 1 && ((dyeCnt == 1) || (1 <= enderPearlCnt));
    }

    //完成品を返す
    @Override
    public ItemStack assemble(CraftingInventory inventory) {
        ItemStack pouch = ItemStack.EMPTY;
        DyeColor dyeColor = null;
        int gradeUpCnt = 0;

        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack itemStack = inventory.getItem(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() instanceof PouchItem) {
                    pouch = itemStack.copy();
                } else if (itemStack.getItem() instanceof DyeItem) {
                    DyeColor tmp = DyeColor.getColor(itemStack);
                    if (tmp != null)
                        dyeColor = tmp;
                } else if (itemStack.getItem() == Items.ENDER_PEARL) {
                    ++gradeUpCnt;
                }
            }
        }
        if (dyeColor != null)
            pouch = PouchItem.setColor(pouch, dyeColor);
        if (0 < gradeUpCnt) {
            PouchInventory pouchInventory = new PouchInventory(pouch);
            for (int i = 0; i < gradeUpCnt; ++i)
                pouchInventory.increaseGrade();
        }

        return pouch;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return Recipes.POUCH_RECIPE.get();
    }

}
