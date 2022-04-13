package moreinventory.container;

import moreinventory.block.Blocks;
import moreinventory.blockentity.BaseTransportBlockEntity;
import moreinventory.blockentity.ImporterBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TransportContainer extends AbstractContainerMenu {

    public static TransportContainer createContainerClientSide(int windowID, Inventory playerInventory, FriendlyByteBuf extraData) {
        var blockEntity = (BaseTransportBlockEntity) playerInventory.player.level.getBlockEntity(extraData.readBlockPos());
        return new TransportContainer(windowID, playerInventory, blockEntity);
    }

    public final int slotSize = BaseTransportBlockEntity.inventorySize;

    private BaseTransportBlockEntity transportBlockEntity;

    public TransportContainer(int windowID, Inventory playerInventory, BaseTransportBlockEntity blockEntity) {
        super(Containers.TRANSPORT_CONTAINER_TYPE, windowID);
        this.transportBlockEntity = blockEntity;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlot(new Slot(blockEntity, j + i * 3, 54 + 8 + j * 18, 17 + i * 18));
            }
        }

        this.bindPlayerInventory(playerInventory);
        if (blockEntity instanceof ImporterBlockEntity) {
            this.trackAllIntFields((ImporterBlockEntity) blockEntity, ImporterBlockEntity.Val.values().length);
        }
    }

    protected void bindPlayerInventory(Inventory player) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(player, i, 8 + i * 18, 138 + 4));
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        var block = (transportBlockEntity instanceof ImporterBlockEntity ? Blocks.IMPORTER : Blocks.EXPORTER);
        return stillValid(ContainerLevelAccess.create(transportBlockEntity.getLevel(), transportBlockEntity.getBlockPos()), playerIn, block);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (0 <= slotId && slotId < 9) {
            if (dragType == 0) {
                var setItem = player.getInventory().player.containerMenu.getCarried().copy();
                setItem.setCount(1);
                transportBlockEntity.setItem(slotId, setItem);
            } else {
                transportBlockEntity.removeItemNoUpdate(slotId);
            }
        } else {
            super.clicked(slotId, dragType, clickTypeIn, player);
        }
    }

    public BaseTransportBlockEntity getBlockEntity() {
        return transportBlockEntity;
    }

    protected void trackAllIntFields(ImporterBlockEntity blockEntity, int valCount) {
        for (int f = 0; f < valCount; f++) {
            trackIntField(blockEntity, f);
        }
    }

    protected void trackIntField(ImporterBlockEntity blockEntity, int id) {
        addDataSlot(new DataSlot() {

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
