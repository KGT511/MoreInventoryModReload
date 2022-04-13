package moreinventory.blockentity;

import moreinventory.block.CatchallBlock;
import moreinventory.container.CatchallContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public class CatchallBlockEntity extends RandomizableContainerBlockEntity {
    public CatchallBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.CATCHALL_BLOCK_ENTITY_TYPE, pos, state);
    }

    public static final int mainInventorySize = 36;
    public static final int armorInventorySize = 4;
    public static final int offHandInventorySize = 1;

    public static final int inventorySize = mainInventorySize + offHandInventorySize;

    private NonNullList<ItemStack> storage = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    private LazyOptional<IItemHandlerModifiable> storageHandler;

    @Override
    public int getContainerSize() {
        return storage.size();
    }

    @Override
    public Component getDefaultName() {
        return new TranslatableComponent("block.moreinventorymod.catchall");
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.storage = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, this.storage);
        }

    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        if (!this.trySaveLootTable(compound)) {
            ContainerHelper.saveAllItems(compound, this.storage);
        }

        return compound;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.storage;
    }

    @Override
    public void setItems(NonNullList<ItemStack> itemsIn) {
        this.storage = itemsIn;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new CatchallContainer(id, player, this);
    }

    //	@Override
    //	public void updateContainingBlockInfo() {
    //		super.updateContainingBlockInfo();
    //		if (this.storageHandler != null) {
    //			this.storageHandler.invalidate();
    //			this.storageHandler = null;
    //		}
    //	}

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (this.storageHandler == null)
                this.storageHandler = LazyOptional.of(this::createHandler);
            return this.storageHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    private IItemHandlerModifiable createHandler() {
        var state = this.getBlockState();
        if (!(state.getBlock() instanceof CatchallBlock)) {
            return new InvWrapper(this);
        }
        return new InvWrapper(this);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.getBlockPos(), 0, this.save(new CompoundTag()));
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.save(super.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (storageHandler != null)
            storageHandler.invalidate();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); ++i)
            if (!getItem(i).isEmpty())
                return false;

        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return storage.get(index);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (!getItem(index).isEmpty()) {
            var itemstack = getItem(index);
            setItem(index, ItemStack.EMPTY);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        storage.set(index, stack);

        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        var pos = this.getBlockPos();
        return this.level.getBlockEntity(pos) == this
                && player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 64;
    }

    public boolean transferTo(Player player) {
        if (isEmpty()) {
            transferToBlock(player);
            return true;
        }

        if (canTransferToPlayer(player)) {
            transferToPlayer(player);
            return true;
        }

        return false;
    }

    private void transferToBlock(Player player) {
        for (int i = 0; i < getContainerSize(); ++i) {
            if (getItem(i).isEmpty() && !player.getInventory().getItem(convertIdx(i)).isEmpty()) {
                setItem(i, player.getInventory().removeItemNoUpdate(convertIdx(i)));
            }
        }

        player.tick();
    }

    public boolean canTransferToPlayer(Player player) {
        for (int i = 0; i < getContainerSize(); ++i) {
            if (!getItem(i).isEmpty() && !player.getInventory().getItem(convertIdx(i)).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public void transferToPlayer(Player player) {
        for (int i = 0; i < getContainerSize(); ++i) {
            if (!getItem(i).isEmpty() && player.getInventory().getItem(convertIdx(i)).isEmpty()) {
                player.getInventory().setItem(convertIdx(i), removeItemNoUpdate(i));
            }
        }

        player.tick();
    }

    private int convertIdx(int indexIn) {
        return indexIn < mainInventorySize ? indexIn : indexIn + armorInventorySize;
    }

}
