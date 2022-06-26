package moreinventory.container;

import moreinventory.blockentity.CatchallBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CatchallContainer extends AbstractContainerMenu {
    public static CatchallContainer createContainerClientSide(int windowID, Inventory playerInventory, FriendlyByteBuf extraData) {
        var blockEntity = (CatchallBlockEntity) playerInventory.player.level.getBlockEntity(extraData.readBlockPos());
        return new CatchallContainer(windowID, playerInventory, blockEntity);
    }

    public final int slotSize = CatchallBlockEntity.inventorySize;

    private CatchallBlockEntity catchall;

    public CatchallContainer(int windowID, Inventory playerInventory, CatchallBlockEntity blockEntity) {
        super(Containers.CATCHALL_CONTAINER_TYPE.get(), windowID);
        this.catchall = blockEntity;

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(blockEntity, i, 8 + i * 18 + 27, 72 + 4));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(blockEntity, j + i * 9 + 9, 8 + j * 18 + 27, 18 + i * 18));
            }
        }
        this.addSlot(new Slot(blockEntity, slotSize - 1, 8, 72 + 4));

        this.bindPlayerInventory(playerInventory);
    }

    protected void bindPlayerInventory(Inventory inventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18 + 27, 108 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18 + 27, 162 + 4));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        var itemstack = ItemStack.EMPTY;

        var slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            var itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < slotSize) {
                if (!this.moveItemStackTo(itemstack1, slotSize, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, slotSize, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return catchall != null && catchall.stillValid(playerIn);
    }
}