package moreinventory.container;

import moreinventory.tileentity.CatchallTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class CatchallContainer extends Container {
    public static CatchallContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer extraData) {
        CatchallTileEntity tile = (CatchallTileEntity) playerInventory.player.level.getBlockEntity(extraData.readBlockPos());
        return new CatchallContainer(windowID, playerInventory, tile);
    }

    public final int slotSize = CatchallTileEntity.inventorySize;

    private CatchallTileEntity catchall;

    public CatchallContainer(int windowID, PlayerInventory playerInventory, CatchallTileEntity tile) {
        super(Containers.CATCHALL_CONTAINER_TYPE, windowID);
        this.catchall = tile;

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(tile, i, 8 + i * 18 + 27, 72 + 4));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(tile, j + i * 9 + 9, 8 + j * 18 + 27, 18 + i * 18));
            }
        }
        this.addSlot(new Slot(tile, slotSize - 1, 8, 72 + 4));

        bindPlayerInventory(playerInventory);
    }

    protected void bindPlayerInventory(PlayerInventory inventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18 + 27, 108 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(inventory, i, 8 + i * 18 + 27, 162 + 4));
        }
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;

        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
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
    public boolean stillValid(PlayerEntity playerIn) {
        return catchall != null && catchall.stillValid(playerIn);
    }
}