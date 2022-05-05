package moreinventory.util;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public final class MIMUtils {
    public static int normalIndex(int idx, int size) {
        if (size == 0)
            return 0;
        int tmp = idx % size;
        return (tmp + size) % size;

    }

    //ItemStackHelperがByteだったため
    public static CompoundNBT writeNonNullListShort(CompoundNBT tag, NonNullList<ItemStack> list, boolean saveEmpty) {
        ListNBT listnbt = new ListNBT();

        for (int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            if (!itemstack.isEmpty()) {
                CompoundNBT compoundnbt = new CompoundNBT();
                compoundnbt.putShort("Slot", (short) i);
                itemstack.save(compoundnbt);
                listnbt.add(compoundnbt);
            }
        }

        if (!listnbt.isEmpty() || saveEmpty) {
            tag.put("Items", listnbt);
        }

        return tag;
    }

    public static void readNonNullListShort(CompoundNBT tag, NonNullList<ItemStack> list) {
        ListNBT listnbt = tag.getList("Items", 10);

        for (int i = 0; i < listnbt.size(); ++i) {
            CompoundNBT compoundnbt = listnbt.getCompound(i);
            int j = compoundnbt.getShort("Slot");
            if (j >= 0 && j < list.size()) {
                list.set(j, ItemStack.of(compoundnbt));
            }
        }
    }

    public static void setIcon(ItemStack s, byte num) {
        final String key = "CustomModelData";
        CompoundNBT tag = s.getOrCreateTag();
        tag.putByte(key, (byte) num);
        s.setTag(tag);
    }

    public static boolean intToBool(int val) {
        return val == 0 ? false : true;
    }

    public static boolean canAccessFromSide(IInventory inventory, int slot, Direction side) {
        if (inventory instanceof ISidedInventory) {
            for (int i : ((ISidedInventory) inventory).getSlotsForFace(side)) {
                if (i == slot) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean canExtractFromSide(IInventory inventory, ItemStack itemstack, int slot, Direction side) {
        return !(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canTakeItemThroughFace(slot, itemstack, side);
    }

    public static boolean canInsertFromSide(IInventory inventory, ItemStack itemstack, int slot, Direction side) {
        return inventory.canPlaceItem(slot, itemstack) && (!(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canPlaceItemThroughFace(slot, itemstack, side));
    }

    public static boolean mergeItemStack(ItemStack itemstack, IInventory inventory) {
        return mergeItemStack(itemstack, inventory, Direction.DOWN);
    }

    public static boolean mergeItemStack(ItemStack itemstack, IInventory inventory, Direction side) {
        if (itemstack.getItem() == ItemStack.EMPTY.getItem()) {
            return false;
        }

        boolean success = false;
        int size = inventory.getContainerSize();

        if (itemstack.isStackable()) {
            for (int i = 0; i < size; ++i) {
                ItemStack item = inventory.getItem(i);

                if (item != null && item.getItem() == itemstack.getItem() && itemstack.getDamageValue() == item.getDamageValue() && ItemStack.tagMatches(itemstack, item)) {
                    if (canAccessFromSide(inventory, i, side) && canInsertFromSide(inventory, itemstack, i, side)) {
                        int sum = item.getCount() + itemstack.getCount();

                        if (sum <= itemstack.getMaxStackSize()) {
                            itemstack.setCount(0);
                            item.setCount(sum);
                            inventory.setItem(i, item.copy());
                            success = true;
                        } else if (item.getCount() < itemstack.getMaxStackSize()) {
                            itemstack.shrink(itemstack.getMaxStackSize() - item.getCount());
                            item.setCount(itemstack.getMaxStackSize());
                            inventory.setItem(i, item.copy());
                            success = true;
                        }
                    }
                }

                if (itemstack.getCount() <= 0) {
                    return success;
                }
            }
        }

        if (itemstack.getCount() > 0) {
            for (int i = 0; i < size; ++i) {
                ItemStack item = inventory.getItem(i);

                if (item.getItem() == ItemStack.EMPTY.getItem() && canAccessFromSide(inventory, i, side) && canInsertFromSide(inventory, itemstack, i, side)) {
                    inventory.setItem(i, itemstack.copy());
                    itemstack.setCount(0);
                    success = true;
                    break;
                }
            }
        }

        return success;
    }

    public static void drawCenteredStringWithoutShadow(MatrixStack poseStack, FontRenderer font, ITextComponent string, float x, float y, int color) {
        font.draw(poseStack, string, x - font.width(string) / 2, y, color);
    }

    public static void drawStringWithoutShadow(MatrixStack poseStack, FontRenderer font, ITextComponent string, float x, float y, int color) {
        font.draw(poseStack, string, x, y, color);
    }

}
