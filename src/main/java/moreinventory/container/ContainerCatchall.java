package moreinventory.container;

import moreinventory.tileentity.TileEntityCatchall;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class ContainerCatchall extends Container {
    public static ContainerCatchall createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer extraData) {
        TileEntityCatchall tile = (TileEntityCatchall) playerInventory.player.world.getTileEntity(extraData.readBlockPos());
        return new ContainerCatchall(windowID, playerInventory, tile);
    }

    public final int slotSize = TileEntityCatchall.inventorySize;

    private TileEntityCatchall catchall;

    public ContainerCatchall(int windowID, PlayerInventory playerInventory, TileEntityCatchall tile) {
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
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;

        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < slotSize) {
                if (!this.mergeItemStack(itemstack1, slotSize, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, slotSize, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return catchall != null && catchall.isUsableByPlayer(playerIn);
    }
}