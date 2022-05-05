package moreinventory.inventory;

import java.util.List;

import moreinventory.item.Items;
import moreinventory.util.MIMUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class PouchInventory implements IInventory {

    public static final int slotSize = 9 * 6;
    public static final int collectableSlotSize = 18;
    private NonNullList<ItemStack> slotItems = NonNullList.withSize(slotSize + collectableSlotSize, ItemStack.EMPTY);

    private ItemStack usingPouch;

    public enum Val {
        STORAGE_BOX, HOT_BAR, AUTO_COLLECT
    };

    private boolean isStorageBox = true;
    private boolean isHotBar = true;
    private boolean isAutoCollect = true;
    private static final String isStorageBoxTagKey = "isStorageBox";
    private static final String isHotBarTagKey = "isHotBar";
    private static final String isAutoCollectTagKey = "isAutoCollect";

    private int grade;
    private static final String gradeTagKey = "grade";

    public ITextComponent customName;

    public PouchInventory(PlayerEntity player, ItemStack itemStack) {
        this.usingPouch = itemStack;
        this.customName = itemStack.getDisplayName();
        this.readToNBT(this.usingPouch.getOrCreateTag());
    }

    public PouchInventory(ItemStack itemStack) {
        this(null, itemStack);
    }

    public void readToNBT(CompoundNBT nbt) {
        if (this.usingPouch != null) {
            if (nbt == null) {
                return;
            }
            this.slotItems = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(nbt, this.slotItems);
            this.isStorageBox = (nbt.contains(isStorageBoxTagKey) ? nbt.getBoolean(isStorageBoxTagKey) : this.isStorageBox);
            this.isHotBar = (nbt.contains(isHotBarTagKey) ? nbt.getBoolean(isHotBarTagKey) : this.isHotBar);
            this.isAutoCollect = (nbt.contains(isAutoCollectTagKey) ? nbt.getBoolean(isAutoCollectTagKey) : this.isAutoCollect);
            this.grade = nbt.getInt(gradeTagKey);
        }
    }

    public void writeToNBT(CompoundNBT nbt) {
        if (this.usingPouch != null) {
            ItemStackHelper.saveAllItems(nbt, this.slotItems);
            nbt.putBoolean(isStorageBoxTagKey, this.isStorageBox);
            nbt.putBoolean(isHotBarTagKey, this.isHotBar);
            nbt.putBoolean(isAutoCollectTagKey, this.isAutoCollect);
            nbt.putInt(gradeTagKey, this.grade);
        }
    }

    @Override
    public void clearContent() {
        this.slotItems.clear();
    }

    @Override
    public int getContainerSize() {
        return slotSize + collectableSlotSize;
    }

    @Override
    public boolean isEmpty() {
        return this.slotItems.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int index) {
        return slotItems.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int num) {
        return ItemStackHelper.removeItem(this.slotItems, index, num);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStackHelper.takeItem(this.slotItems, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        slotItems.set(index, stack);
        this.setChanged();
    }

    @Override
    public void startOpen(PlayerEntity player) {
        this.readToNBT(this.usingPouch.getOrCreateTag());
    }

    @Override
    public void stopOpen(PlayerEntity player) {
        this.writeToNBT(this.usingPouch.getOrCreateTag());
    }

    @Override
    public void setChanged() {
        this.writeToNBT(this.usingPouch.getOrCreateTag());
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.usingPouch.sameItem(player.getItemInHand(player.getUsedItemHand()));
    }

    public int getGrade() {
        return this.grade;
    }

    public List<ItemStack> getInventorySlotItems() {
        return this.slotItems.subList(0, slotSize);
    }

    public List<ItemStack> getCollectableSlotItems() {
        return this.slotItems.subList(slotSize, this.getContainerSize());
    }

    public boolean isCollectableItem(ItemStack itemstack) {
        for (ItemStack colletctableItemStack : this.getCollectableSlotItems())
            if (itemstack.sameItem(colletctableItemStack))
                return true;

        return false;
    }

    public void collectAllItemStack(IInventory inventory, boolean flag) {
        ItemStack itemStack;
        int origin = (isHotBar ? 0 : 9);

        for (int i = origin; i < inventory.getContainerSize(); i++) {
            itemStack = inventory.getItem(i);

            if (itemStack.isEmpty())
                continue;

            if (itemStack.getItem() == Items.POUCH) {
                PouchInventory pouch = new PouchInventory(itemStack);
                if (pouch.isAutoCollect && flag && itemStack != this.usingPouch) {
                    pouch.collectAllItemStack(inventory, false);
                }
            } else {
                if (isCollectableItem(itemStack)) {
                    mergeItemStack(itemStack, this);
                }
            }
        }
    }

    public void transferToChest(IInventory tile) {
        if (27 <= tile.getContainerSize()) {
            for (int i = 0; i < slotSize; i++) {
                ItemStack itemstack = this.getItem(i);

                if (itemstack != null) {
                    MIMUtils.mergeItemStack(itemstack, tile);
                }
            }
        }
        this.setChanged();
    }

    public boolean getIsStorageBox() {
        return this.isStorageBox;
    }

    public boolean getIsHotBar() {
        return this.isHotBar;
    }

    public boolean getIsAUtoCollect() {
        return this.isAutoCollect;
    }

    public void setIsStorageBox(boolean val) {
        this.isStorageBox = val;
    }

    public void setIsHotBar(boolean val) {
        this.isHotBar = val;
    }

    public void setIsAutoCollect(boolean val) {
        this.isAutoCollect = val;
    }

    public void setValByID(int id, int val) {
        if (Val.values().length <= id) {
            return;
        }
        switch (Val.values()[id]) {
        case STORAGE_BOX:
            setIsStorageBox(MIMUtils.intToBool(val));
            break;
        case HOT_BAR:
            setIsHotBar(MIMUtils.intToBool(val));
            break;
        case AUTO_COLLECT:
            setIsAutoCollect(MIMUtils.intToBool(val));
            break;
        }
        this.writeToNBT(this.usingPouch.getOrCreateTag());

    }

    public int getValByID(int id) {
        if (Val.values().length <= id) {
            return 0;
        }
        switch (Val.values()[id]) {
        case STORAGE_BOX:
            return this.getIsStorageBox() ? 1 : 0;
        case HOT_BAR:
            return this.getIsHotBar() ? 1 : 0;
        case AUTO_COLLECT:
            return this.getIsAUtoCollect() ? 1 : 0;
        }

        return 0;
    }

    public static boolean mergeItemStack(ItemStack itemstack, IInventory inventory) {
        if (itemstack == null) {
            return false;
        }

        boolean success = false;
        int size = slotSize;
        Direction side = Direction.DOWN;

        if (itemstack.isStackable()) {
            for (int i = 0; i < size; ++i) {
                ItemStack item = inventory.getItem(i);

                if (item != null && item.getItem() == itemstack.getItem() && itemstack.getDamageValue() == item.getDamageValue() && ItemStack.tagMatches(itemstack, item)) {
                    if (MIMUtils.canAccessFromSide(inventory, i, side) && MIMUtils.canInsertFromSide(inventory, itemstack, i, side)) {
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

                if (item.getItem() == ItemStack.EMPTY.getItem() && MIMUtils.canAccessFromSide(inventory, i, side) && MIMUtils.canInsertFromSide(inventory, itemstack, i, side)) {
                    inventory.setItem(i, itemstack.copy());
                    itemstack.setCount(0);
                    success = true;
                    break;
                }
            }
        }

        return success;
    }
}
