package moreinventory.container;

import moreinventory.tileentity.BaseTransportTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class TransportContainer extends Container {

    public static TransportContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer extraData) {
        BaseTransportTileEntity tile = (BaseTransportTileEntity) playerInventory.player.world.getTileEntity(extraData.readBlockPos());
        return new TransportContainer(windowID, playerInventory, tile);
    }

    public final int slotSize = BaseTransportTileEntity.inventorySize;

    private BaseTransportTileEntity transportManager;

    public TransportContainer(int windowID, PlayerInventory playerInventory, BaseTransportTileEntity tile) {
        super(Containers.TRANSPORT_MANAGER_CONTAINER_TYPE, windowID);
        this.transportManager = tile;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlot(new Slot(tile, j + i * 3, 54 + 8 + j * 18, 17 + i * 18));
            }
        }

        this.bindPlayerInventory(playerInventory);
    }

    protected void bindPlayerInventory(PlayerInventory player) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(player, i, 8 + i * 18, 138 + 4));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return transportManager != null && transportManager.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        if (0 <= slotId && slotId < 9) {
            if (dragType == 0) {
                ItemStack setItem = player.inventory.getItemStack().copy();
                setItem.setCount(1);
                transportManager.setInventorySlotContents(slotId, setItem);
            } else {
                transportManager.removeStackFromSlot(slotId);
            }
            return player.inventory.getItemStack();
        } else
            return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    public BaseTransportTileEntity getTile() {
        return transportManager;
    }
}
