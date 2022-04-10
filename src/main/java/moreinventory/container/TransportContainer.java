package moreinventory.container;

import moreinventory.block.Blocks;
import moreinventory.tileentity.BaseTransportTileEntity;
import moreinventory.tileentity.ImporterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;

public class TransportContainer extends Container {

    public static TransportContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer extraData) {
        BaseTransportTileEntity tile = (BaseTransportTileEntity) playerInventory.player.level.getBlockEntity(extraData.readBlockPos());
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
        if (tile instanceof ImporterTileEntity) {
            this.trackAllIntFields((ImporterTileEntity) tile, ImporterTileEntity.Val.values().length);
        }
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
    public boolean stillValid(PlayerEntity playerIn) {
        Block block = (transportManager instanceof ImporterTileEntity ? Blocks.IMPORTER : Blocks.EXPORTER);
        return stillValid(IWorldPosCallable.create(transportManager.getLevel(), transportManager.getBlockPos()), playerIn, block);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        if (0 <= slotId && slotId < 9) {
            if (dragType == 0) {
                ItemStack setItem = player.inventory.getCarried().copy();
                setItem.setCount(1);
                transportManager.setItem(slotId, setItem);
            } else {
                transportManager.removeItemNoUpdate(slotId);
            }
            return player.inventory.getCarried();
        } else
            return super.clicked(slotId, dragType, clickTypeIn, player);
    }

    public BaseTransportTileEntity getTile() {
        return transportManager;
    }

    protected void trackAllIntFields(ImporterTileEntity blockEntity, int valCount) {
        for (int f = 0; f < valCount; f++) {
            trackIntField(blockEntity, f);
        }
    }

    protected void trackIntField(ImporterTileEntity blockEntity, int id) {
        addDataSlot(new IntReferenceHolder() {

            @Override
            public int get() {
                return blockEntity.getValByID(id);
            }

            @Override
            public void set(int value) {
                blockEntity.setValByID(id, value);
            }
        });
    }
}
